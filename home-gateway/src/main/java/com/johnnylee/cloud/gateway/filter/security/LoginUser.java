package com.johnnylee.cloud.gateway.filter.security;

import lombok.Data;

import java.util.List;

/**
 * 登录用户信息
 *
 * copy from home-spring-boot-starter-security 的 LoginUser 类
 *
 * @author Johnny
 */
@Data
public class LoginUser {

    /**
     * 用户编号
     */
    private Long id;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 租户编号
     */
    private Long tenantId;
    /**
     * 授权范围
     */
    private List<String> scopes;

}
