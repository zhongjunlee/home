package com.johnnylee.cloud.module.product.dal.mysql.property;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.property.ProductPropertyListReqVO;
import com.johnnylee.cloud.module.product.controller.admin.property.vo.property.ProductPropertyPageReqVO;
import com.johnnylee.cloud.module.product.dal.dataobject.property.ProductPropertyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductPropertyMapper extends BaseMapperX<ProductPropertyDO> {

    default PageResult<ProductPropertyDO> selectPage(ProductPropertyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductPropertyDO>()
                .likeIfPresent(ProductPropertyDO::getName, reqVO.getName())
                .betweenIfPresent(ProductPropertyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductPropertyDO::getId));
    }

    default ProductPropertyDO selectByName(String name) {
        return selectOne(ProductPropertyDO::getName, name);
    }

    default List<ProductPropertyDO> selectList(ProductPropertyListReqVO listReqVO) {
        return selectList(new LambdaQueryWrapperX<ProductPropertyDO>()
                .eqIfPresent(ProductPropertyDO::getName, listReqVO.getName()));
    }

}
