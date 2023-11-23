package com.johnnylee.cloud.module.trade.controller.admin.brokerage;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.member.api.user.MemberUserApi;
import com.johnnylee.cloud.module.member.api.user.dto.MemberUserRespDTO;
import com.johnnylee.cloud.module.trade.controller.admin.brokerage.vo.withdraw.BrokerageWithdrawRejectReqVO;
import com.johnnylee.cloud.module.trade.controller.admin.brokerage.vo.withdraw.BrokerageWithdrawPageReqVO;
import com.johnnylee.cloud.module.trade.controller.admin.brokerage.vo.withdraw.BrokerageWithdrawRespVO;
import com.johnnylee.cloud.module.trade.convert.brokerage.BrokerageWithdrawConvert;
import com.johnnylee.cloud.module.trade.dal.dataobject.brokerage.BrokerageWithdrawDO;
import com.johnnylee.cloud.module.trade.enums.brokerage.BrokerageWithdrawStatusEnum;
import com.johnnylee.cloud.module.trade.service.brokerage.BrokerageWithdrawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;
import static com.johnnylee.cloud.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - 佣金提现")
@RestController
@RequestMapping("/trade/brokerage-withdraw")
@Validated
public class BrokerageWithdrawController {

    @Resource
    private BrokerageWithdrawService brokerageWithdrawService;

    @Resource
    private MemberUserApi memberUserApi;

    @PutMapping("/approve")
    @Operation(summary = "通过申请")
    @PreAuthorize("@ss.hasPermission('trade:brokerage-withdraw:audit')")
    public CommonResult<Boolean> approveBrokerageWithdraw(@RequestParam("id") Integer id) {
        brokerageWithdrawService.auditBrokerageWithdraw(id, BrokerageWithdrawStatusEnum.AUDIT_SUCCESS, "");
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "驳回申请")
    @PreAuthorize("@ss.hasPermission('trade:brokerage-withdraw:audit')")
    public CommonResult<Boolean> rejectBrokerageWithdraw(@Valid @RequestBody BrokerageWithdrawRejectReqVO reqVO) {
        brokerageWithdrawService.auditBrokerageWithdraw(reqVO.getId(), BrokerageWithdrawStatusEnum.AUDIT_FAIL, reqVO.getAuditReason());
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得佣金提现")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('trade:brokerage-withdraw:query')")
    public CommonResult<BrokerageWithdrawRespVO> getBrokerageWithdraw(@RequestParam("id") Integer id) {
        BrokerageWithdrawDO brokerageWithdraw = brokerageWithdrawService.getBrokerageWithdraw(id);
        return success(BrokerageWithdrawConvert.INSTANCE.convert(brokerageWithdraw));
    }

    @GetMapping("/page")
    @Operation(summary = "获得佣金提现分页")
    @PreAuthorize("@ss.hasPermission('trade:brokerage-withdraw:query')")
    public CommonResult<PageResult<BrokerageWithdrawRespVO>> getBrokerageWithdrawPage(@Valid BrokerageWithdrawPageReqVO pageVO) {
        // 分页查询
        PageResult<BrokerageWithdrawDO> pageResult = brokerageWithdrawService.getBrokerageWithdrawPage(pageVO);

        // 拼接信息
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(
                convertSet(pageResult.getList(), BrokerageWithdrawDO::getUserId));
        return success(BrokerageWithdrawConvert.INSTANCE.convertPage(pageResult, userMap));
    }

}
