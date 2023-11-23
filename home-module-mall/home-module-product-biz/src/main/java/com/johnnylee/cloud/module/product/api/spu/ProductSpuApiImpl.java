package com.johnnylee.cloud.module.product.api.spu;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.product.api.spu.dto.ProductSpuRespDTO;
import com.johnnylee.cloud.module.product.convert.spu.ProductSpuConvert;
import com.johnnylee.cloud.module.product.service.spu.ProductSpuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

/**
 * 商品 SPU API 接口实现类
 *
 * @author LeeYan9
 * @since 2022-09-06
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class ProductSpuApiImpl implements ProductSpuApi {

    @Resource
    private ProductSpuService spuService;

    @Override
    public CommonResult<List<ProductSpuRespDTO>> getSpuList(Collection<Long> ids) {
        return success(ProductSpuConvert.INSTANCE.convertList2(spuService.getSpuList(ids)));
    }

    @Override
    public CommonResult<List<ProductSpuRespDTO>> validateSpuList(Collection<Long> ids) {
        return success(ProductSpuConvert.INSTANCE.convertList2(spuService.validateSpuList(ids)));
    }

    @Override
    public CommonResult<ProductSpuRespDTO> getSpu(Long id) {
        return success(ProductSpuConvert.INSTANCE.convert02(spuService.getSpu(id)));
    }

}
