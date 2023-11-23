package com.johnnylee.cloud.module.pay.controller.app.wallet;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.pay.controller.app.wallet.vo.recharge.AppPayWalletRechargeCreateReqVO;
import com.johnnylee.cloud.module.pay.controller.app.wallet.vo.recharge.AppPayWalletRechargeCreateRespVO;
import com.johnnylee.cloud.module.pay.convert.wallet.PayWalletRechargeConvert;
import com.johnnylee.cloud.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import com.johnnylee.cloud.module.pay.service.wallet.PayWalletRechargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;
import static com.johnnylee.cloud.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.johnnylee.cloud.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.johnnylee.cloud.framework.web.core.util.WebFrameworkUtils.getLoginUserType;

@Tag(name = "用户 APP - 钱包充值")
@RestController
@RequestMapping("/pay/wallet-recharge")
@Validated
@Slf4j
public class AppPayWalletRechargeController {

    @Resource
    private PayWalletRechargeService walletRechargeService;

    @PostMapping("/create")
    @Operation(summary = "创建钱包充值记录（发起充值）")
    public CommonResult<AppPayWalletRechargeCreateRespVO> createWalletRecharge(
            @Valid  @RequestBody  AppPayWalletRechargeCreateReqVO reqVO) {
        PayWalletRechargeDO walletRecharge = walletRechargeService.createWalletRecharge(
                getLoginUserId(), getLoginUserType(), getClientIP(), reqVO);
        return success(PayWalletRechargeConvert.INSTANCE.convert(walletRecharge));
    }

}
