package com.johnnylee.cloud.framework.file.config;

import com.johnnylee.cloud.framework.file.core.client.FileClientFactory;
import com.johnnylee.cloud.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 文件配置类
 *
 * @author Johnny
 */
@AutoConfiguration
public class YudaoFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
