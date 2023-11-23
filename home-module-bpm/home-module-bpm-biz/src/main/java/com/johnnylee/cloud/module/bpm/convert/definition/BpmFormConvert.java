package com.johnnylee.cloud.module.bpm.convert.definition;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.bpm.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import com.johnnylee.cloud.module.bpm.controller.admin.definition.vo.form.BpmFormRespVO;
import com.johnnylee.cloud.module.bpm.controller.admin.definition.vo.form.BpmFormSimpleRespVO;
import com.johnnylee.cloud.module.bpm.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import com.johnnylee.cloud.module.bpm.dal.dataobject.definition.BpmFormDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 动态表单 Convert
 *
 * @author 芋艿
 */
@Mapper
public interface BpmFormConvert {

    BpmFormConvert INSTANCE = Mappers.getMapper(BpmFormConvert.class);

    BpmFormDO convert(BpmFormCreateReqVO bean);

    BpmFormDO convert(BpmFormUpdateReqVO bean);

    BpmFormRespVO convert(BpmFormDO bean);

    List<BpmFormSimpleRespVO> convertList2(List<BpmFormDO> list);

    PageResult<BpmFormRespVO> convertPage(PageResult<BpmFormDO> page);

}
