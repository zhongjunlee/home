package com.johnnylee.cloud.module.product.convert.property;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.value.ProductPropertyValueCreateReqVO;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.value.ProductPropertyValueRespVO;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.value.ProductPropertyValueUpdateReqVO;
import com.johnnylee.cloud.module.product.dal.dataobject.property.ProductPropertyValueDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 属性值 Convert
 *
 * @author Johnny
 */
@Mapper
public interface ProductPropertyValueConvert {

    ProductPropertyValueConvert INSTANCE = Mappers.getMapper(ProductPropertyValueConvert.class);

    ProductPropertyValueDO convert(ProductPropertyValueCreateReqVO bean);

    ProductPropertyValueDO convert(ProductPropertyValueUpdateReqVO bean);

    ProductPropertyValueRespVO convert(ProductPropertyValueDO bean);

    List<ProductPropertyValueRespVO> convertList(List<ProductPropertyValueDO> list);

    PageResult<ProductPropertyValueRespVO> convertPage(PageResult<ProductPropertyValueDO> page);

}
