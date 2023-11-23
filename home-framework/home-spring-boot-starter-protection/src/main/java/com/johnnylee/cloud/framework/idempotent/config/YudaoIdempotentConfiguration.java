package com.johnnylee.cloud.framework.idempotent.config;

import com.johnnylee.cloud.framework.idempotent.core.aop.IdempotentAspect;
import com.johnnylee.cloud.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.johnnylee.cloud.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.johnnylee.cloud.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import com.johnnylee.cloud.framework.idempotent.core.redis.IdempotentRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import com.johnnylee.cloud.framework.redis.config.YudaoRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = YudaoRedisAutoConfiguration.class)
public class YudaoIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
