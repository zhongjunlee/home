package com.johnnylee.cloud.module.product.service.spu;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.johnnylee.cloud.framework.common.enums.CommonStatusEnum;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.common.util.collection.CollectionUtils;
import com.johnnylee.cloud.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import com.johnnylee.cloud.module.product.controller.admin.sku.vo.ProductSkuCreateOrUpdateReqVO;
import com.johnnylee.cloud.module.product.controller.admin.spu.vo.*;
import com.johnnylee.cloud.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import com.johnnylee.cloud.module.product.convert.spu.ProductSpuConvert;
import com.johnnylee.cloud.module.product.dal.dataobject.category.ProductCategoryDO;
import com.johnnylee.cloud.module.product.dal.dataobject.spu.ProductSpuDO;
import com.johnnylee.cloud.module.product.dal.mysql.spu.ProductSpuMapper;
import com.johnnylee.cloud.module.product.enums.spu.ProductSpuStatusEnum;
import com.johnnylee.cloud.module.product.service.brand.ProductBrandService;
import com.johnnylee.cloud.module.product.service.category.ProductCategoryService;
import com.johnnylee.cloud.module.product.service.sku.ProductSkuService;
import com.google.common.collect.Maps;
import com.johnnylee.cloud.module.product.controller.admin.spu.vo.*;
import com.johnnylee.cloud.module.product.enums.ErrorCodeConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static com.johnnylee.cloud.framework.common.util.collection.CollectionUtils.*;

/**
 * 商品 SPU Service 实现类
 *
 * @author Johnny
 */
@Service
@Validated
public class ProductSpuServiceImpl implements ProductSpuService {

    @Resource
    private ProductSpuMapper productSpuMapper;

    @Resource
    @Lazy // 循环依赖，避免报错
    private ProductSkuService productSkuService;
    @Resource
    private ProductBrandService brandService;
    @Resource
    private ProductCategoryService categoryService;

//    @Resource
//    @Lazy
//    private CouponTemplateApi couponTemplateApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSpu(ProductSpuCreateReqVO createReqVO) {
        // 校验分类、品牌
        validateCategory(createReqVO.getCategoryId());
        brandService.validateProductBrand(createReqVO.getBrandId());
        // 校验优惠券
        Set<Long> giveCouponTemplateIds = convertSet(createReqVO.getGiveCouponTemplates(), ProductSpuCreateReqVO.GiveCouponTemplate::getId);
//        validateCouponTemplate(giveCouponTemplateIds);
        // 校验 SKU
        List<ProductSkuCreateOrUpdateReqVO> skuSaveReqList = createReqVO.getSkus();
        productSkuService.validateSkuList(skuSaveReqList, createReqVO.getSpecType());

        ProductSpuDO spu = ProductSpuConvert.INSTANCE.convert(createReqVO);
        // 初始化 SPU 中 SKU 相关属性
        initSpuFromSkus(spu, skuSaveReqList);
        // 设置优惠券
        spu.setGiveCouponTemplateIds(CollUtil.newArrayList(giveCouponTemplateIds));
        // 插入 SPU
        productSpuMapper.insert(spu);
        // 插入 SKU
        productSkuService.createSkuList(spu.getId(), skuSaveReqList);
        // 返回
        return spu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpu(ProductSpuUpdateReqVO updateReqVO) {
        // 校验 SPU 是否存在
        validateSpuExists(updateReqVO.getId());
        // 校验分类、品牌
        validateCategory(updateReqVO.getCategoryId());
        brandService.validateProductBrand(updateReqVO.getBrandId());
        // 校验优惠券
        Set<Long> giveCouponTemplateIds = convertSet(updateReqVO.getGiveCouponTemplates(), ProductSpuUpdateReqVO.GiveCouponTemplate::getId);
//        validateCouponTemplate(giveCouponTemplateIds);
        // 校验SKU
        List<ProductSkuCreateOrUpdateReqVO> skuSaveReqList = updateReqVO.getSkus();
        productSkuService.validateSkuList(skuSaveReqList, updateReqVO.getSpecType());

        // 更新 SPU
        ProductSpuDO updateObj = ProductSpuConvert.INSTANCE.convert(updateReqVO);
        initSpuFromSkus(updateObj, skuSaveReqList);
        // 设置优惠券
        updateObj.setGiveCouponTemplateIds(CollUtil.newArrayList(giveCouponTemplateIds));
        productSpuMapper.updateById(updateObj);
        // 批量更新 SKU
        productSkuService.updateSkuList(updateObj.getId(), updateReqVO.getSkus());
    }

