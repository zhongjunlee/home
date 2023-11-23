package com.johnnylee.cloud.module.pay.service.wallet;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.pay.controller.admin.wallet.vo.transaction.PayWalletTransactionPageReqVO;
import com.johnnylee.cloud.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import com.johnnylee.cloud.module.pay.convert.wallet.PayWalletTransactionConvert;
import com.johnnylee.cloud.module.pay.dal.dataobject.wallet.PayWalletDO;
import com.johnnylee.cloud.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import com.johnnylee.cloud.module.pay.dal.mysql.wallet.PayWalletTransactionMapper;
import com.johnnylee.cloud.module.pay.dal.redis.no.PayNoRedisDAO;
import com.johnnylee.cloud.module.pay.enums.wallet.PayWalletBizTypeEnum;
import com.johnnylee.cloud.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 钱包流水 Service 实现类
 *
 * @author jason
 */
@Service
@Slf4j
@Validated
public class PayWalletTransactionServiceImpl implements PayWalletTransactionService {

    /**
     * 钱包流水的 no 前缀
     */
    private static final String WALLET_NO_PREFIX = "W";

    @Resource
    private PayWalletService payWalletService;
    @Resource
    private PayWalletTransactionMapper payWalletTransactionMapper;
    @Resource
    private PayNoRedisDAO noRedisDAO;

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionPage(Long userId, Integer userType,
                                                                       AppPayWalletTransactionPageReqVO pageVO) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        return payWalletTransactionMapper.selectPage(wallet.getId(), pageVO.getType(), pageVO);
    }

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionPage(PayWalletTransactionPageReqVO pageVO) {
        return payWalletTransactionMapper.selectPage(pageVO.getWalletId(), null, pageVO);
    }

    @Override
    public PayWalletTransactionDO createWalletTransaction(WalletTransactionCreateReqBO bo) {
        PayWalletTransactionDO transaction = PayWalletTransactionConvert.INSTANCE.convert(bo)
                .setNo(noRedisDAO.generate(WALLET_NO_PREFIX));
        payWalletTransactionMapper.insert(transaction);
        return transaction;
    }

    @Override
    public PayWalletTransactionDO getWalletTransactionByNo(String no) {
        return payWalletTransactionMapper.selectByNo(no);
    }

    @Override
    public PayWalletTransactionDO getWalletTransaction(String bizId, PayWalletBizTypeEnum type) {
        return payWalletTransactionMapper.selectByBiz(bizId, type.getType());
    }

}
