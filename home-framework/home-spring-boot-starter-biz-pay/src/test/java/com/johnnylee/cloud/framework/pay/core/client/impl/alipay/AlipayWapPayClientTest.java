package com.johnnylee.cloud.framework.pay.core.client.impl.alipay;

import cn.hutool.http.Method;
import com.johnnylee.cloud.framework.pay.core.client.dto.order.PayOrderRespDTO;
import com.johnnylee.cloud.framework.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.johnnylee.cloud.framework.pay.core.enums.order.PayOrderDisplayModeEnum;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.johnnylee.cloud.framework.pay.core.enums.order.PayOrderStatusRespEnum;
import com.johnnylee.cloud.framework.test.core.util.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * {@link AlipayWapPayClient} 单元测试
 *
 * @author jason
 */
public class AlipayWapPayClientTest extends AbstractAlipayClientTest {

    /**
     * 支付宝 H5 支付 Client
     */
    @InjectMocks
    private AlipayWapPayClient client = new AlipayWapPayClient(RandomUtils.randomLongId(), config);

    @BeforeEach
    public void setUp() {
        setClient(client);
    }

    @Test
    @DisplayName("支付宝 H5 支付：下单成功")
    public void testUnifiedOrder_success() throws AlipayApiException {
        // mock 方法
        String h5Body = RandomUtils.randomString();
        Integer price = RandomUtils.randomInteger();
        AlipayTradeWapPayResponse response = RandomUtils.randomPojo(AlipayTradeWapPayResponse.class, o -> {
            o.setSubCode("");
            o.setBody(h5Body);
        });
        String notifyUrl = RandomUtils.randomURL();
        when(defaultAlipayClient.pageExecute(argThat((ArgumentMatcher<AlipayTradeWapPayRequest>) request -> {
            assertInstanceOf(AlipayTradeWapPayModel.class, request.getBizModel());
            AlipayTradeWapPayModel bizModel = (AlipayTradeWapPayModel) request.getBizModel();
            assertEquals(String.valueOf(price / 100.0), bizModel.getTotalAmount());
            assertEquals(notifyUrl, request.getNotifyUrl());
            return true;
        }), eq(Method.GET.name()))).thenReturn(response);
        // 准备请求参数
        String outTradeNo = RandomUtils.randomString();
        PayOrderUnifiedReqDTO reqDTO = buildOrderUnifiedReqDTO(notifyUrl, outTradeNo, price);

        // 调用
        PayOrderRespDTO resp = client.unifiedOrder(reqDTO);
        // 断言
        Assertions.assertEquals(PayOrderStatusRespEnum.WAITING.getStatus(), resp.getStatus());
        assertEquals(outTradeNo, resp.getOutTradeNo());
        assertNull(resp.getChannelOrderNo());
        assertNull(resp.getChannelUserId());
        assertNull(resp.getSuccessTime());
        Assertions.assertEquals(PayOrderDisplayModeEnum.URL.getMode(), resp.getDisplayMode());
        assertEquals(response.getBody(), resp.getDisplayContent());
        assertSame(response, resp.getRawData());
        assertNull(resp.getChannelErrorCode());
        assertNull(resp.getChannelErrorMsg());
    }

    @Test
    @DisplayName("支付宝 H5 支付：渠道返回失败")
    public void test_unified_order_channel_failed() throws AlipayApiException {
        // mock 方法
        String subCode = RandomUtils.randomString();
        String subMsg = RandomUtils.randomString();
        AlipayTradeWapPayResponse response = RandomUtils.randomPojo(AlipayTradeWapPayResponse.class, o -> {
            o.setSubCode(subCode);
            o.setSubMsg(subMsg);
        });
        when(defaultAlipayClient.pageExecute(argThat((ArgumentMatcher<AlipayTradeWapPayRequest>) request -> true),
                eq(Method.GET.name()))).thenReturn(response);
        String outTradeNo = RandomUtils.randomString();
        PayOrderUnifiedReqDTO reqDTO = buildOrderUnifiedReqDTO(RandomUtils.randomURL(), outTradeNo, RandomUtils.randomInteger());

        // 调用
        PayOrderRespDTO resp = client.unifiedOrder(reqDTO);
        // 断言
        Assertions.assertEquals(PayOrderStatusRespEnum.CLOSED.getStatus(), resp.getStatus());
        assertEquals(outTradeNo, resp.getOutTradeNo());
        assertNull(resp.getChannelOrderNo());
        assertNull(resp.getChannelUserId());
        assertNull(resp.getSuccessTime());
        assertNull(resp.getDisplayMode());
        assertNull(resp.getDisplayContent());
        assertSame(response, resp.getRawData());
        assertEquals(subCode, resp.getChannelErrorCode());
        assertEquals(subMsg, resp.getChannelErrorMsg());
    }

}
