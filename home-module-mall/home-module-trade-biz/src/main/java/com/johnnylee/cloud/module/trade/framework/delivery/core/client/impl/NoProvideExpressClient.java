package com.johnnylee.cloud.module.trade.framework.delivery.core.client.impl;

import com.johnnylee.cloud.module.trade.framework.delivery.core.client.ExpressClient;
import com.johnnylee.cloud.module.trade.framework.delivery.core.client.dto.ExpressTrackQueryReqDTO;
import com.johnnylee.cloud.module.trade.framework.delivery.core.client.dto.ExpressTrackRespDTO;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.module.trade.enums.ErrorCodeConstants;

import java.util.List;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 未实现的快递客户端，用来提醒用户需要接入快递服务商，
 *
 * @author jason
 */
public class NoProvideExpressClient implements ExpressClient {

    @Override
    public List<ExpressTrackRespDTO> getExpressTrackList(ExpressTrackQueryReqDTO reqDTO) {
        throw ServiceExceptionUtil.exception(ErrorCodeConstants.EXPRESS_CLIENT_NOT_PROVIDE);
    }

}
