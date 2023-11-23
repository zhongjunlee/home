package com.johnnylee.cloud.module.pay.enums.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付通知类型
 *
 * @author Johnny
 */
@Getter
@AllArgsConstructor
public enum PayNotifyTypeEnum {

    ORDER(1, "支付单"),
    REFUND(2, "退款单"),
    ;

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名字
     */
    private final String name;

}
