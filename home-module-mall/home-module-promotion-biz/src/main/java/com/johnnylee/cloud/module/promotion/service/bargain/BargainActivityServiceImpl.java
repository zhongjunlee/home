package com.johnnylee.cloud.module.promotion.service.bargain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.johnnylee.cloud.framework.common.enums.CommonStatusEnum;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.PageParam;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.common.util.date.LocalDateTimeUtils;
import com.johnnylee.cloud.module.product.api.sku.ProductSkuApi;
import com.johnnylee.cloud.module.product.api.sku.dto.ProductSkuRespDTO;
import com.johnnylee.cloud.module.promotion.controller.admin.bargain.vo.activity.BargainActivityCreateReqVO;
import com.johnnylee.cloud.module.promotion.controller.admin.bargain.vo.activity.BargainActivityPageReqVO;
import com.johnnylee.cloud.module.promotion.controller.admin.bargain.vo.activity.BargainActivityUpdateReqVO;
import com.johnnylee.cloud.module.promotion.convert.bargain.BargainActivityConvert;
import com.johnnylee.cloud.module.promotion.dal.dataobject.bargain.BargainActivityDO;
import com.johnnylee.cloud.module.promotion.dal.mysql.bargain.BargainActivityMapper;
import com.johnnylee.cloud.module.promotion.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.johnnylee.cloud.framework.common.util.collection.CollectionUtils.anyMatch;
import static com.johnnylee.cloud.framework.common.util.collection.CollectionUtils.convertSet;
import static com.johnnylee.cloud.module.product.enums.ErrorCodeConstants.SKU_NOT_EXISTS;

/**
 * 砍价活动 Service 实现类
 *
 * @author HUIHUI
 */
@Service
@Validated
public class BargainActivityServiceImpl implements BargainActivityService {

    @Resource
    private BargainActivityMapper bargainActivityMapper;

    @Resource
    private ProductSkuApi productSkuApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBargainActivity(BargainActivityCreateReqVO createReqVO) {
        // 校验商品 SPU 是否存在是否参加的别的活动
        validateBargainConflict(createReqVO.getSpuId(), null);
        // 校验商品 sku 是否存在
        validateSku(createReqVO.getSkuId());

        // 插入砍价活动
        BargainActivityDO activityDO = BargainActivityConvert.INSTANCE.convert(createReqVO)
                .setTotalStock(createReqVO.getStock())
                .setStatus(CommonStatusEnum.ENABLE.getStatus());
        bargainActivityMapper.insert(activityDO);
        return activityDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBargainActivity(BargainActivityUpdateReqVO updateReqVO) {
        // 校验存在
        BargainActivityDO activity = validateBargainActivityExists(updateReqVO.getId());
        // 校验状态
        if (ObjectUtil.equal(activity.getStatus(), CommonStatusEnum.DISABLE.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_STATUS_DISABLE);
        }
        // 校验商品冲突
        validateBargainConflict(updateReqVO.getSpuId(), updateReqVO.getId());
        // 校验商品 sku 是否存在
        validateSku(updateReqVO.getSkuId());

        // 更新
        BargainActivityDO updateObj = BargainActivityConvert.INSTANCE.convert(updateReqVO);
        if (updateObj.getStock() > activity.getTotalStock()) { // 如果更新的库存大于原来的库存，则更新总库存
            updateObj.setTotalStock(updateObj.getStock());
        }
        bargainActivityMapper.updateById(updateObj);
    }

    @Override
    public void updateBargainActivityStock(Long id, Integer count) {
        if (count < 0) {
            // 更新库存。如果更新失败，则抛出异常
            int updateCount = bargainActivityMapper.updateStock(id, count);
            if (updateCount == 0) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_STOCK_NOT_ENOUGH);
            }
        } else if (count > 0) {
            bargainActivityMapper.updateStock(id, count);
        }
    }

    private void validateBargainConflict(Long spuId, Long activityId) {
        // 查询所有开启的砍价活动
        List<BargainActivityDO> activityList = bargainActivityMapper.selectListByStatus(CommonStatusEnum.ENABLE.getStatus());
        if (activityId != null) { // 更新时排除自己
            activityList.removeIf(item -> ObjectUtil.equal(item.getId(), activityId));
        }
        // 校验商品 spu 是否参加了其它活动
        if (anyMatch(activityList, activity -> ObjectUtil.equal(activity.getSpuId(), spuId))) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_SPU_CONFLICTS);
        }
    }

    private void validateSku(Long skuId) {
        ProductSkuRespDTO sku = productSkuApi.getSku(skuId).getCheckedData();
        if (sku == null) {
            throw exception(SKU_NOT_EXISTS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBargainActivity(Long id) {
        // 校验存在
        BargainActivityDO activityDO = validateBargainActivityExists(id);
        // 校验状态
        if (ObjectUtil.equal(activityDO.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_DELETE_FAIL_STATUS_NOT_CLOSED_OR_END);
        }

        // 删除
        bargainActivityMapper.deleteById(id);
    }

    private BargainActivityDO validateBargainActivityExists(Long id) {
        BargainActivityDO activityDO = bargainActivityMapper.selectById(id);
        if (activityDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_NOT_EXISTS);
        }
        return activityDO;
    }

    @Override
    public BargainActivityDO getBargainActivity(Long id) {
        return bargainActivityMapper.selectById(id);
    }

    @Override
    public List<BargainActivityDO> getBargainActivityList(Set<Long> ids) {
        return bargainActivityMapper.selectBatchIds(ids);
    }

    @Override
    public BargainActivityDO validateBargainActivityCanJoin(Long id) {
        BargainActivityDO activity = bargainActivityMapper.selectById(id);
        if (activity == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_NOT_EXISTS);
        }
        if (ObjUtil.notEqual(activity.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_STATUS_CLOSED);
        }
        if (activity.getStock() <= 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_STOCK_NOT_ENOUGH);
        }
        if (!LocalDateTimeUtils.isBetween(activity.getStartTime(), activity.getEndTime())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.BARGAIN_ACTIVITY_TIME_END);
        }
        return activity;
    }

    @Override
    public PageResult<BargainActivityDO> getBargainActivityPage(BargainActivityPageReqVO pageReqVO) {
        return bargainActivityMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<BargainActivityDO> getBargainActivityPage(PageParam pageReqVO) {
        // 只查询进行中，且在时间范围内的
        return bargainActivityMapper.selectPage(pageReqVO, CommonStatusEnum.ENABLE.getStatus(), LocalDateTime.now());
    }

    @Override
    public List<BargainActivityDO> getBargainActivityListByCount(Integer count) {
        return bargainActivityMapper.selectList(count, CommonStatusEnum.ENABLE.getStatus(), LocalDateTime.now());
    }

    @Override
    public List<BargainActivityDO> getBargainActivityBySpuIdsAndStatusAndDateTimeLt(Collection<Long> spuIds, Integer status, LocalDateTime dateTime) {
        // 1. 查询出指定 spuId 的 spu 参加的活动最接近现在的一条记录。多个的话，一个 spuId 对应一个最近的活动编号
        List<Map<String, Object>> spuIdAndActivityIdMaps = bargainActivityMapper.selectSpuIdAndActivityIdMapsBySpuIdsAndStatus(spuIds, status);
        if (CollUtil.isEmpty(spuIdAndActivityIdMaps)) {
            return Collections.emptyList();
        }
        // 2. 查询活动详情
        return bargainActivityMapper.selectListByIdsAndDateTimeLt(
                convertSet(spuIdAndActivityIdMaps, map -> MapUtil.getLong(map, "activityId")), dateTime);
    }

}
