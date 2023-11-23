package com.johnnylee.cloud.module.bpm.convert.oa;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import com.johnnylee.cloud.module.bpm.controller.admin.oa.vo.BpmOALeaveRespVO;
import com.johnnylee.cloud.module.bpm.dal.dataobject.oa.BpmOALeaveDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 请假申请 Convert
 *
 * @author 芋艿
 */
@Mapper
public interface BpmOALeaveConvert {

    BpmOALeaveConvert INSTANCE = Mappers.getMapper(BpmOALeaveConvert.class);

    BpmOALeaveDO convert(BpmOALeaveCreateReqVO bean);

    BpmOALeaveRespVO convert(BpmOALeaveDO bean);

    List<BpmOALeaveRespVO> convertList(List<BpmOALeaveDO> list);

    PageResult<BpmOALeaveRespVO> convertPage(PageResult<BpmOALeaveDO> page);

}
