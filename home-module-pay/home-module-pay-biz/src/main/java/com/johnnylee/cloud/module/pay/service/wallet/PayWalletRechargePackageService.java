package com.johnnylee.cloud.module.pay.service.wallet;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.pay.controller.admin.wallet.vo.rechargepackage.WalletRechargePackageCreateReqVO;
import com.johnnylee.cloud.module.pay.controller.admin.wallet.vo.rechargepackage.WalletRechargePackagePageReqVO;
import com.johnnylee.cloud.module.pay.controller.admin.wallet.vo.rechargepackage.WalletRechargePackageUpdateReqVO;
import com.johnnylee.cloud.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;

import javax.validation.Valid;

/**
 * 钱包充值套餐 Service 接口
 *
 * @author jason
 */
public interface PayWalletRechargePackageService {

    /**
     * 获取钱包充值套餐
     * @param packageId 充值套餐编号
     */
    PayWalletRechargePackageDO getWalletRechargePackage(Long packageId);

    /**
     * 校验钱包充值套餐的有效性, 无效的话抛出 ServiceException 异常
     *
     * @param packageId 充值套餐编号
     */
    PayWalletRechargePackageDO validWalletRechargePackage(Long packageId);

    /**
     * 创建充值套餐
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWalletRechargePackage(@Valid WalletRechargePackageCreateReqVO createReqVO);

    /**
     * 更新充值套餐
     *
     * @param updateReqVO 更新信息
     */
    void updateWalletRechargePackage(@Valid WalletRechargePackageUpdateReqVO updateReqVO);

    /**
     * 删除充值套餐
     *
     * @param id 编号
     */
    void deleteWalletRechargePackage(Long id);

    /**
     * 获得充值套餐分页
     *
     * @param pageReqVO 分页查询
     * @return 充值套餐分页
     */
    PageResult<PayWalletRechargePackageDO> getWalletRechargePackagePage(WalletRechargePackagePageReqVO pageReqVO);

}
