package com.johnnylee.cloud.module.product.service.property;

import cn.hutool.core.util.ObjUtil;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.property.ProductPropertyCreateReqVO;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.property.ProductPropertyListReqVO;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.property.ProductPropertyPageReqVO;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.property.ProductPropertyUpdateReqVO;
import com.johnnylee.cloud.module.product.convert.property.ProductPropertyConvert;
import com.johnnylee.cloud.module.product.dal.dataobject.property.ProductPropertyDO;
import com.johnnylee.cloud.module.product.dal.mysql.property.ProductPropertyMapper;
import com.johnnylee.cloud.module.product.service.sku.ProductSkuService;
import com.johnnylee.cloud.module.product.enums.ErrorCodeConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 商品属性项 Service 实现类
 *
 * @author Johnny
 */
@Service
@Validated
public class ProductPropertyServiceImpl implements ProductPropertyService {

    @Resource
    private ProductPropertyMapper productPropertyMapper;

    @Resource
    @Lazy // 延迟加载，解决循环依赖问题
    private ProductPropertyValueService productPropertyValueService;

    @Resource
    private ProductSkuService productSkuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProperty(ProductPropertyCreateReqVO createReqVO) {
        // 如果已经添加过该属性项，直接返回
        ProductPropertyDO dbProperty = productPropertyMapper.selectByName(createReqVO.getName());
        if (dbProperty != null) {
            return dbProperty.getId();
        }

        // 插入
        ProductPropertyDO property = ProductPropertyConvert.INSTANCE.convert(createReqVO);
        productPropertyMapper.insert(property);
        // 返回
        return property.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProperty(ProductPropertyUpdateReqVO updateReqVO) {
        validatePropertyExists(updateReqVO.getId());
        // 校验名字重复
        ProductPropertyDO productPropertyDO = productPropertyMapper.selectByName(updateReqVO.getName());
        if (productPropertyDO != null &&
                ObjUtil.notEqual(productPropertyDO.getId(), updateReqVO.getId())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROPERTY_EXISTS);
        }

        // 更新
        ProductPropertyDO updateObj = ProductPropertyConvert.INSTANCE.convert(updateReqVO);
        productPropertyMapper.updateById(updateObj);
        // 更新 sku 相关属性
        productSkuService.updateSkuProperty(updateObj.getId(), updateObj.getName());
    }

    @Override
    public void deleteProperty(Long id) {
        // 校验存在
        validatePropertyExists(id);
        // 校验其下是否有规格值
        if (productPropertyValueService.getPropertyValueCountByPropertyId(id) > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROPERTY_DELETE_FAIL_VALUE_EXISTS);
        }

        // 删除
        productPropertyMapper.deleteById(id);
        // 同步删除属性值
        productPropertyValueService.deletePropertyValueByPropertyId(id);
    }

    private void validatePropertyExists(Long id) {
        if (productPropertyMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PROPERTY_NOT_EXISTS);
        }
    }

    @Override
    public List<ProductPropertyDO> getPropertyList(ProductPropertyListReqVO listReqVO) {
        return productPropertyMapper.selectList(listReqVO);
    }

    @Override
    public PageResult<ProductPropertyDO> getPropertyPage(ProductPropertyPageReqVO pageReqVO) {
        return productPropertyMapper.selectPage(pageReqVO);
    }

    @Override
    public ProductPropertyDO getProperty(Long id) {
        return productPropertyMapper.selectById(id);
    }

    @Override
    public List<ProductPropertyDO> getPropertyList(Collection<Long> ids) {
        return productPropertyMapper.selectBatchIds(ids);
    }

}
