package com.johnnylee.cloud.module.promotion.framework.rpc.config;

import com.johnnylee.cloud.module.member.api.user.MemberUserApi;
import com.johnnylee.cloud.module.product.api.category.ProductCategoryApi;
import com.johnnylee.cloud.module.product.api.sku.ProductSkuApi;
import com.johnnylee.cloud.module.product.api.spu.ProductSpuApi;
import com.johnnylee.cloud.module.trade.api.order.TradeOrderApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {ProductSkuApi.class, ProductSpuApi.class, ProductCategoryApi.class,
        MemberUserApi.class, TradeOrderApi.class})
public class RpcConfiguration {
}
