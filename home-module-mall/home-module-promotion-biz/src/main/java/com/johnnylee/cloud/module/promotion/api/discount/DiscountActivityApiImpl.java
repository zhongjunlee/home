package com.johnnylee.cloud.module.promotion.api.discount;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.promotion.api.discount.dto.DiscountProductRespDTO;
import com.johnnylee.cloud.module.promotion.convert.discount.DiscountActivityConvert;
import com.johnnylee.cloud.module.promotion.service.discount.DiscountActivityService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

/**
 * 限时折扣 API 实现类
 *
 * @author Johnny
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class DiscountActivityApiImpl implements DiscountActivityApi {

    @Resource
    private DiscountActivityService discountActivityService;

    @Override
    public CommonResult<List<DiscountProductRespDTO>> getMatchDiscountProductList(Collection<Long> skuIds) {
        return success(DiscountActivityConvert.INSTANCE.convertList02(discountActivityService.getMatchDiscountProductList(skuIds)));
    }

}
