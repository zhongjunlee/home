package com.johnnylee.cloud.module.pay.job.refund;

import com.johnnylee.cloud.framework.tenant.core.job.TenantJob;
import com.johnnylee.cloud.module.pay.service.refund.PayRefundService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 退款订单的同步 Job
 *
 * 由于退款订单的状态，是由支付渠道异步通知进行同步，考虑到异步通知可能会失败（小概率），所以需要定时进行同步。
 *
 * @author Johnny
 */
@Component
@Slf4j
public class PayRefundSyncJob {

    @Resource
    private PayRefundService refundService;

    @XxlJob("payRefundSyncJob")
    @TenantJob // 多租户
    public void execute() {
        int count = refundService.syncRefund();
        log.info("[execute][同步退款订单 ({}) 个]", count);
    }

}
