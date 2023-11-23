package com.johnnylee.cloud.module.system.convert.sms;

import com.johnnylee.cloud.module.system.controller.admin.sms.vo.channel.SmsChannelCreateReqVO;
import com.johnnylee.cloud.module.system.controller.admin.sms.vo.channel.SmsChannelRespVO;
import com.johnnylee.cloud.module.system.controller.admin.sms.vo.channel.SmsChannelSimpleRespVO;
import com.johnnylee.cloud.module.system.controller.admin.sms.vo.channel.SmsChannelUpdateReqVO;
import com.johnnylee.cloud.module.system.dal.dataobject.sms.SmsChannelDO;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.sms.core.property.SmsChannelProperties;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 短信渠道 Convert
 *
 * @author Johnny
 */
@Mapper
public interface SmsChannelConvert {

    SmsChannelConvert INSTANCE = Mappers.getMapper(SmsChannelConvert.class);

    SmsChannelDO convert(SmsChannelCreateReqVO bean);

    SmsChannelDO convert(SmsChannelUpdateReqVO bean);

    SmsChannelRespVO convert(SmsChannelDO bean);

    List<SmsChannelRespVO> convertList(List<SmsChannelDO> list);

    PageResult<SmsChannelRespVO> convertPage(PageResult<SmsChannelDO> page);

    List<SmsChannelSimpleRespVO> convertList03(List<SmsChannelDO> list);

    SmsChannelProperties convert02(SmsChannelDO channel);

}
