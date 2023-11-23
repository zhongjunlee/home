package com.johnnylee.cloud.module.product.api.category;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.product.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 商品分类")
public interface ProductCategoryApi {

    String PREFIX = ApiConstants.PREFIX + "/category";

    @GetMapping(PREFIX + "/valid")
    @Operation(summary = "校验部门是否合法")
    @Parameter(name = "ids", description = "商品分类编号数组", example = "1,2", required = true)
    CommonResult<Boolean> validateCategoryList(@RequestParam("ids") Collection<Long> ids);

}
