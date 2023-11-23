package com.johnnylee.cloud.module.system.convert.sms;

import com.johnnylee.cloud.module.system.controller.admin.sms.vo.log.SmsLogExcelVO;
import com.johnnylee.cloud.module.system.controller.admin.sms.vo.log.SmsLogRespVO;
import com.johnnylee.cloud.module.system.dal.dataobject.sms.SmsLogDO;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 短信日志 Convert
 *
 * @author Johnny
 */
@Mapper
public interface SmsLogConvert {

    SmsLogConvert INSTANCE = Mappers.getMapper(SmsLogConvert.class);

    SmsLogRespVO convert(SmsLogDO bean);

    List<SmsLogRespVO> convertList(List<SmsLogDO> list);

    PageResult<SmsLogRespVO> convertPage(PageResult<SmsLogDO> page);

    List<SmsLogExcelVO> convertList02(List<SmsLogDO> list);

}
