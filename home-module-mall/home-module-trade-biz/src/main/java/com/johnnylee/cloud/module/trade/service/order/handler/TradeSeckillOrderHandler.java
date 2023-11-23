package com.johnnylee.cloud.module.trade.service.order.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.johnnylee.cloud.module.promotion.api.seckill.SeckillActivityApi;
import com.johnnylee.cloud.module.trade.dal.dataobject.order.TradeOrderDO;
import com.johnnylee.cloud.module.trade.dal.dataobject.order.TradeOrderItemDO;
import com.johnnylee.cloud.module.trade.enums.order.TradeOrderTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 秒杀订单的 {@link TradeOrderHandler} 实现类
 *
 * @author HUIHUI
 */
@Component
public class TradeSeckillOrderHandler implements TradeOrderHandler {

    @Resource
    private SeckillActivityApi seckillActivityApi;

    @Override
    public void beforeOrderCreate(TradeOrderDO order, List<TradeOrderItemDO> orderItems) {
        if (!TradeOrderTypeEnum.isSeckill(order.getType())) {
            return;
        }
        // 明确校验一下
        Assert.isTrue(orderItems.size() == 1, "秒杀时，只允许选择一个商品");

        // 扣减秒杀活动的库存
        seckillActivityApi.updateSeckillStockDecr(order.getSeckillActivityId(),
                orderItems.get(0).getSkuId(), orderItems.get(0).getCount());
    }

    @Override
    public void afterCancelOrder(TradeOrderDO order, List<TradeOrderItemDO> orderItems) {
        if (!TradeOrderTypeEnum.isSeckill(order.getType())) {
            return;
        }
        // 明确校验一下
        Assert.isTrue(orderItems.size() == 1, "秒杀时，只允许选择一个商品");

        // 售后的订单项，已经在 afterCancelOrderItem 回滚库存，所以这里不需要重复回滚
        orderItems = filterOrderItemListByNoneAfterSale(orderItems);
        if (CollUtil.isEmpty(orderItems)) {
            return;
        }
        afterCancelOrderItem(order, orderItems.get(0));
    }

    @Override
    public void afterCancelOrderItem(TradeOrderDO order, TradeOrderItemDO orderItem) {
        if (!TradeOrderTypeEnum.isSeckill(order.getType())) {
            return;
        }
        // 恢复秒杀活动的库存
        seckillActivityApi.updateSeckillStockIncr(order.getSeckillActivityId(),
                orderItem.getSkuId(), orderItem.getCount());
    }

}
