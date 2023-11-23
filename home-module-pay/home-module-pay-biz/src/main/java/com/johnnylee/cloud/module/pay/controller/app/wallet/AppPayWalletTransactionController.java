package com.johnnylee.cloud.module.pay.controller.app.wallet;

import com.johnnylee.cloud.framework.common.enums.UserTypeEnum;
import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import com.johnnylee.cloud.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionRespVO;
import com.johnnylee.cloud.module.pay.convert.wallet.PayWalletTransactionConvert;
import com.johnnylee.cloud.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import com.johnnylee.cloud.module.pay.service.wallet.PayWalletTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;
import static com.johnnylee.cloud.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 钱包余额明细")
@RestController
@RequestMapping("/pay/wallet-transaction")
@Validated
@Slf4j
public class AppPayWalletTransactionController {

    @Resource
    private PayWalletTransactionService payWalletTransactionService;

    @GetMapping("/page")
    @Operation(summary = "获得钱包流水分页")
    public CommonResult<PageResult<AppPayWalletTransactionRespVO>> getWalletTransactionPage(
            @Valid AppPayWalletTransactionPageReqVO pageReqVO) {
        if (true) {
            PageResult<AppPayWalletTransactionRespVO> result = new PageResult<>(10L);
            result.getList().add(new AppPayWalletTransactionRespVO().setPrice(1L)
                    .setTitle("测试").setCreateTime(LocalDateTime.now()));
            result.getList().add(new AppPayWalletTransactionRespVO().setPrice(-1L)
                    .setTitle("测试2").setCreateTime(LocalDateTime.now()));
            return success(result);
        }
        PageResult<PayWalletTransactionDO> result = payWalletTransactionService.getWalletTransactionPage(getLoginUserId(),
                UserTypeEnum.MEMBER.getValue(), pageReqVO);
        return success(PayWalletTransactionConvert.INSTANCE.convertPage(result));
    }
}
