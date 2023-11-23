package com.johnnylee.cloud.framework.env.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 环境配置
 *
 * @author Johnny
 */
@ConfigurationProperties(prefix = "yudao.env")
@Data
public class EnvProperties {

    public static final String TAG_KEY = "yudao.env.tag";

    /**
     * 环境标签
     */
    private String tag;

}
