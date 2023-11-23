package com.johnnylee.cloud.module.bpm.convert.task;

import com.johnnylee.cloud.framework.common.util.date.DateUtils;
import com.johnnylee.cloud.module.bpm.controller.admin.task.vo.activity.BpmActivityRespVO;
import org.flowable.engine.history.HistoricActivityInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * BPM 活动 Convert
 *
 * @author Johnny
 */
@Mapper(uses = DateUtils.class)
public interface BpmActivityConvert {

    BpmActivityConvert INSTANCE = Mappers.getMapper(BpmActivityConvert.class);

    List<BpmActivityRespVO> convertList(List<HistoricActivityInstance> list);

    @Mappings({
            @Mapping(source = "activityId", target = "key"),
            @Mapping(source = "activityType", target = "type")
    })
    BpmActivityRespVO convert(HistoricActivityInstance bean);
}
