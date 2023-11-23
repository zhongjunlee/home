package com.johnnylee.cloud.module.system.service.logger;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.johnnylee.cloud.module.system.controller.admin.logger.vo.loginlog.LoginLogExportReqVO;
import com.johnnylee.cloud.module.system.controller.admin.logger.vo.loginlog.LoginLogPageReqVO;
import com.johnnylee.cloud.module.system.convert.logger.LoginLogConvert;
import com.johnnylee.cloud.module.system.dal.dataobject.logger.LoginLogDO;
import com.johnnylee.cloud.module.system.dal.mysql.logger.LoginLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 登录日志 Service 实现
 */
@Service
@Validated
public class LoginLogServiceImpl implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public PageResult<LoginLogDO> getLoginLogPage(LoginLogPageReqVO reqVO) {
        return loginLogMapper.selectPage(reqVO);
    }

    @Override
    public List<LoginLogDO> getLoginLogList(LoginLogExportReqVO reqVO) {
        return loginLogMapper.selectList(reqVO);
    }

    @Override
    public void createLoginLog(LoginLogCreateReqDTO reqDTO) {
        LoginLogDO loginLog = LoginLogConvert.INSTANCE.convert(reqDTO);
        loginLogMapper.insert(loginLog);
    }

}
