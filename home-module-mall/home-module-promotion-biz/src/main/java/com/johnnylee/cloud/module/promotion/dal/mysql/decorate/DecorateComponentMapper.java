package com.johnnylee.cloud.module.promotion.dal.mysql.decorate;

import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.johnnylee.cloud.module.promotion.dal.dataobject.decorate.DecorateComponentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DecorateComponentMapper extends BaseMapperX<DecorateComponentDO> {

    default List<DecorateComponentDO> selectListByPageAndStatus(Integer page, Integer status) {
        return selectList(new LambdaQueryWrapperX<DecorateComponentDO>()
                        .eq(DecorateComponentDO::getPage, page)
                        .eqIfPresent(DecorateComponentDO::getStatus, status));
    }

    default DecorateComponentDO selectByPageAndCode(Integer page, String code) {
        return selectOne(DecorateComponentDO::getPage, page,
                DecorateComponentDO::getCode, code);
    }

}




