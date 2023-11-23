package com.johnnylee.cloud.module.pay.controller.admin.transfer;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.pay.controller.admin.transfer.vo.PayTransferSubmitReqVO;
import com.johnnylee.cloud.module.pay.controller.admin.transfer.vo.PayTransferSubmitRespVO;
import com.johnnylee.cloud.module.pay.service.transfer.PayTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;
import static com.johnnylee.cloud.framework.common.util.servlet.ServletUtils.getClientIP;

@Tag(name = "管理后台 - 转账单")
@RestController
@RequestMapping("/pay/transfer")
@Validated
public class PayTransferController {

    @Resource
    private PayTransferService payTransferService;

    @PostMapping("/submit")
    @Operation(summary = "提交转账订单")
    // TODO @jason：权限的设置， 管理后台页面加的时候加一下
    public CommonResult<PayTransferSubmitRespVO> submitPayTransfer(@Valid @RequestBody PayTransferSubmitReqVO reqVO) {
        PayTransferSubmitRespVO respVO = payTransferService.submitTransfer(reqVO, getClientIP());
        return success(respVO);
    }

}
