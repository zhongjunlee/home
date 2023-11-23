package com.johnnylee.cloud.module.product.controller.app.comment;

import cn.hutool.core.collection.CollUtil;
import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.product.controller.app.comment.vo.AppCommentPageReqVO;
import com.johnnylee.cloud.module.product.controller.app.comment.vo.AppCommentStatisticsRespVO;
import com.johnnylee.cloud.module.product.controller.app.comment.vo.AppProductCommentRespVO;
import com.johnnylee.cloud.module.product.convert.comment.ProductCommentConvert;
import com.johnnylee.cloud.module.product.dal.dataobject.comment.ProductCommentDO;
import com.johnnylee.cloud.module.product.service.comment.ProductCommentService;
import com.johnnylee.cloud.module.product.service.sku.ProductSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;
import static com.johnnylee.cloud.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "用户 APP - 商品评价")
@RestController
@RequestMapping("/product/comment")
@Validated
public class AppProductCommentController {

    @Resource
    private ProductCommentService productCommentService;

    @Resource
    @Lazy
    private ProductSkuService productSkuService;

    @GetMapping("/list")
    @Operation(summary = "获得最近的 n 条商品评价")
    @Parameters({
            @Parameter(name = "spuId", description = "商品 SPU 编号", required = true, example = "1024"),
            @Parameter(name = "count", description = "数量", required = true, example = "10")
    })
    public CommonResult<List<AppProductCommentRespVO>> getCommentList(
            @RequestParam("spuId") Long spuId,
            @RequestParam(value = "count", defaultValue = "10") Integer count) {
        return success(productCommentService.getCommentList(spuId, count));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品评价分页")
    public CommonResult<PageResult<AppProductCommentRespVO>> getCommentPage(@Valid AppCommentPageReqVO pageVO) {
        // 查询评论分页
        PageResult<ProductCommentDO> commentPageResult = productCommentService.getCommentPage(pageVO, Boolean.TRUE);
        if (CollUtil.isEmpty(commentPageResult.getList())) {
            return success(PageResult.empty(commentPageResult.getTotal()));
        }

        // 拼接返回
        Set<Long> skuIds = convertSet(commentPageResult.getList(), ProductCommentDO::getSkuId);
        PageResult<AppProductCommentRespVO> commentVOPageResult = ProductCommentConvert.INSTANCE.convertPage02(
                commentPageResult, productSkuService.getSkuList(skuIds));
        return success(commentVOPageResult);
    }

    // TODO 芋艿：需要搞下
    @GetMapping("/statistics")
    @Operation(summary = "获得商品的评价统计")
    public CommonResult<AppCommentStatisticsRespVO> getCommentStatistics(@Valid @RequestParam("spuId") Long spuId) {
        return success(productCommentService.getCommentStatistics(spuId, Boolean.TRUE));
    }

}
