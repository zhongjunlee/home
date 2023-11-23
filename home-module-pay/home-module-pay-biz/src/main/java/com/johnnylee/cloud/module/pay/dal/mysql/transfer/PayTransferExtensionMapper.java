package com.johnnylee.cloud.module.pay.dal.mysql.transfer;

import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.module.pay.dal.dataobject.transfer.PayTransferExtensionDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PayTransferExtensionMapper extends BaseMapperX<PayTransferExtensionDO> {

    default PayTransferExtensionDO selectByNo(String no){
       return  selectOne(PayTransferExtensionDO::getNo, no);
    }

    default int updateByIdAndStatus(Long id, List<Integer> whereStatuses, PayTransferExtensionDO updateObj) {
        return update(updateObj, new LambdaQueryWrapper<PayTransferExtensionDO>()
                .eq(PayTransferExtensionDO::getId, id).in(PayTransferExtensionDO::getStatus, whereStatuses));
    }

}