    /**
     * 基于 SKU 的信息，初始化 SPU 的信息
     * 主要是计数相关的字段，例如说市场价、最大最小价、库存等等
     *
     * @param spu  商品 SPU
     * @param skus 商品 SKU 数组
     */
    private void initSpuFromSkus(ProductSpuDO spu, List<ProductSkuCreateOrUpdateReqVO> skus) {
        // sku 单价最低的商品的价格
        spu.setPrice(getMinValue(skus, ProductSkuCreateOrUpdateReqVO::getPrice));
        // sku 单价最低的商品的市场价格
        spu.setMarketPrice(getMinValue(skus, ProductSkuCreateOrUpdateReqVO::getMarketPrice));
        // sku 单价最低的商品的成本价格
        spu.setCostPrice(getMinValue(skus, ProductSkuCreateOrUpdateReqVO::getCostPrice));
        // sku 单价最低的商品的条形码 TODO 芋艿：条形码字段，是不是可以删除
        spu.setBarCode("");
//        spu.setBarCode(getMinValue(skus, ProductSkuCreateOrUpdateReqVO::getBarCode));
        // skus 库存总数
        spu.setStock(getSumValue(skus, ProductSkuCreateOrUpdateReqVO::getStock, Integer::sum));
        // 若是 spu 已有状态则不处理
        if (spu.getStatus() == null) {
            // 默认状态为上架
            spu.setStatus(ProductSpuStatusEnum.ENABLE.getStatus());
            // 默认商品销量
            spu.setSalesCount(0);
            // 默认商品浏览量
            spu.setBrowseCount(0);
        }
        // 如果活动顺序为空则默认初始化
        // TODO 芋艿：后续再优化
//        if (CollUtil.isEmpty(spu.getActivityOrders())) {
//            spu.setActivityOrders(Arrays.stream(PromotionTypeEnum.ARRAYS).boxed().collect(Collectors.toList()));
//        }
    }

    /**
     * 校验商品分类是否合法
     *
     * @param id 商品分类编号
     */
    private void validateCategory(Long id) {
        categoryService.validateCategory(id);
        // 校验层级
        if (categoryService.getCategoryLevel(id) < ProductCategoryDO.CATEGORY_LEVEL) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_SAVE_FAIL_CATEGORY_LEVEL_ERROR);
        }
    }

