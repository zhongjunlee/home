package com.johnnylee.cloud.module.system.convert.social;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.johnnylee.cloud.module.system.api.social.dto.SocialWxJsapiSignatureRespDTO;
import com.johnnylee.cloud.module.system.api.social.dto.SocialWxPhoneNumberInfoRespDTO;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SocialClientConvert {

    SocialClientConvert INSTANCE = Mappers.getMapper(SocialClientConvert.class);

    SocialWxJsapiSignatureRespDTO convert(WxJsapiSignature bean);

    SocialWxPhoneNumberInfoRespDTO convert(WxMaPhoneNumberInfo bean);

}
