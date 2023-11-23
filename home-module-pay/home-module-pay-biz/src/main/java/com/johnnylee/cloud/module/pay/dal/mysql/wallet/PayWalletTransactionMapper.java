package com.johnnylee.cloud.module.pay.dal.mysql.wallet;


import com.johnnylee.cloud.framework.common.pojo.PageParam;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.johnnylee.cloud.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import com.johnnylee.cloud.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Objects;

@Mapper
public interface PayWalletTransactionMapper extends BaseMapperX<PayWalletTransactionDO> {

    default PageResult<PayWalletTransactionDO> selectPage(Long walletId, Integer type,
                                                          PageParam pageParam) {
        LambdaQueryWrapperX<PayWalletTransactionDO> query = new LambdaQueryWrapperX<PayWalletTransactionDO>()
                .eqIfPresent(PayWalletTransactionDO::getWalletId, walletId);
        if (Objects.equals(type, AppPayWalletTransactionPageReqVO.TYPE_INCOME)) {
            query.gt(PayWalletTransactionDO::getPrice, 0);
        } else if (Objects.equals(type, AppPayWalletTransactionPageReqVO.TYPE_EXPENSE)) {
            query.lt(PayWalletTransactionDO::getPrice, 0);
        }
        query.orderByDesc(PayWalletTransactionDO::getId);
        return selectPage(pageParam, query);
    }

    default PayWalletTransactionDO selectByNo(String no) {
        return selectOne(PayWalletTransactionDO::getNo, no);
    }

    default PayWalletTransactionDO selectByBiz(String bizId, Integer bizType) {
        return selectOne(PayWalletTransactionDO::getBizId, bizId,
                PayWalletTransactionDO::getBizType, bizType);
    }

}




