package com.johnnylee.cloud.module.member.controller.app.signin;

import com.johnnylee.cloud.framework.common.enums.CommonStatusEnum;
import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.member.controller.app.signin.vo.config.AppMemberSignInConfigRespVO;
import com.johnnylee.cloud.module.member.convert.signin.MemberSignInConfigConvert;
import com.johnnylee.cloud.module.member.dal.dataobject.signin.MemberSignInConfigDO;
import com.johnnylee.cloud.module.member.service.signin.MemberSignInConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 签到规则")
@RestController
@RequestMapping("/member/sign-in/config")
@Validated
public class AppMemberSignInConfigController {

    @Resource
    private MemberSignInConfigService signInConfigService;

    @GetMapping("/list")
    @Operation(summary = "获得签到规则列表")
    public CommonResult<List<AppMemberSignInConfigRespVO>> getSignInConfigList() {
        List<MemberSignInConfigDO> pageResult = signInConfigService.getSignInConfigList(CommonStatusEnum.ENABLE.getStatus());
        return success(MemberSignInConfigConvert.INSTANCE.convertList02(pageResult));
    }

}
