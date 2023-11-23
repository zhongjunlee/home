package com.johnnylee.cloud.framework.sms.core.client.impl.debug;

import com.johnnylee.cloud.framework.common.exception.ErrorCode;
import com.johnnylee.cloud.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.johnnylee.cloud.framework.sms.core.client.SmsCodeMapping;
import com.johnnylee.cloud.framework.sms.core.enums.SmsFrameworkErrorCodeConstants;

import java.util.Objects;

/**
 * 钉钉的 SmsCodeMapping 实现类
 *
 * @author Johnny
 */
public class DebugDingTalkCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        return Objects.equals(apiCode, "0") ? GlobalErrorCodeConstants.SUCCESS : SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;
    }

}
