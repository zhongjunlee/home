package com.johnnylee.cloud.module.trade.dal.mysql.order;

import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.module.trade.dal.dataobject.order.TradeOrderLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TradeOrderLogMapper extends BaseMapperX<TradeOrderLogDO> {

    default List<TradeOrderLogDO> selectListByOrderId(Long orderId) {
        return selectList(TradeOrderLogDO::getOrderId, orderId);
    }

}
