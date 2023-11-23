package com.johnnylee.cloud.framework.pay.config;

import com.johnnylee.cloud.framework.pay.core.client.PayClientFactory;
import com.johnnylee.cloud.framework.pay.core.client.impl.PayClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 支付配置类
 *
 * @author Johnny
 */
@AutoConfiguration
public class YudaoPayAutoConfiguration {

    @Bean
    public PayClientFactory payClientFactory() {
        return new PayClientFactoryImpl();
    }

}
