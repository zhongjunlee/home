package com.johnnylee.cloud.module.pay.dal.mysql.app;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.johnnylee.cloud.module.pay.controller.admin.app.vo.PayAppPageReqVO;
import com.johnnylee.cloud.module.pay.dal.dataobject.app.PayAppDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayAppMapper extends BaseMapperX<PayAppDO> {

    default PageResult<PayAppDO> selectPage(PayAppPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PayAppDO>()
                .likeIfPresent(PayAppDO::getName, reqVO.getName())
                .eqIfPresent(PayAppDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(PayAppDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayAppDO::getId));
    }

}
