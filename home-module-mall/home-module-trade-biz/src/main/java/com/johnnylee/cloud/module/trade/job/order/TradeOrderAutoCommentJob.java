package com.johnnylee.cloud.module.trade.job.order;

import com.johnnylee.cloud.framework.tenant.core.job.TenantJob;
import com.johnnylee.cloud.module.trade.service.order.TradeOrderUpdateService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 交易订单的自动评论 Job
 *
 * @author Johnny
 */
@Component
public class TradeOrderAutoCommentJob {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;

    @XxlJob("tradeOrderAutoCommentJob")
    @TenantJob // 多租户
    public String execute() {
        int count = tradeOrderUpdateService.createOrderItemCommentBySystem();
        return String.format("评论订单 %s 个", count);
    }

}
