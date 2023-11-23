package com.johnnylee.cloud.module.trade.convert.order;

import com.johnnylee.cloud.module.trade.dal.dataobject.order.TradeOrderLogDO;
import com.johnnylee.cloud.module.trade.service.order.bo.TradeOrderLogCreateReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TradeOrderLogConvert {

    TradeOrderLogConvert INSTANCE = Mappers.getMapper(TradeOrderLogConvert.class);

    TradeOrderLogDO convert(TradeOrderLogCreateReqBO bean);

}
