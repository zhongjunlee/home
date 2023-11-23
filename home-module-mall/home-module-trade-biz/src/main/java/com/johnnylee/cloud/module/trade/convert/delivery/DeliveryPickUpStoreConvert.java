package com.johnnylee.cloud.module.trade.convert.delivery;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.common.util.collection.CollectionUtils;
import com.johnnylee.cloud.framework.common.util.number.NumberUtils;
import com.johnnylee.cloud.framework.ip.core.utils.AreaUtils;
import com.johnnylee.cloud.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreCreateReqVO;
import com.johnnylee.cloud.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreRespVO;
import com.johnnylee.cloud.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreSimpleRespVO;
import com.johnnylee.cloud.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStoreUpdateReqVO;
import com.johnnylee.cloud.module.trade.controller.app.delivery.vo.pickup.AppDeliveryPickUpStoreRespVO;
import com.johnnylee.cloud.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeliveryPickUpStoreConvert {

    DeliveryPickUpStoreConvert INSTANCE = Mappers.getMapper(DeliveryPickUpStoreConvert.class);

    DeliveryPickUpStoreDO convert(DeliveryPickUpStoreCreateReqVO bean);

    DeliveryPickUpStoreDO convert(DeliveryPickUpStoreUpdateReqVO bean);

    DeliveryPickUpStoreRespVO convert(DeliveryPickUpStoreDO bean);

    List<DeliveryPickUpStoreRespVO> convertList(List<DeliveryPickUpStoreDO> list);

    PageResult<DeliveryPickUpStoreRespVO> convertPage(PageResult<DeliveryPickUpStoreDO> page);

    List<DeliveryPickUpStoreSimpleRespVO> convertList1(List<DeliveryPickUpStoreDO> list);
    @Mapping(source = "areaId", target = "areaName", qualifiedByName = "convertAreaIdToAreaName")
    DeliveryPickUpStoreSimpleRespVO convert02(DeliveryPickUpStoreDO bean);

    @Named("convertAreaIdToAreaName")
    default String convertAreaIdToAreaName(Integer areaId) {
        return AreaUtils.format(areaId);
    }

    default List<AppDeliveryPickUpStoreRespVO> convertList(List<DeliveryPickUpStoreDO> list,
                                                           Double latitude, Double longitude) {
        List<AppDeliveryPickUpStoreRespVO> voList =  CollectionUtils.convertList(list, store -> {
            AppDeliveryPickUpStoreRespVO storeVO = convert03(store);
            if (latitude != null && longitude != null) {
                storeVO.setDistance(NumberUtils.getDistance(latitude, longitude, storeVO.getLatitude(), storeVO.getLongitude()));
            }
            return storeVO;
        });
        return voList;
    }
    @Mapping(source = "areaId", target = "areaName", qualifiedByName = "convertAreaIdToAreaName")
    AppDeliveryPickUpStoreRespVO convert03(DeliveryPickUpStoreDO bean);

}
