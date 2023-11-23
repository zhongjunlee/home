package com.johnnylee.cloud.module.bpm.framework.rpc.config;

import com.johnnylee.cloud.module.system.api.dept.DeptApi;
import com.johnnylee.cloud.module.system.api.dept.PostApi;
import com.johnnylee.cloud.module.system.api.dict.DictDataApi;
import com.johnnylee.cloud.module.system.api.permission.RoleApi;
import com.johnnylee.cloud.module.system.api.sms.SmsSendApi;
import com.johnnylee.cloud.module.system.api.user.AdminUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {RoleApi.class, DeptApi.class, PostApi.class, AdminUserApi.class, SmsSendApi.class, DictDataApi.class})
public class RpcConfiguration {
}
