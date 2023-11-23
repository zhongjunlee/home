package com.johnnylee.cloud.module.statistics.controller.admin.trade.vo;

import com.johnnylee.cloud.framework.excel.core.convert.MoneyConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.time.LocalDate;

import static com.johnnylee.cloud.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

/**
 * 交易状况统计 Excel VO
 *
 * @author owen
 */
@Data
public class TradeTrendSummaryExcelVO {

    @ExcelProperty(value = "日期")
    @DateTimeFormat(FORMAT_YEAR_MONTH_DAY)
    private LocalDate date;

    @ExcelProperty(value = "营业额", converter = MoneyConvert.class)
    private Integer turnoverPrice;

    @ExcelProperty(value = "商品支付金额", converter = MoneyConvert.class)
    private Integer orderPayPrice;

    @ExcelProperty(value = "充值金额", converter = MoneyConvert.class)
    private Integer rechargePrice;

    @ExcelProperty(value = "支出金额", converter = MoneyConvert.class)
    private Integer expensePrice;

    @ExcelProperty(value = "余额支付金额", converter = MoneyConvert.class)
    private Integer walletPayPrice;

    @ExcelProperty(value = "支付佣金金额", converter = MoneyConvert.class)
    private Integer brokerageSettlementPrice;

    @ExcelProperty(value = "商品退款金额", converter = MoneyConvert.class)
    private Integer afterSaleRefundPrice;
}
