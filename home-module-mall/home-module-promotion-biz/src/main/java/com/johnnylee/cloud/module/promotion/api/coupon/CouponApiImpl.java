package com.johnnylee.cloud.module.promotion.api.coupon;


import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.promotion.api.coupon.dto.CouponRespDTO;
import com.johnnylee.cloud.module.promotion.api.coupon.dto.CouponUseReqDTO;
import com.johnnylee.cloud.module.promotion.api.coupon.dto.CouponValidReqDTO;
import com.johnnylee.cloud.module.promotion.convert.coupon.CouponConvert;
import com.johnnylee.cloud.module.promotion.dal.dataobject.coupon.CouponDO;
import com.johnnylee.cloud.module.promotion.service.coupon.CouponService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

/**
 * 优惠劵 API 实现类
 *
 * @author Johnny
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class CouponApiImpl implements CouponApi {

    @Resource
    private CouponService couponService;

    @Override
    public CommonResult<Boolean> useCoupon(CouponUseReqDTO useReqDTO) {
        couponService.useCoupon(useReqDTO.getId(), useReqDTO.getUserId(), useReqDTO.getOrderId());
        return success(true);
    }

    @Override
    public CommonResult<Boolean> returnUsedCoupon(Long id) {
        couponService.returnUsedCoupon(id);
        return success(true);
    }

    @Override
    public CommonResult<CouponRespDTO> validateCoupon(CouponValidReqDTO validReqDTO) {
        CouponDO coupon = couponService.validCoupon(validReqDTO.getId(), validReqDTO.getUserId());
        return success(CouponConvert.INSTANCE.convert(coupon));
    }

}
