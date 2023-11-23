package com.johnnylee.cloud.module.promotion.convert.bargain;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.common.util.collection.CollectionUtils;
import com.johnnylee.cloud.framework.dict.core.util.DictFrameworkUtils;
import com.johnnylee.cloud.module.product.api.spu.dto.ProductSpuRespDTO;
import com.johnnylee.cloud.module.product.enums.DictTypeConstants;
import com.johnnylee.cloud.module.promotion.controller.admin.bargain.vo.activity.BargainActivityBaseVO;
import com.johnnylee.cloud.module.promotion.controller.admin.bargain.vo.activity.BargainActivityPageItemRespVO;
import com.johnnylee.cloud.module.promotion.controller.admin.bargain.vo.activity.BargainActivityRespVO;
import com.johnnylee.cloud.module.promotion.controller.admin.bargain.vo.activity.BargainActivityUpdateReqVO;
import com.johnnylee.cloud.module.promotion.controller.app.bargain.vo.activity.AppBargainActivityDetailRespVO;
import com.johnnylee.cloud.module.promotion.controller.app.bargain.vo.activity.AppBargainActivityRespVO;
import com.johnnylee.cloud.module.promotion.dal.dataobject.bargain.BargainActivityDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static com.johnnylee.cloud.framework.common.util.collection.CollectionUtils.convertMap;
import static com.johnnylee.cloud.framework.common.util.collection.MapUtils.findAndThen;

/**
 * 拼团活动 Convert
 *
 * @author HUIHUI
 */
@Mapper
public interface BargainActivityConvert {

    BargainActivityConvert INSTANCE = Mappers.getMapper(BargainActivityConvert.class);

    BargainActivityDO convert(BargainActivityBaseVO bean);

    BargainActivityDO convert(BargainActivityUpdateReqVO bean);

    BargainActivityRespVO convert(BargainActivityDO bean);

    List<BargainActivityRespVO> convertList(List<BargainActivityDO> list);

    PageResult<BargainActivityPageItemRespVO> convertPage(PageResult<BargainActivityDO> page);

    default PageResult<BargainActivityPageItemRespVO> convertPage(PageResult<BargainActivityDO> page, List<ProductSpuRespDTO> spuList,
                                                                  Map<Long, Integer> recordUserCountMap, Map<Long, Integer> recordSuccessUserCountMap,
                                                                  Map<Long, Integer> helpUserCountMap) {
        PageResult<BargainActivityPageItemRespVO> result = convertPage(page);
        // 拼接关联属性
        Map<Long, ProductSpuRespDTO> spuMap = convertMap(spuList, ProductSpuRespDTO::getId);
        result.getList().forEach(item -> {
            findAndThen(spuMap, item.getSpuId(), spu -> {
                item.setPicUrl(spu.getPicUrl()).setSpuName(spu.getName());
            });
            // 设置统计字段
            item.setRecordUserCount(recordUserCountMap.getOrDefault(item.getId(), 0))
                    .setRecordSuccessUserCount(recordSuccessUserCountMap.getOrDefault(item.getId(), 0))
                    .setHelpUserCount(helpUserCountMap.getOrDefault(item.getId(), 0));
        });
        return result;
    }

    AppBargainActivityDetailRespVO convert1(BargainActivityDO bean);

    default AppBargainActivityDetailRespVO convert(BargainActivityDO bean, Integer successUserCount, ProductSpuRespDTO spu) {
        AppBargainActivityDetailRespVO detail = convert1(bean).setSuccessUserCount(successUserCount);
        if (spu != null) {
            detail.setPicUrl(spu.getPicUrl()).setMarketPrice(spu.getMarketPrice())
                    .setUnitName(DictFrameworkUtils.getDictDataLabel(DictTypeConstants.PRODUCT_UNIT, spu.getUnit()));
        }
        return detail;
    }

    PageResult<AppBargainActivityRespVO> convertAppPage(PageResult<BargainActivityDO> page);

    default PageResult<AppBargainActivityRespVO> convertAppPage(PageResult<BargainActivityDO> page, List<ProductSpuRespDTO> spuList) {
        PageResult<AppBargainActivityRespVO> result = convertAppPage(page);
        // 拼接关联属性
        Map<Long, ProductSpuRespDTO> spuMap = convertMap(spuList, ProductSpuRespDTO::getId);
        List<AppBargainActivityRespVO> list = CollectionUtils.convertList(result.getList(), item -> {
            findAndThen(spuMap, item.getSpuId(), spu -> item.setPicUrl(spu.getPicUrl()).setMarketPrice(spu.getMarketPrice()));
            return item;
        });
        result.setList(list);
        return result;
    }

    List<AppBargainActivityRespVO> convertAppList(List<BargainActivityDO> list);

    default List<AppBargainActivityRespVO> convertAppList(List<BargainActivityDO> list, List<ProductSpuRespDTO> spuList) {
        List<AppBargainActivityRespVO> activityList = convertAppList(list);
        Map<Long, ProductSpuRespDTO> spuMap = convertMap(spuList, ProductSpuRespDTO::getId);
        return CollectionUtils.convertList(activityList, item -> {
            findAndThen(spuMap, item.getSpuId(), spu -> item.setPicUrl(spu.getPicUrl()).setMarketPrice(spu.getMarketPrice()));
            return item;
        });
    }

}
