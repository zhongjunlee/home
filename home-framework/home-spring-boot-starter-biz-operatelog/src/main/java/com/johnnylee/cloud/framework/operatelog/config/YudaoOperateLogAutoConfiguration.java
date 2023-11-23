package com.johnnylee.cloud.framework.operatelog.config;

import com.johnnylee.cloud.framework.operatelog.core.aop.OperateLogAspect;
import com.johnnylee.cloud.framework.operatelog.core.service.OperateLogFrameworkService;
import com.johnnylee.cloud.framework.operatelog.core.service.OperateLogFrameworkServiceImpl;
import com.johnnylee.cloud.module.system.api.logger.OperateLogApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class YudaoOperateLogAutoConfiguration {

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

    @Bean
    public OperateLogFrameworkService operateLogFrameworkService(OperateLogApi operateLogApi) {
        return new OperateLogFrameworkServiceImpl(operateLogApi);
    }

}
