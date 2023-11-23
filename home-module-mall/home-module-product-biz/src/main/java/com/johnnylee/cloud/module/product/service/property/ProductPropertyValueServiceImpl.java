package com.johnnylee.cloud.module.product.service.property;

import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.value.ProductPropertyValueCreateReqVO;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.value.ProductPropertyValuePageReqVO;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.value.ProductPropertyValueUpdateReqVO;
import com.johnnylee.cloud.module.product.convert.property.ProductPropertyValueConvert;
import com.johnnylee.cloud.module.product.dal.dataobject.property.ProductPropertyValueDO;
import com.johnnylee.cloud.module.product.dal.mysql.property.ProductPropertyValueMapper;
import com.johnnylee.cloud.module.product.service.sku.ProductSkuService;
import com.johnnylee.cloud.module.product.enums.ErrorCodeConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 商品属性值 Service 实现类
 *
 * @author LuoWenFeng
 */
@Service
@Validated
public class ProductPropertyValueServiceImpl implements ProductPropertyValueService {

    @Resource
    private ProductPropertyValueMapper productPropertyValueMapper;

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private ProductSkuService productSkuService;

    @Override
    public Long createPropertyValue(ProductPropertyValueCreateReqVO createReqVO) {
        // 如果已经添加过该属性值，直接返回
        ProductPropertyValueDO dbValue = productPropertyValueMapper.selectByName(
                createReqVO.getPropertyId(), createReqVO.getName());
        if (dbValue != null) {
            return dbValue.getId();
        }

        // 新增
        ProductPropertyValueDO value = ProductPropertyValueConvert.INSTANCE.convert(createReqVO);
        productPropertyValueMapper.insert(value);
        return value.getId();
    }

    @Override
    public void updatePropertyValue(ProductPropertyValueUpdateReqVO updateReqVO) {
        validatePropertyValueExists(updateReqVO.getId());
        // 校验名字唯一
        ProductPropertyValueDO productPropertyValueDO = productPropertyValueMapper.selectByName
                (updateReqVO.getPropertyId(), updateReqVO.getName());
        if (productPropertyValueDO != null && !productPropertyValueDO.getId().equals(updateReqVO.getId())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROPERTY_VALUE_EXISTS);
        }

        // 更新
        ProductPropertyValueDO updateObj = ProductPropertyValueConvert.INSTANCE.convert(updateReqVO);
        productPropertyValueMapper.updateById(updateObj);
        // 更新 sku 相关属性
        productSkuService.updateSkuPropertyValue(updateObj.getId(), updateObj.getName());
    }

    @Override
    public void deletePropertyValue(Long id) {
        validatePropertyValueExists(id);
        productPropertyValueMapper.deleteById(id);
    }

    private void validatePropertyValueExists(Long id) {
        if (productPropertyValueMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROPERTY_VALUE_NOT_EXISTS);
        }
    }

    @Override
    public ProductPropertyValueDO getPropertyValue(Long id) {
        return productPropertyValueMapper.selectById(id);
    }

    @Override
    public List<ProductPropertyValueDO> getPropertyValueListByPropertyId(Collection<Long> propertyIds) {
        return productPropertyValueMapper.selectListByPropertyId(propertyIds);
    }

    @Override
    public Integer getPropertyValueCountByPropertyId(Long propertyId) {
        return productPropertyValueMapper.selectCountByPropertyId(propertyId);
    }

    @Override
    public PageResult<ProductPropertyValueDO> getPropertyValuePage(ProductPropertyValuePageReqVO pageReqVO) {
        return productPropertyValueMapper.selectPage(pageReqVO);
    }

    @Override
    public void deletePropertyValueByPropertyId(Long propertyId) {
        productPropertyValueMapper.deleteByPropertyId(propertyId);
    }

}
