package com.johnnylee.cloud.module.system.dal.mysql.logger;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.johnnylee.cloud.module.system.controller.admin.logger.vo.loginlog.LoginLogExportReqVO;
import com.johnnylee.cloud.module.system.controller.admin.logger.vo.loginlog.LoginLogPageReqVO;
import com.johnnylee.cloud.module.system.dal.dataobject.logger.LoginLogDO;
import com.johnnylee.cloud.module.system.enums.logger.LoginResultEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginLogMapper extends BaseMapperX<LoginLogDO> {

    default PageResult<LoginLogDO> selectPage(LoginLogPageReqVO reqVO) {
        LambdaQueryWrapperX<LoginLogDO> query = new LambdaQueryWrapperX<LoginLogDO>()
                .likeIfPresent(LoginLogDO::getUserIp, reqVO.getUserIp())
                .likeIfPresent(LoginLogDO::getUsername, reqVO.getUsername())
                .betweenIfPresent(LoginLogDO::getCreateTime, reqVO.getCreateTime());
        if (Boolean.TRUE.equals(reqVO.getStatus())) {
            query.eq(LoginLogDO::getResult, LoginResultEnum.SUCCESS.getResult());
        } else if (Boolean.FALSE.equals(reqVO.getStatus())) {
            query.gt(LoginLogDO::getResult, LoginResultEnum.SUCCESS.getResult());
        }
        query.orderByDesc(LoginLogDO::getId); // 降序
        return selectPage(reqVO, query);
    }

    default List<LoginLogDO> selectList(LoginLogExportReqVO reqVO) {
        LambdaQueryWrapperX<LoginLogDO> query = new LambdaQueryWrapperX<LoginLogDO>()
                .likeIfPresent(LoginLogDO::getUserIp, reqVO.getUserIp())
                .likeIfPresent(LoginLogDO::getUsername, reqVO.getUsername())
                .betweenIfPresent(LoginLogDO::getCreateTime, reqVO.getCreateTime());
        if (Boolean.TRUE.equals(reqVO.getStatus())) {
            query.eq(LoginLogDO::getResult, LoginResultEnum.SUCCESS.getResult());
        } else if (Boolean.FALSE.equals(reqVO.getStatus())) {
            query.gt(LoginLogDO::getResult, LoginResultEnum.SUCCESS.getResult());
        }
        query.orderByDesc(LoginLogDO::getId); // 降序
        return selectList(query);
    }

}
