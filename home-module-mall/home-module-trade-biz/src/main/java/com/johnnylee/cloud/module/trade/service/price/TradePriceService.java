package com.johnnylee.cloud.module.trade.service.price;

import com.johnnylee.cloud.module.trade.service.price.bo.TradePriceCalculateReqBO;
import com.johnnylee.cloud.module.trade.service.price.bo.TradePriceCalculateRespBO;

import javax.validation.Valid;

/**
 * 价格计算 Service 接口
 *
 * @author Johnny
 */
public interface TradePriceService {

    /**
     * 价格计算
     *
     * @param calculateReqDTO 计算信息
     * @return 计算结果
     */
    TradePriceCalculateRespBO calculatePrice(@Valid TradePriceCalculateReqBO calculateReqDTO);

}
