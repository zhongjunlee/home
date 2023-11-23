package com.johnnylee.cloud.module.statistics.convert.pay;

import com.johnnylee.cloud.module.statistics.controller.admin.pay.vo.PaySummaryRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付统计 Convert
 *
 * @author owen
 */
@Mapper
public interface PayStatisticsConvert {

    PayStatisticsConvert INSTANCE = Mappers.getMapper(PayStatisticsConvert.class);

    PaySummaryRespVO convert(Integer rechargePrice);

}
