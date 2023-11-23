package com.johnnylee.cloud.module.system.api.social;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.system.api.social.dto.SocialWxJsapiSignatureRespDTO;
import com.johnnylee.cloud.module.system.api.social.dto.SocialWxPhoneNumberInfoRespDTO;
import com.johnnylee.cloud.module.system.convert.social.SocialClientConvert;
import com.johnnylee.cloud.module.system.service.social.SocialClientService;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

/**
 * 社交应用的 API 实现类
 *
 * @author Johnny
 */
@RestController
@Validated
public class SocialClientApiImpl implements SocialClientApi {

    @Resource
    private SocialClientService socialClientService;

    @Override
    public CommonResult<String> getAuthorizeUrl(Integer socialType, Integer userType, String redirectUri) {
        return success(socialClientService.getAuthorizeUrl(socialType, userType, redirectUri));
    }

    @Override
    public CommonResult<SocialWxJsapiSignatureRespDTO> createWxMpJsapiSignature(Integer userType, String url) {
        WxJsapiSignature signature = socialClientService.createWxMpJsapiSignature(userType, url);
        return success(SocialClientConvert.INSTANCE.convert(signature));
    }

    @Override
    public CommonResult<SocialWxPhoneNumberInfoRespDTO> getWxMaPhoneNumberInfo(Integer userType, String phoneCode) {
        WxMaPhoneNumberInfo info = socialClientService.getWxMaPhoneNumberInfo(userType, phoneCode);
        return success(SocialClientConvert.INSTANCE.convert(info));
    }

}