//    private void validateCouponTemplate(Collection<Long> ids) {
//        List<CouponTemplateRespDTO> couponTemplateList = couponTemplateApi.getCouponTemplateListByIds(ids);
//        if (couponTemplateList.size() != ids.size()) {
//            throw exception(SPU_SAVE_FAIL_COUPON_TEMPLATE_NOT_EXISTS);
//        }
//    }

    @Override
    public List<ProductSpuDO> validateSpuList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 获得商品信息
        List<ProductSpuDO> list = productSpuMapper.selectBatchIds(ids);
        Map<Long, ProductSpuDO> spuMap = CollectionUtils.convertMap(list, ProductSpuDO::getId);
        // 校验
        ids.forEach(id -> {
            ProductSpuDO spu = spuMap.get(id);
            if (spu == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_NOT_EXISTS);
            }
            if (!ProductSpuStatusEnum.isEnable(spu.getStatus())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_NOT_ENABLE, spu.getName());
            }
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpu(Long id) {
        // 校验存在
        validateSpuExists(id);
        // 校验商品状态不是回收站不能删除
        ProductSpuDO spuDO = productSpuMapper.selectById(id);
        // 判断 SPU 状态是否为回收站
        if (ObjectUtil.notEqual(spuDO.getStatus(), ProductSpuStatusEnum.RECYCLE.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_NOT_RECYCLE);
        }

        // 删除 SPU
        productSpuMapper.deleteById(id);
        // 删除关联的 SKU
        productSkuService.deleteSkuBySpuId(id);
    }

    private void validateSpuExists(Long id) {
        if (productSpuMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_NOT_EXISTS);
        }
    }

    @Override
    public ProductSpuDO getSpu(Long id) {
        return productSpuMapper.selectById(id);
    }

    @Override
    public List<ProductSpuDO> getSpuList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return productSpuMapper.selectBatchIds(ids);
    }

    @Override
    public List<ProductSpuDO> getSpuListByStatus(Integer status) {
        return productSpuMapper.selectList(ProductSpuDO::getStatus, status);
    }

    @Override
    public List<ProductSpuDO> getSpuList(ProductSpuExportReqVO reqVO) {
        return productSpuMapper.selectList(reqVO);
    }

    @Override
    public PageResult<ProductSpuDO> getSpuPage(ProductSpuPageReqVO pageReqVO) {
        return productSpuMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<ProductSpuDO> getSpuPage(AppProductSpuPageReqVO pageReqVO) {
        // 查找时，如果查找某个分类编号，则包含它的子分类。因为顶级分类不包含商品
        Set<Long> categoryIds = new HashSet<>();
        if (pageReqVO.getCategoryId() != null && pageReqVO.getCategoryId() > 0) {
            categoryIds.add(pageReqVO.getCategoryId());
            List<ProductCategoryDO> categoryChildren = categoryService.getEnableCategoryList(new ProductCategoryListReqVO()
                    .setParentId(pageReqVO.getCategoryId()).setStatus(CommonStatusEnum.ENABLE.getStatus()));
            categoryIds.addAll(CollectionUtils.convertList(categoryChildren, ProductCategoryDO::getId));
        }
        // 分页查询
        return productSpuMapper.selectPage(pageReqVO, categoryIds);
    }

    @Override
    public List<ProductSpuDO> getSpuList(String recommendType, Integer count) {
        return productSpuMapper.selectListByRecommendType(recommendType, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpuStock(Map<Long, Integer> stockIncrCounts) {
        stockIncrCounts.forEach((id, incCount) -> productSpuMapper.updateStock(id, incCount));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpuStatus(ProductSpuUpdateStatusReqVO updateReqVO) {
        // 校验存在
        validateSpuExists(updateReqVO.getId());

        // 更新状态
        ProductSpuDO productSpuDO = productSpuMapper.selectById(updateReqVO.getId()).setStatus(updateReqVO.getStatus());
        productSpuMapper.updateById(productSpuDO);
    }

    @Override
    public Map<Integer, Long> getTabsCount() {
        Map<Integer, Long> counts = Maps.newLinkedHashMapWithExpectedSize(5);
        // 查询销售中的商品数量
        counts.put(ProductSpuPageReqVO.FOR_SALE,
                productSpuMapper.selectCount(ProductSpuDO::getStatus, ProductSpuStatusEnum.ENABLE.getStatus()));
        // 查询仓库中的商品数量
        counts.put(ProductSpuPageReqVO.IN_WAREHOUSE,
                productSpuMapper.selectCount(ProductSpuDO::getStatus, ProductSpuStatusEnum.DISABLE.getStatus()));
        // 查询售空的商品数量
        counts.put(ProductSpuPageReqVO.SOLD_OUT,
                productSpuMapper.selectCount(ProductSpuDO::getStock, 0));
        // 查询触发警戒库存的商品数量
        counts.put(ProductSpuPageReqVO.ALERT_STOCK,
                productSpuMapper.selectCount());
        // 查询回收站中的商品数量
        counts.put(ProductSpuPageReqVO.RECYCLE_BIN,
                productSpuMapper.selectCount(ProductSpuDO::getStatus, ProductSpuStatusEnum.RECYCLE.getStatus()));
        return counts;
    }

    @Override
    public Long getSpuCountByCategoryId(Long categoryId) {
        return productSpuMapper.selectCount(ProductSpuDO::getCategoryId, categoryId);
    }

}
