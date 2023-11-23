package com.johnnylee.cloud.module.bpm.service.oa;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.bpm.api.task.BpmProcessInstanceApi;
import com.johnnylee.cloud.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.johnnylee.cloud.module.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import com.johnnylee.cloud.module.bpm.controller.admin.oa.vo.BpmOALeavePageReqVO;
import com.johnnylee.cloud.module.bpm.convert.oa.BpmOALeaveConvert;
import com.johnnylee.cloud.module.bpm.dal.dataobject.oa.BpmOALeaveDO;
import com.johnnylee.cloud.module.bpm.dal.mysql.oa.BpmOALeaveMapper;
import com.johnnylee.cloud.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.johnnylee.cloud.module.bpm.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * OA 请假申请 Service 实现类
 *
 * @author jason
 * @author Johnny
 */
@Service
@Validated
public class BpmOALeaveServiceImpl implements BpmOALeaveService {

    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "oa_leave";

    @Resource
    private BpmOALeaveMapper leaveMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLeave(Long userId, BpmOALeaveCreateReqVO createReqVO) {
        // 插入 OA 请假单
        long day = LocalDateTimeUtil.between(createReqVO.getStartTime(), createReqVO.getEndTime()).toDays();
        BpmOALeaveDO leave = BpmOALeaveConvert.INSTANCE.convert(createReqVO).setUserId(userId).setDay(day)
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        leaveMapper.insert(leave);

        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("day", day);
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(leave.getId()))).getCheckedData();

        // 将工作流的编号，更新到 OA 请假单中
        leaveMapper.updateById(new BpmOALeaveDO().setId(leave.getId()).setProcessInstanceId(processInstanceId));
        return leave.getId();
    }

    @Override
    public void updateLeaveResult(Long id, Integer result) {
        validateLeaveExists(id);
        leaveMapper.updateById(new BpmOALeaveDO().setId(id).setResult(result));
    }

    private void validateLeaveExists(Long id) {
        if (leaveMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.OA_LEAVE_NOT_EXISTS);
        }
    }

    @Override
    public BpmOALeaveDO getLeave(Long id) {
        return leaveMapper.selectById(id);
    }

    @Override
    public PageResult<BpmOALeaveDO> getLeavePage(Long userId, BpmOALeavePageReqVO pageReqVO) {
        return leaveMapper.selectPage(userId, pageReqVO);
    }

}
