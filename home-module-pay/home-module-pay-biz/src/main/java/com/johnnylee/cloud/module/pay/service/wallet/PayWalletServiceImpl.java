package com.johnnylee.cloud.module.pay.service.wallet;

import cn.hutool.core.lang.Assert;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.pay.controller.admin.wallet.vo.wallet.PayWalletPageReqVO;
import com.johnnylee.cloud.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import com.johnnylee.cloud.module.pay.dal.dataobject.refund.PayRefundDO;
import com.johnnylee.cloud.module.pay.dal.dataobject.wallet.PayWalletDO;
import com.johnnylee.cloud.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import com.johnnylee.cloud.module.pay.dal.mysql.wallet.PayWalletMapper;
import com.johnnylee.cloud.module.pay.enums.wallet.PayWalletBizTypeEnum;
import com.johnnylee.cloud.module.pay.service.order.PayOrderService;
import com.johnnylee.cloud.module.pay.service.refund.PayRefundService;
import com.johnnylee.cloud.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import com.johnnylee.cloud.module.pay.enums.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 钱包 Service 实现类
 *
 * @author jason
 */
@Service
@Slf4j
public class PayWalletServiceImpl implements  PayWalletService {

    @Resource
    private PayWalletMapper walletMapper;
    @Resource
    private PayWalletTransactionService walletTransactionService;
    @Resource
    @Lazy
    private PayOrderService orderService;
    @Resource
    @Lazy
    private PayRefundService refundService;

    @Override
    public PayWalletDO getOrCreateWallet(Long userId, Integer userType) {
        PayWalletDO wallet = walletMapper.selectByUserIdAndType(userId, userType);
        if (wallet == null) {
            wallet = new PayWalletDO().setUserId(userId).setUserType(userType)
                    .setBalance(0).setTotalExpense(0).setTotalRecharge(0);
            wallet.setCreateTime(LocalDateTime.now());
            walletMapper.insert(wallet);
        }
        return wallet;
    }

    @Override
    public PayWalletDO getWallet(Long walletId) {
        return walletMapper.selectById(walletId);
    }

    @Override
    public PageResult<PayWalletDO> getWalletPage(Integer userType,PayWalletPageReqVO pageReqVO) {
        return walletMapper.selectPage(userType, pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayWalletTransactionDO orderPay(Long userId, Integer userType, String outTradeNo, Integer price) {
        // 1. 判断支付交易拓展单是否存
        PayOrderExtensionDO orderExtension = orderService.getOrderExtensionByNo(outTradeNo);
        if (orderExtension == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PAY_ORDER_EXTENSION_NOT_FOUND);
        }
        PayWalletDO wallet = getOrCreateWallet(userId, userType);
        // 2. 扣减余额
        return reduceWalletBalance(wallet.getId(), orderExtension.getOrderId(), PayWalletBizTypeEnum.PAYMENT, price);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayWalletTransactionDO orderRefund(String outRefundNo, Integer refundPrice, String reason) {
        // 1.1 判断退款单是否存在
        PayRefundDO payRefund = refundService.getRefundByNo(outRefundNo);
        if (payRefund == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.REFUND_NOT_FOUND);
        }
        // 1.2 校验是否可以退款
        Long walletId = validateWalletCanRefund(payRefund.getId(), payRefund.getChannelOrderNo());
        PayWalletDO wallet = walletMapper.selectById(walletId);
        Assert.notNull(wallet, "钱包 {} 不存在", walletId);

        // 2. 增加余额
        return addWalletBalance(walletId, String.valueOf(payRefund.getId()), PayWalletBizTypeEnum.PAYMENT_REFUND, refundPrice);
    }

    /**
     * 校验是否能退款
     *
     * @param refundId 支付退款单 id
     * @param walletPayNo 钱包支付 no
     */
    private Long validateWalletCanRefund(Long refundId, String walletPayNo) {
        // 1. 校验钱包支付交易存在
        PayWalletTransactionDO walletTransaction = walletTransactionService.getWalletTransactionByNo(walletPayNo);
        if (walletTransaction == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WALLET_TRANSACTION_NOT_FOUND);
        }
        // 2. 校验退款是否存在
        PayWalletTransactionDO refundTransaction = walletTransactionService.getWalletTransaction(
                String.valueOf(refundId), PayWalletBizTypeEnum.PAYMENT_REFUND);
        if (refundTransaction != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WALLET_REFUND_EXIST);
        }
        return walletTransaction.getWalletId();
    }

    @Override
    public PayWalletTransactionDO reduceWalletBalance(Long walletId, Long bizId,
                                                      PayWalletBizTypeEnum bizType, Integer price) {
        // 1. 获取钱包
        PayWalletDO payWallet = getWallet(walletId);
        if (payWallet == null) {
            log.error("[reduceWalletBalance]，用户钱包({})不存在.", walletId);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WALLET_NOT_FOUND);
        }

        // 2.1 扣除余额
        int updateCounts;
        switch (bizType) {
            case PAYMENT: {
                updateCounts = walletMapper.updateWhenConsumption(payWallet.getId(), price);
                break;
            }
            case RECHARGE_REFUND: {
                updateCounts = walletMapper.updateWhenRechargeRefund(payWallet.getId(), price);
                break;
            }
            default: {
                // TODO 其它类型待实现
                throw new UnsupportedOperationException("待实现");
            }
        }
        if (updateCounts == 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WALLET_BALANCE_NOT_ENOUGH);
        }
        // 2.2 生成钱包流水
        Integer afterBalance = payWallet.getBalance() - price;
        WalletTransactionCreateReqBO bo = new WalletTransactionCreateReqBO().setWalletId(payWallet.getId())
                .setPrice(-price).setBalance(afterBalance).setBizId(String.valueOf(bizId))
                .setBizType(bizType.getType()).setTitle(bizType.getDescription());
        return walletTransactionService.createWalletTransaction(bo);
    }

    @Override
    public PayWalletTransactionDO addWalletBalance(Long walletId, String bizId,
                                                   PayWalletBizTypeEnum bizType, Integer price) {
        // 1.1 获取钱包
        PayWalletDO payWallet = getWallet(walletId);
        if (payWallet == null) {
            log.error("[addWalletBalance]，用户钱包({})不存在.", walletId);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WALLET_NOT_FOUND);
        }
        // 1.2 更新钱包金额
        switch (bizType) {
            case PAYMENT_REFUND: { // 退款更新
                walletMapper.updateWhenConsumptionRefund(payWallet.getId(), price);
                break;
            }
            case RECHARGE: { // 充值更新
                walletMapper.updateWhenRecharge(payWallet.getId(), price);
                break;
            }
            default: {
                // TODO 其它类型待实现
                throw new UnsupportedOperationException("待实现");
            }
        }

        // 2. 生成钱包流水
        WalletTransactionCreateReqBO transactionCreateReqBO = new WalletTransactionCreateReqBO()
                .setWalletId(payWallet.getId()).setPrice(price).setBalance(payWallet.getBalance() + price)
                .setBizId(bizId).setBizType(bizType.getType()).setTitle(bizType.getDescription());
        return walletTransactionService.createWalletTransaction(transactionCreateReqBO);
    }

    @Override
    public void freezePrice(Long id, Integer price) {
        int updateCounts = walletMapper.freezePrice(id, price);
        if (updateCounts == 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WALLET_BALANCE_NOT_ENOUGH);
        }
    }

    @Override
    public void unfreezePrice(Long id, Integer price) {
        int updateCounts = walletMapper.unFreezePrice(id, price);
        if (updateCounts == 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.WALLET_FREEZE_PRICE_NOT_ENOUGH);
        }
    }

}
