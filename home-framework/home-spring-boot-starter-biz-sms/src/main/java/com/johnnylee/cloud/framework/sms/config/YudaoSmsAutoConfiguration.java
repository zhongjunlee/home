package com.johnnylee.cloud.framework.sms.config;

import com.johnnylee.cloud.framework.sms.core.client.SmsClientFactory;
import com.johnnylee.cloud.framework.sms.core.client.impl.SmsClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 短信配置类
 *
 * @author Johnny
 */
@AutoConfiguration
public class YudaoSmsAutoConfiguration {

    @Bean
    public SmsClientFactory smsClientFactory() {
        return new SmsClientFactoryImpl();
    }

}
