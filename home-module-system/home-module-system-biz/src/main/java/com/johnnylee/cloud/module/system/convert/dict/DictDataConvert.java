package com.johnnylee.cloud.module.system.convert.dict;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.system.api.dict.dto.DictDataRespDTO;
import com.johnnylee.cloud.module.system.controller.admin.dict.vo.data.*;
import com.johnnylee.cloud.module.system.controller.app.dict.vo.AppDictDataRespVO;
import com.johnnylee.cloud.module.system.dal.dataobject.dict.DictDataDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DictDataConvert {

    DictDataConvert INSTANCE = Mappers.getMapper(DictDataConvert.class);

    List<DictDataSimpleRespVO> convertList(List<DictDataDO> list);

    DictDataRespVO convert(DictDataDO bean);

    PageResult<DictDataRespVO> convertPage(PageResult<DictDataDO> page);

    DictDataDO convert(DictDataUpdateReqVO bean);

    DictDataDO convert(DictDataCreateReqVO bean);

    List<DictDataExcelVO> convertList02(List<DictDataDO> bean);

    DictDataRespDTO convert02(DictDataDO bean);

    List<AppDictDataRespVO> convertList03(List<DictDataDO> list);

}
