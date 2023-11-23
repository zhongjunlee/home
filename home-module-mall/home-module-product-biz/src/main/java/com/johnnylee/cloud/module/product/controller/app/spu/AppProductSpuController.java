package com.johnnylee.cloud.module.product.controller.app.spu;

import cn.hutool.core.collection.CollUtil;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.member.api.level.MemberLevelApi;
import com.johnnylee.cloud.module.member.api.level.dto.MemberLevelRespDTO;
import com.johnnylee.cloud.module.member.api.user.MemberUserApi;
import com.johnnylee.cloud.module.member.api.user.dto.MemberUserRespDTO;
import com.johnnylee.cloud.module.product.controller.app.spu.vo.AppProductSpuDetailRespVO;
import com.johnnylee.cloud.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import com.johnnylee.cloud.module.product.controller.app.spu.vo.AppProductSpuPageRespVO;
import com.johnnylee.cloud.module.product.convert.spu.ProductSpuConvert;
import com.johnnylee.cloud.module.product.dal.dataobject.sku.ProductSkuDO;
import com.johnnylee.cloud.module.product.dal.dataobject.spu.ProductSpuDO;
import com.johnnylee.cloud.module.product.enums.spu.ProductSpuStatusEnum;
import com.johnnylee.cloud.module.product.service.sku.ProductSkuService;
import com.johnnylee.cloud.module.product.service.spu.ProductSpuService;
import com.johnnylee.cloud.module.product.enums.ErrorCodeConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;
import static com.johnnylee.cloud.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 商品 SPU")
@RestController
@RequestMapping("/product/spu")
@Validated
public class AppProductSpuController {

    @Resource
    private ProductSpuService productSpuService;
    @Resource
    private ProductSkuService productSkuService;

    @Resource
    private MemberLevelApi memberLevelApi;
    @Resource
    private MemberUserApi memberUserApi;

    @GetMapping("/list")
    @Operation(summary = "获得商品 SPU 列表")
    @Parameters({
            @Parameter(name = "recommendType", description = "推荐类型", required = true), // 参见 AppProductSpuPageReqVO.RECOMMEND_TYPE_XXX 常量
            @Parameter(name = "count", description = "数量", required = true)
    })
    public CommonResult<List<AppProductSpuPageRespVO>> getSpuList(
            @RequestParam("recommendType") String recommendType,
            @RequestParam(value = "count", defaultValue = "10") Integer count) {
        List<ProductSpuDO> list = productSpuService.getSpuList(recommendType, count);
        if (CollUtil.isEmpty(list)) {
            return success(Collections.emptyList());
        }

        // 拼接返回
        List<AppProductSpuPageRespVO> voList = ProductSpuConvert.INSTANCE.convertListForGetSpuList(list);
        // 处理 vip 价格
        MemberLevelRespDTO memberLevel = getMemberLevel();
        voList.forEach(vo -> vo.setVipPrice(calculateVipPrice(vo.getPrice(), memberLevel)));
        return success(voList);
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品 SPU 分页")
    public CommonResult<PageResult<AppProductSpuPageRespVO>> getSpuPage(@Valid AppProductSpuPageReqVO pageVO) {
        PageResult<ProductSpuDO> pageResult = productSpuService.getSpuPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty(pageResult.getTotal()));
        }

        // 拼接返回
        PageResult<AppProductSpuPageRespVO> voPageResult = ProductSpuConvert.INSTANCE.convertPageForGetSpuPage(pageResult);
        // 处理 vip 价格
        MemberLevelRespDTO memberLevel = getMemberLevel();
        voPageResult.getList().forEach(vo -> vo.setVipPrice(calculateVipPrice(vo.getPrice(), memberLevel)));
        return success(voPageResult);
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得商品 SPU 明细")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<AppProductSpuDetailRespVO> getSpuDetail(@RequestParam("id") Long id) {
        // 获得商品 SPU
        ProductSpuDO spu = productSpuService.getSpu(id);
        if (spu == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_NOT_EXISTS);
        }
        if (!ProductSpuStatusEnum.isEnable(spu.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_NOT_ENABLE);
        }

        // 拼接返回
        List<ProductSkuDO> skus = productSkuService.getSkuListBySpuId(spu.getId());
        AppProductSpuDetailRespVO detailVO = ProductSpuConvert.INSTANCE.convertForGetSpuDetail(spu, skus);
        // 处理 vip 价格
        MemberLevelRespDTO memberLevel = getMemberLevel();
        detailVO.setVipPrice(calculateVipPrice(detailVO.getPrice(), memberLevel));
        return success(detailVO);
    }

    private MemberLevelRespDTO getMemberLevel() {
        Long userId = getLoginUserId();
        if (userId == null) {
            return null;
        }
        MemberUserRespDTO user = memberUserApi.getUser(userId).getCheckedData();
        if (user.getLevelId() == null || user.getLevelId() <= 0) {
            return null;
        }
        return memberLevelApi.getMemberLevel(user.getLevelId()).getCheckedData();
    }

    /**
     * 计算会员 VIP 优惠价格
     *
     * @param price 原价
     * @param memberLevel 会员等级
     * @return 优惠价格
     */
    public Integer calculateVipPrice(Integer price, MemberLevelRespDTO memberLevel) {
        if (memberLevel == null || memberLevel.getDiscountPercent() == null) {
            return 0;
        }
        Integer newPrice = price * memberLevel.getDiscountPercent() / 100;
        return price - newPrice;
    }

    // TODO 芋艿：商品的浏览记录；
}
