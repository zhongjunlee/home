package com.johnnylee.cloud.module.system.mq;

import cn.hutool.core.thread.ThreadUtil;
import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import com.johnnylee.cloud.module.system.mq.consumer.mail.MailSendConsumer;
import com.johnnylee.cloud.module.system.mq.consumer.sms.SmsSendConsumer;
import com.johnnylee.cloud.module.system.mq.message.mail.MailSendMessage;
import com.johnnylee.cloud.module.system.mq.message.sms.SmsSendMessage;
import com.johnnylee.cloud.module.system.test.BaseRedisIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public class RedisStreamTest  {

    @Import({SmsSendConsumer.class, MailSendConsumer.class})
    @Disabled
    public static class ConsumerTest extends BaseRedisIntegrationTest {

        @Test
        public void testConsumer() {
            ThreadUtil.sleep(1, TimeUnit.DAYS);
        }

    }

    @Disabled
    public static class ProducerTest extends BaseRedisIntegrationTest {

        @Resource
        private RedisMQTemplate redisMQTemplate;

        @Resource
        private RedisTemplate<String, Object> redisTemplate;

        @Test
        public void testProducer01() {
            for (int i = 0; i < 100; i++) {
                // 创建消息
                SmsSendMessage message = new SmsSendMessage();
                message.setMobile("15601691300").setApiTemplateId("test:" + i);
                // 发送消息
                redisMQTemplate.send(message);
            }
        }

        @Test
        public void testProducer02() {
            // 创建消息
            MailSendMessage message = new MailSendMessage();
            message.setAddress("fangfang@mihayou.com").setTemplateCode("test");
            // 发送消息
            redisMQTemplate.send(message);
        }

    }

}
