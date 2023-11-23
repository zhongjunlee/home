package com.johnnylee.cloud.framework.pay.core.client.impl.alipay;

import cn.hutool.http.Method;
import com.johnnylee.cloud.framework.pay.core.client.dto.order.PayOrderRespDTO;
import com.johnnylee.cloud.framework.pay.core.client.dto.order.PayOrderUnifiedReqDTO;
import com.johnnylee.cloud.framework.pay.core.enums.order.PayOrderDisplayModeEnum;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
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
 * {@link  AlipayPcPayClient} 单元测试
 *
 * @author jason
 */
public class AlipayPcPayClientTest extends AbstractAlipayClientTest {

    @InjectMocks
    private AlipayPcPayClient client = new AlipayPcPayClient(RandomUtils.randomLongId(), config);

    @Override
    @BeforeEach
    public void setUp() {
        setClient(client);
    }

    @Test
    @DisplayName("支付宝 PC 网站支付：URL Display Mode 下单成功")
    public void testUnifiedOrder_urlSuccess() throws AlipayApiException {
        // mock 方法
        String notifyUrl = RandomUtils.randomURL();
        AlipayTradePagePayResponse response = RandomUtils.randomPojo(AlipayTradePagePayResponse.class, o -> o.setSubCode(""));
        when(defaultAlipayClient.pageExecute(argThat((ArgumentMatcher<AlipayTradePagePayRequest>) request -> true),
                eq(Method.GET.name()))).thenReturn(response);
        // 准备请求参数
        String outTradeNo = RandomUtils.randomString();
        Integer price = RandomUtils.randomInteger();
        PayOrderUnifiedReqDTO reqDTO = buildOrderUnifiedReqDTO(notifyUrl, outTradeNo, price);
        reqDTO.setDisplayMode(null);

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
    @DisplayName("支付宝 PC 网站支付：Form Display Mode 下单成功")
    public void testUnifiedOrder_formSuccess() throws AlipayApiException {
        // mock 方法
        String notifyUrl = RandomUtils.randomURL();
        AlipayTradePagePayResponse response = RandomUtils.randomPojo(AlipayTradePagePayResponse.class, o -> o.setSubCode(""));
        when(defaultAlipayClient.pageExecute(argThat((ArgumentMatcher<AlipayTradePagePayRequest>) request -> true),
                eq(Method.POST.name()))).thenReturn(response);
        // 准备请求参数
        String outTradeNo = RandomUtils.randomString();
        Integer price = RandomUtils.randomInteger();
        PayOrderUnifiedReqDTO reqDTO = buildOrderUnifiedReqDTO(notifyUrl, outTradeNo, price);
        reqDTO.setDisplayMode(PayOrderDisplayModeEnum.FORM.getMode());

        // 调用
        PayOrderRespDTO resp = client.unifiedOrder(reqDTO);
        // 断言
        Assertions.assertEquals(PayOrderStatusRespEnum.WAITING.getStatus(), resp.getStatus());
        assertEquals(outTradeNo, resp.getOutTradeNo());
        assertNull(resp.getChannelOrderNo());
        assertNull(resp.getChannelUserId());
        assertNull(resp.getSuccessTime());
        assertEquals(PayOrderDisplayModeEnum.FORM.getMode(), resp.getDisplayMode());
        assertEquals(response.getBody(), resp.getDisplayContent());
        assertSame(response, resp.getRawData());
        assertNull(resp.getChannelErrorCode());
        assertNull(resp.getChannelErrorMsg());
    }

    @Test
    @DisplayName("支付宝 PC 网站支付：渠道返回失败")
    public void testUnifiedOrder_channelFailed() throws AlipayApiException {
        // mock 方法
        String subCode = RandomUtils.randomString();
        String subMsg = RandomUtils.randomString();
        AlipayTradePagePayResponse response = RandomUtils.randomPojo(AlipayTradePagePayResponse.class, o -> {
            o.setSubCode(subCode);
            o.setSubMsg(subMsg);
        });
        when(defaultAlipayClient.pageExecute(argThat((ArgumentMatcher<AlipayTradePagePayRequest>) request -> true),
                eq(Method.GET.name()))).thenReturn(response);
        // 准备请求参数
        String outTradeNo = RandomUtils.randomString();
        PayOrderUnifiedReqDTO reqDTO = buildOrderUnifiedReqDTO(RandomUtils.randomURL(), outTradeNo, RandomUtils.randomInteger());
        reqDTO.setDisplayMode(PayOrderDisplayModeEnum.URL.getMode());

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
