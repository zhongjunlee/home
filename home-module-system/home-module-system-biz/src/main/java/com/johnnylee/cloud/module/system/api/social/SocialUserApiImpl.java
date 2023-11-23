package com.johnnylee.cloud.module.system.api.social;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.system.api.social.dto.SocialUserBindReqDTO;
import com.johnnylee.cloud.module.system.api.social.dto.SocialUserRespDTO;
import com.johnnylee.cloud.module.system.api.social.dto.SocialUserUnbindReqDTO;
import com.johnnylee.cloud.module.system.service.social.SocialUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class SocialUserApiImpl implements SocialUserApi {

    @Resource
    private SocialUserService socialUserService;

    @Override
    public CommonResult<String> bindSocialUser(SocialUserBindReqDTO reqDTO) {
        return success(socialUserService.bindSocialUser(reqDTO));
    }

    @Override
    public CommonResult<Boolean> unbindSocialUser(SocialUserUnbindReqDTO reqDTO) {
        socialUserService.unbindSocialUser(reqDTO.getUserId(), reqDTO.getUserType(),
                reqDTO.getSocialType(), reqDTO.getOpenid());
        return success(true);
    }

    @Override
    public CommonResult<SocialUserRespDTO> getSocialUser(Integer userType, Integer socialType, String code, String state) {
       return success(socialUserService.getSocialUser(userType, socialType, code, state));
    }

}
