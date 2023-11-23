package com.johnnylee.cloud.module.pay.job.order;

import com.johnnylee.cloud.framework.tenant.core.job.TenantJob;
import com.johnnylee.cloud.module.pay.service.order.PayOrderService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 支付订单的过期 Job
 *
 * 支付超过过期时间时，支付渠道是不会通知进行过期，所以需要定时进行过期关闭。
 *
 * @author Johnny
 */
@Component
@Slf4j
public class PayOrderExpireJob {

    @Resource
    private PayOrderService orderService;

    @XxlJob("payOrderExpireJob")
    @TenantJob // 多租户
    public void execute(String param) {
        int count = orderService.expireOrder();
        log.info("[execute][支付过期 ({}) 个]", count);
    }

}
