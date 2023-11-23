package com.johnnylee.cloud.module.pay.api.transfer;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.pay.api.transfer.dto.PayTransferCreateReqDTO;
import com.johnnylee.cloud.module.pay.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 转账单")
public interface PayTransferApi {

    String PREFIX = ApiConstants.PREFIX + "/transfer";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建转账单")
    CommonResult<Long> createTransfer(@Valid @RequestBody PayTransferCreateReqDTO reqDTO);

}
