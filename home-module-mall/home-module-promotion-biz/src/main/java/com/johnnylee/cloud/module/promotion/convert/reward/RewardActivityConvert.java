package com.johnnylee.cloud.module.promotion.convert.reward;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.promotion.controller.admin.reward.vo.RewardActivityCreateReqVO;
import com.johnnylee.cloud.module.promotion.controller.admin.reward.vo.RewardActivityRespVO;
import com.johnnylee.cloud.module.promotion.controller.admin.reward.vo.RewardActivityUpdateReqVO;
import com.johnnylee.cloud.module.promotion.dal.dataobject.reward.RewardActivityDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 满减送活动 Convert
 *
 * @author Johnny
 */
@Mapper
public interface RewardActivityConvert {

    RewardActivityConvert INSTANCE = Mappers.getMapper(RewardActivityConvert.class);

    RewardActivityDO convert(RewardActivityCreateReqVO bean);

    RewardActivityDO convert(RewardActivityUpdateReqVO bean);

    RewardActivityRespVO convert(RewardActivityDO bean);

    PageResult<RewardActivityRespVO> convertPage(PageResult<RewardActivityDO> page);

}
