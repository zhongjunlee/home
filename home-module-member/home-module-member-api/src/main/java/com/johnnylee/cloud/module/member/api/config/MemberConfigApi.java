package com.johnnylee.cloud.module.member.api.config;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.member.api.config.dto.MemberConfigRespDTO;
import com.johnnylee.cloud.module.member.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 用户配置")
public interface MemberConfigApi {

    String PREFIX = ApiConstants.PREFIX + "/config";

    @GetMapping(PREFIX + "/get")
    @Operation(summary = "获得用户配置")
    CommonResult<MemberConfigRespDTO> getConfig();

}
