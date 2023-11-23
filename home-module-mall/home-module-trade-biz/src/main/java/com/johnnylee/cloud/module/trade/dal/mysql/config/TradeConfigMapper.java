package com.johnnylee.cloud.module.trade.dal.mysql.config;

import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.module.trade.dal.dataobject.config.TradeConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易中心配置 Mapper
 *
 * @author owen
 */
@Mapper
public interface TradeConfigMapper extends BaseMapperX<TradeConfigDO> {

}
