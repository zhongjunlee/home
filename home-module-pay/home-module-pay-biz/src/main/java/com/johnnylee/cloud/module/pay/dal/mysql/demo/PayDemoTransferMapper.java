package com.johnnylee.cloud.module.pay.dal.mysql.demo;

import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.module.pay.dal.dataobject.demo.PayDemoTransferDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayDemoTransferMapper extends BaseMapperX<PayDemoTransferDO> {

}
