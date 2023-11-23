package com.johnnylee.cloud.module.trade.api.order;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.trade.api.order.dto.TradeOrderRespDTO;
import com.johnnylee.cloud.module.trade.convert.order.TradeOrderConvert;
import com.johnnylee.cloud.module.trade.service.order.TradeOrderQueryService;
import com.johnnylee.cloud.module.trade.service.order.TradeOrderUpdateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

/**
 * 订单 API 接口实现类
 *
 * @author HUIHUI
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class TradeOrderApiImpl implements TradeOrderApi {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private TradeOrderQueryService tradeOrderQueryService;

    @Override
    public CommonResult<List<TradeOrderRespDTO>> getOrderList(Collection<Long> ids) {
        return success(TradeOrderConvert.INSTANCE.convertList04(tradeOrderQueryService.getOrderList(ids)));
    }

    @Override
    public CommonResult<TradeOrderRespDTO> getOrder(Long id) {
        return success(TradeOrderConvert.INSTANCE.convert(tradeOrderQueryService.getOrder(id)));
    }

    @Override
    public CommonResult<Boolean> cancelPaidOrder(Long userId, Long orderId) {
        tradeOrderUpdateService.cancelPaidOrder(userId, orderId);
        return success(true);
    }

}
