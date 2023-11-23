package com.johnnylee.cloud.module.system.convert.logger;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.common.util.collection.MapUtils;
import com.johnnylee.cloud.module.system.api.logger.dto.OperateLogCreateReqDTO;
import com.johnnylee.cloud.module.system.controller.admin.logger.vo.operatelog.OperateLogExcelVO;
import com.johnnylee.cloud.module.system.controller.admin.logger.vo.operatelog.OperateLogRespVO;
import com.johnnylee.cloud.module.system.dal.dataobject.logger.OperateLogDO;
import com.johnnylee.cloud.module.system.dal.dataobject.user.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.johnnylee.cloud.framework.common.exception.enums.GlobalErrorCodeConstants.SUCCESS;

@Mapper
public interface OperateLogConvert {

    OperateLogConvert INSTANCE = Mappers.getMapper(OperateLogConvert.class);

    OperateLogDO convert(OperateLogCreateReqDTO bean);

    PageResult<OperateLogRespVO> convertPage(PageResult<OperateLogDO> page);

    OperateLogRespVO convert(OperateLogDO bean);

    default List<OperateLogExcelVO> convertList(List<OperateLogDO> list, Map<Long, AdminUserDO> userMap) {
        return list.stream().map(operateLog -> {
            OperateLogExcelVO excelVO = convert02(operateLog);
            MapUtils.findAndThen(userMap, operateLog.getUserId(), user -> excelVO.setUserNickname(user.getNickname()));
            excelVO.setSuccessStr(SUCCESS.getCode().equals(operateLog.getResultCode()) ? "成功" : "失败");
            return excelVO;
        }).collect(Collectors.toList());
    }

    OperateLogExcelVO convert02(OperateLogDO bean);

}
