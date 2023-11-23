package com.johnnylee.cloud.module.member.mq.message.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 会员用户创建消息
 *
 * @author owen
 */
@Data
public class UserCreateMessage {

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;

}
