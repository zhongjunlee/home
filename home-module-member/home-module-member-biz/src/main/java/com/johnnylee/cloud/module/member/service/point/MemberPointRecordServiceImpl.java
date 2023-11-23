package com.johnnylee.cloud.module.member.service.point;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.PageParam;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.member.controller.admin.point.vo.recrod.MemberPointRecordPageReqVO;
import com.johnnylee.cloud.module.member.dal.dataobject.point.MemberPointRecordDO;
import com.johnnylee.cloud.module.member.dal.dataobject.user.MemberUserDO;
import com.johnnylee.cloud.module.member.dal.mysql.point.MemberPointRecordMapper;
import com.johnnylee.cloud.module.member.enums.point.MemberPointBizTypeEnum;
import com.johnnylee.cloud.module.member.service.user.MemberUserService;
import com.johnnylee.cloud.module.member.enums.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.johnnylee.cloud.framework.common.util.collection.CollectionUtils.convertSet;


/**
 * 积分记录 Service 实现类
 *
 * @author QingX
 */
@Slf4j
@Service
@Validated
public class MemberPointRecordServiceImpl implements MemberPointRecordService {

    @Resource
    private MemberPointRecordMapper memberPointRecordMapper;

    @Resource
    private MemberUserService memberUserService;

    @Override
    public PageResult<MemberPointRecordDO> getPointRecordPage(MemberPointRecordPageReqVO pageReqVO) {
        // 根据用户昵称查询出用户 ids
        Set<Long> userIds = null;
        if (StringUtils.isNotBlank(pageReqVO.getNickname())) {
            List<MemberUserDO> users = memberUserService.getUserListByNickname(pageReqVO.getNickname());
            // 如果查询用户结果为空直接返回无需继续查询
            if (CollectionUtils.isEmpty(users)) {
                return PageResult.empty();
            }
            userIds = convertSet(users, MemberUserDO::getId);
        }
        // 执行查询
        return memberPointRecordMapper.selectPage(pageReqVO, userIds);
    }

    @Override
    public PageResult<MemberPointRecordDO> getPointRecordPage(Long userId, PageParam pageVO) {
        return memberPointRecordMapper.selectPage(userId, pageVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPointRecord(Long userId, Integer point, MemberPointBizTypeEnum bizType, String bizId) {
        if (point == 0) {
            return;
        }
        // 1. 校验用户积分余额
        MemberUserDO user = memberUserService.getUser(userId);
        Integer userPoint = ObjectUtil.defaultIfNull(user.getPoint(), 0);
        int totalPoint = userPoint + point; // 用户变动后的积分
        if (totalPoint < 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_POINT_NOT_ENOUGH);
        }

        // 2. 更新用户积分
        boolean success = memberUserService.updateUserPoint(userId, point);
        if (!success) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_POINT_NOT_ENOUGH);
        }

        // 3. 增加积分记录
        MemberPointRecordDO record = new MemberPointRecordDO()
                .setUserId(userId).setBizId(bizId).setBizType(bizType.getType())
                .setTitle(bizType.getName()).setDescription(StrUtil.format(bizType.getDescription(), point))
                .setPoint(point).setTotalPoint(totalPoint);
        memberPointRecordMapper.insert(record);
    }

}
