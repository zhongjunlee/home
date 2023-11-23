package com.johnnylee.cloud.module.promotion.convert.bargain;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.common.util.collection.CollectionUtils;
import com.johnnylee.cloud.framework.common.util.collection.MapUtils;
import com.johnnylee.cloud.module.member.api.user.dto.MemberUserRespDTO;
import com.johnnylee.cloud.module.product.api.spu.dto.ProductSpuRespDTO;
import com.johnnylee.cloud.module.promotion.controller.admin.bargain.vo.recrod.BargainRecordPageItemRespVO;
import com.johnnylee.cloud.module.promotion.controller.app.bargain.vo.record.AppBargainRecordDetailRespVO;
import com.johnnylee.cloud.module.promotion.controller.app.bargain.vo.record.AppBargainRecordRespVO;
import com.johnnylee.cloud.module.promotion.controller.app.bargain.vo.record.AppBargainRecordSummaryRespVO;
import com.johnnylee.cloud.module.promotion.dal.dataobject.bargain.BargainActivityDO;
import com.johnnylee.cloud.module.promotion.dal.dataobject.bargain.BargainRecordDO;
import com.johnnylee.cloud.module.trade.api.order.dto.TradeOrderRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static com.johnnylee.cloud.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 砍价记录 Convert
 *
 * @author Johnny
 */
@Mapper
public interface BargainRecordConvert {

    BargainRecordConvert INSTANCE = Mappers.getMapper(BargainRecordConvert.class);

    default PageResult<BargainRecordPageItemRespVO> convertPage(PageResult<BargainRecordDO> page,
                                                                Map<Long, Integer> helpCountMap,
                                                                List<BargainActivityDO> activityList,
                                                                Map<Long, MemberUserRespDTO> userMap) {
        PageResult<BargainRecordPageItemRespVO> pageResult = convertPage(page);
        // 拼接数据
        Map<Long, BargainActivityDO> activityMap = convertMap(activityList, BargainActivityDO::getId);
        pageResult.getList().forEach(record -> {
            MapUtils.findAndThen(userMap, record.getUserId(),
                    user -> record.setNickname(user.getNickname()).setAvatar(user.getAvatar()));
            record.setActivity(BargainActivityConvert.INSTANCE.convert(activityMap.get(record.getActivityId())))
                    .setHelpCount(helpCountMap.getOrDefault(record.getId(), 0));
        });
        return pageResult;
    }
    PageResult<BargainRecordPageItemRespVO> convertPage(PageResult<BargainRecordDO> page);

    default PageResult<AppBargainRecordRespVO> convertPage02(PageResult<BargainRecordDO> page,
                                                             List<BargainActivityDO> activityList,
                                                             List<ProductSpuRespDTO> spuList,
                                                             List<TradeOrderRespDTO> orderList) {
        PageResult<AppBargainRecordRespVO> pageResult = convertPage02(page);
        // 拼接数据
        Map<Long, BargainActivityDO> activityMap = convertMap(activityList, BargainActivityDO::getId);
        Map<Long, ProductSpuRespDTO> spuMap = convertMap(spuList, ProductSpuRespDTO::getId);
        Map<Long, TradeOrderRespDTO> orderMap = convertMap(orderList, TradeOrderRespDTO::getId);
        pageResult.getList().forEach(record -> {
            MapUtils.findAndThen(activityMap, record.getActivityId(),
                    activity -> record.setActivityName(activity.getName()).setEndTime(activity.getEndTime()));
            MapUtils.findAndThen(spuMap, record.getSpuId(),
                    spu -> record.setPicUrl(record.getPicUrl()));
            MapUtils.findAndThen(orderMap, record.getOrderId(),
                    order -> record.setPayStatus(order.getPayStatus()).setPayOrderId(order.getPayOrderId()));
        });
        return pageResult;
    }
    PageResult<AppBargainRecordRespVO> convertPage02(PageResult<BargainRecordDO> page);

    default AppBargainRecordSummaryRespVO convert(Integer successUserCount, List<BargainRecordDO> successList,
                                                  List<BargainActivityDO> activityList, Map<Long, MemberUserRespDTO> userMap) {
        AppBargainRecordSummaryRespVO summary = new AppBargainRecordSummaryRespVO().setSuccessUserCount(successUserCount);
        Map<Long, BargainActivityDO> activityMap = convertMap(activityList, BargainActivityDO::getId);
        summary.setSuccessList(CollectionUtils.convertList(successList, record -> {
            AppBargainRecordSummaryRespVO.Record recordVO = new AppBargainRecordSummaryRespVO.Record();
            MapUtils.findAndThen(userMap, record.getUserId(),
                    user -> recordVO.setNickname(user.getNickname()).setAvatar(user.getAvatar()));
            MapUtils.findAndThen(activityMap, record.getActivityId(),
                    activity -> recordVO.setActivityName(activity.getName()));
            return recordVO;
        }));
        return summary;
    }

    @Mapping(source = "record.id", target = "id")
    @Mapping(source = "record.userId", target = "userId")
    @Mapping(source = "record.status", target = "status")
    AppBargainRecordDetailRespVO convert02(BargainRecordDO record, Integer helpAction, TradeOrderRespDTO order);

}
