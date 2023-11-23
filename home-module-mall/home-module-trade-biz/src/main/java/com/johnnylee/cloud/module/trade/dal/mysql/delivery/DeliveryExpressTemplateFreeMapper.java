package com.johnnylee.cloud.module.trade.dal.mysql.delivery;

import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.module.trade.dal.dataobject.delivery.DeliveryExpressTemplateFreeDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DeliveryExpressTemplateFreeMapper extends BaseMapperX<DeliveryExpressTemplateFreeDO> {

    default List<DeliveryExpressTemplateFreeDO> selectListByTemplateId(Long templateId) {
        return selectList(new LambdaQueryWrapper<DeliveryExpressTemplateFreeDO>()
                .eq(DeliveryExpressTemplateFreeDO::getTemplateId, templateId));
    }

    default int deleteByTemplateId(Long templateId) {
        return delete(new LambdaQueryWrapper<DeliveryExpressTemplateFreeDO>()
                .eq(DeliveryExpressTemplateFreeDO::getTemplateId, templateId));
    }

    default List<DeliveryExpressTemplateFreeDO> selectListByTemplateIds(Collection<Long> templateIds) {
        return selectList(DeliveryExpressTemplateFreeDO::getTemplateId, templateIds);
    }
}




