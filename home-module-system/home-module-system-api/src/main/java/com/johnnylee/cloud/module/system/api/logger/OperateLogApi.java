package com.johnnylee.cloud.module.system.api.logger;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.system.api.logger.dto.OperateLogCreateReqDTO;
import com.johnnylee.cloud.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 操作日志")
public interface OperateLogApi {

    String PREFIX = ApiConstants.PREFIX + "/operate-log";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建操作日志")
    CommonResult<Boolean> createOperateLog(@Valid @RequestBody OperateLogCreateReqDTO createReqDTO);

}
