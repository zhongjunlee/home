package com.johnnylee.cloud.module.promotion.api.coupon;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.promotion.api.coupon.dto.CouponRespDTO;
import com.johnnylee.cloud.module.promotion.api.coupon.dto.CouponUseReqDTO;
import com.johnnylee.cloud.module.promotion.api.coupon.dto.CouponValidReqDTO;
import com.johnnylee.cloud.module.promotion.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 优惠劵")
public interface CouponApi {

    String PREFIX = ApiConstants.PREFIX + "/coupon";

    @PutMapping(PREFIX + "/use")
    @Operation(summary = "使用优惠劵")
    CommonResult<Boolean> useCoupon(@RequestBody @Valid CouponUseReqDTO useReqDTO);

    @PutMapping(PREFIX + "/return-used")
    @Parameter(name = "id", description = "优惠券编号", required = true, example = "1")
    CommonResult<Boolean> returnUsedCoupon(@RequestParam("id") Long id);

    @GetMapping(PREFIX + "/validate")
    @Operation(summary = "校验优惠劵")
    CommonResult<CouponRespDTO> validateCoupon(@Valid CouponValidReqDTO validReqDTO);

}
