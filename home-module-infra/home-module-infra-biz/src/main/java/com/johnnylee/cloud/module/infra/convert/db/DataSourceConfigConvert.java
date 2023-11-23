package com.johnnylee.cloud.module.infra.convert.db;

import java.util.*;

import com.johnnylee.cloud.module.infra.controller.admin.db.vo.DataSourceConfigCreateReqVO;
import com.johnnylee.cloud.module.infra.controller.admin.db.vo.DataSourceConfigRespVO;
import com.johnnylee.cloud.module.infra.controller.admin.db.vo.DataSourceConfigUpdateReqVO;
import com.johnnylee.cloud.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.johnnylee.cloud.module.infra.controller.admin.db.vo.*;

/**
 * 数据源配置 Convert
 *
 * @author Johnny
 */
@Mapper
public interface DataSourceConfigConvert {

    DataSourceConfigConvert INSTANCE = Mappers.getMapper(DataSourceConfigConvert.class);

    DataSourceConfigDO convert(DataSourceConfigCreateReqVO bean);

    DataSourceConfigDO convert(DataSourceConfigUpdateReqVO bean);

    DataSourceConfigRespVO convert(DataSourceConfigDO bean);

    List<DataSourceConfigRespVO> convertList(List<DataSourceConfigDO> list);

}
