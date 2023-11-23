package com.johnnylee.cloud.module.statistics.controller.admin.pay;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.statistics.controller.admin.pay.vo.PaySummaryRespVO;
import com.johnnylee.cloud.module.statistics.convert.pay.PayStatisticsConvert;
import com.johnnylee.cloud.module.statistics.service.pay.PayWalletStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 支付统计")
@RestController
@RequestMapping("/statistics/pay")
@Validated
@Slf4j
public class PayStatisticsController {

    @Resource
    private PayWalletStatisticsService payWalletStatisticsService;

    @GetMapping("/summary")
    @Operation(summary = "获取充值金额")
    public CommonResult<PaySummaryRespVO> getWalletRechargePrice() {
        Integer rechargePrice = payWalletStatisticsService.getRechargePriceSummary();
        return success(PayStatisticsConvert.INSTANCE.convert(rechargePrice));
    }

}
