package com.johnnylee.cloud.module.mp.service.message;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.mp.controller.admin.message.vo.message.MpMessagePageReqVO;
import com.johnnylee.cloud.module.mp.controller.admin.message.vo.message.MpMessageSendReqVO;
import com.johnnylee.cloud.module.mp.convert.message.MpMessageConvert;
import com.johnnylee.cloud.module.mp.dal.dataobject.account.MpAccountDO;
import com.johnnylee.cloud.module.mp.dal.dataobject.message.MpMessageDO;
import com.johnnylee.cloud.module.mp.dal.dataobject.user.MpUserDO;
import com.johnnylee.cloud.module.mp.dal.mysql.message.MpMessageMapper;
import com.johnnylee.cloud.module.mp.enums.message.MpMessageSendFromEnum;
import com.johnnylee.cloud.module.mp.framework.mp.core.MpServiceFactory;
import com.johnnylee.cloud.module.mp.framework.mp.core.util.MpUtils;
import com.johnnylee.cloud.module.mp.service.account.MpAccountService;
import com.johnnylee.cloud.module.mp.service.material.MpMaterialService;
import com.johnnylee.cloud.module.mp.service.message.bo.MpMessageSendOutReqBO;
import com.johnnylee.cloud.module.mp.service.user.MpUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Validator;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.johnnylee.cloud.module.mp.enums.ErrorCodeConstants.MESSAGE_SEND_FAIL;

/**
 * 粉丝消息 Service 实现类
 *
 * @author Johnny
 */
@Service
@Validated
@Slf4j
public class MpMessageServiceImpl implements MpMessageService {

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private MpAccountService mpAccountService;
    @Resource
    private MpUserService mpUserService;
    @Resource
    private MpMaterialService mpMaterialService;

    @Resource
    private MpMessageMapper mpMessageMapper;

    @Resource
    @Lazy // 延迟加载，解决循环依赖的问题
    private MpServiceFactory mpServiceFactory;

    @Resource
    private Validator validator;

    @Override
    public PageResult<MpMessageDO> getMessagePage(MpMessagePageReqVO pageReqVO) {
        return mpMessageMapper.selectPage(pageReqVO);
    }

    @Override
    public void receiveMessage(String appId, WxMpXmlMessage wxMessage) {
        // 获得关联信息
        MpAccountDO account = mpAccountService.getAccountFromCache(appId);
        Assert.notNull(account, "公众号账号({}) 不存在", appId);
        MpUserDO user = mpUserService.getUser(appId, wxMessage.getFromUser());
        Assert.notNull(user, "公众号粉丝({}/{}) 不存在", appId, wxMessage.getFromUser());

        // 记录消息
        MpMessageDO message = MpMessageConvert.INSTANCE.convert(wxMessage, account, user)
                .setSendFrom(MpMessageSendFromEnum.USER_TO_MP.getFrom());
        downloadMessageMedia(message);
        mpMessageMapper.insert(message);
    }

    @Override
    public WxMpXmlOutMessage sendOutMessage(MpMessageSendOutReqBO sendReqBO) {
        // 校验消息格式
        MpUtils.validateMessage(validator, sendReqBO.getType(), sendReqBO);

        // 获得关联信息
        MpAccountDO account = mpAccountService.getAccountFromCache(sendReqBO.getAppId());
        Assert.notNull(account, "公众号账号({}) 不存在", sendReqBO.getAppId());
        MpUserDO user = mpUserService.getUser(sendReqBO.getAppId(), sendReqBO.getOpenid());
        Assert.notNull(user, "公众号粉丝({}/{}) 不存在", sendReqBO.getAppId(), sendReqBO.getOpenid());

        // 记录消息
        MpMessageDO message = MpMessageConvert.INSTANCE.convert(sendReqBO, account, user).
                setSendFrom(MpMessageSendFromEnum.MP_TO_USER.getFrom());
        downloadMessageMedia(message);
        mpMessageMapper.insert(message);

        // 转换返回 WxMpXmlOutMessage 对象
        return MpMessageConvert.INSTANCE.convert02(message, account);
    }

    @Override
    public MpMessageDO sendKefuMessage(MpMessageSendReqVO sendReqVO) {
        // 校验消息格式
        MpUtils.validateMessage(validator, sendReqVO.getType(), sendReqVO);

        // 获得关联信息
        MpUserDO user = mpUserService.getRequiredUser(sendReqVO.getUserId());
        MpAccountDO account = mpAccountService.getRequiredAccount(user.getAccountId());

        // 发送客服消息
        WxMpKefuMessage wxMessage = MpMessageConvert.INSTANCE.convert(sendReqVO, user);
        WxMpService mpService = mpServiceFactory.getRequiredMpService(user.getAppId());
        try {
            mpService.getKefuService().sendKefuMessageWithResponse(wxMessage);
        } catch (WxErrorException e) {
            throw exception(MESSAGE_SEND_FAIL, e.getError().getErrorMsg());
        }

        // 记录消息
        MpMessageDO message = MpMessageConvert.INSTANCE.convert(wxMessage, account, user)
                .setSendFrom(MpMessageSendFromEnum.MP_TO_USER.getFrom());
        downloadMessageMedia(message);
        mpMessageMapper.insert(message);
        return message;
    }

    /**
     * 下载消息使用到的媒体文件，并上传到文件服务
     *
     * @param message 消息
     */
    private void downloadMessageMedia(MpMessageDO message) {
        if (StrUtil.isNotEmpty(message.getMediaId())) {
            message.setMediaUrl(mpMaterialService.downloadMaterialUrl(message.getAccountId(),
                    message.getMediaId(), MpUtils.getMediaFileType(message.getType())));
        }
        if (StrUtil.isNotEmpty(message.getThumbMediaId())) {
            message.setThumbMediaUrl(mpMaterialService.downloadMaterialUrl(message.getAccountId(),
                    message.getThumbMediaId(), WxConsts.MediaFileType.THUMB));
        }
    }

}
