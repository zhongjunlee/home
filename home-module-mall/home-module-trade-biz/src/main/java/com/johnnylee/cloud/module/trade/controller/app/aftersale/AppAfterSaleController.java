package com.johnnylee.cloud.module.trade.controller.app.aftersale;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.framework.common.pojo.PageParam;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.trade.controller.app.aftersale.vo.AppAfterSaleCreateReqVO;
import com.johnnylee.cloud.module.trade.controller.app.aftersale.vo.AppAfterSaleDeliveryReqVO;
import com.johnnylee.cloud.module.trade.controller.app.aftersale.vo.AppAfterSaleRespVO;
import com.johnnylee.cloud.module.trade.convert.aftersale.AfterSaleConvert;
import com.johnnylee.cloud.module.trade.service.aftersale.AfterSaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;
import static com.johnnylee.cloud.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 App - 交易售后")
@RestController
@RequestMapping("/trade/after-sale")
@Validated
@Slf4j
public class AppAfterSaleController {

    @Resource
    private AfterSaleService afterSaleService;

    @GetMapping(value = "/page")
    @Operation(summary = "获得售后分页")
    public CommonResult<PageResult<AppAfterSaleRespVO>> getAfterSalePage(PageParam pageParam) {
        return success(AfterSaleConvert.INSTANCE.convertPage02(
                afterSaleService.getAfterSalePage(getLoginUserId(), pageParam)));
    }

    @GetMapping(value = "/get")
    @Operation(summary = "获得售后订单")
    @Parameter(name = "id", description = "售后编号", required = true, example = "1")
    public CommonResult<AppAfterSaleRespVO> getAfterSale(@RequestParam("id") Long id) {
        return success(AfterSaleConvert.INSTANCE.convert(afterSaleService.getAfterSale(getLoginUserId(), id)));
    }

    @GetMapping(value = "/get-applying-count")
    @Operation(summary = "获得进行中的售后订单数量")
    public CommonResult<Long> getApplyingAfterSaleCount() {
        return success(afterSaleService.getApplyingAfterSaleCount(getLoginUserId()));
    }

    @PostMapping(value = "/create")
    @Operation(summary = "申请售后")
    public CommonResult<Long> createAfterSale(@RequestBody AppAfterSaleCreateReqVO createReqVO) {
        return success(afterSaleService.createAfterSale(getLoginUserId(), createReqVO));
    }

    @PutMapping(value = "/delivery")
    @Operation(summary = "退回货物")
    public CommonResult<Boolean> deliveryAfterSale(@RequestBody AppAfterSaleDeliveryReqVO deliveryReqVO) {
        afterSaleService.deliveryAfterSale(getLoginUserId(), deliveryReqVO);
        return success(true);
    }

    @DeleteMapping(value = "/cancel")
    @Operation(summary = "取消售后")
    @Parameter(name = "id", description = "售后编号", required = true, example = "1")
    public CommonResult<Boolean> cancelAfterSale(@RequestParam("id") Long id) {
        afterSaleService.cancelAfterSale(getLoginUserId(), id);
        return success(true);
    }

}
