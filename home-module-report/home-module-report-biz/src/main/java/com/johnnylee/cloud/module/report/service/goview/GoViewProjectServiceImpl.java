package com.johnnylee.cloud.module.report.service.goview;

import com.johnnylee.cloud.framework.common.enums.CommonStatusEnum;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.PageParam;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.report.controller.admin.goview.vo.project.GoViewProjectCreateReqVO;
import com.johnnylee.cloud.module.report.controller.admin.goview.vo.project.GoViewProjectUpdateReqVO;
import com.johnnylee.cloud.module.report.convert.goview.GoViewProjectConvert;
import com.johnnylee.cloud.module.report.dal.dataobject.goview.GoViewProjectDO;
import com.johnnylee.cloud.module.report.dal.mysql.goview.GoViewProjectMapper;
import com.johnnylee.cloud.module.report.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * GoView 项目 Service 实现类
 *
 * @author Johnny
 */
@Service
@Validated
public class GoViewProjectServiceImpl implements GoViewProjectService {

    @Resource
    private GoViewProjectMapper goViewProjectMapper;

    @Override
    public Long createProject(GoViewProjectCreateReqVO createReqVO) {
        // 插入
        GoViewProjectDO goViewProject = GoViewProjectConvert.INSTANCE.convert(createReqVO)
                .setStatus(CommonStatusEnum.DISABLE.getStatus());
        goViewProjectMapper.insert(goViewProject);
        // 返回
        return goViewProject.getId();
    }

    @Override
    public void updateProject(GoViewProjectUpdateReqVO updateReqVO) {
        // 校验存在
        validateProjectExists(updateReqVO.getId());
        // 更新
        GoViewProjectDO updateObj = GoViewProjectConvert.INSTANCE.convert(updateReqVO);
        goViewProjectMapper.updateById(updateObj);
    }

    @Override
    public void deleteProject(Long id) {
        // 校验存在
        validateProjectExists(id);
        // 删除
        goViewProjectMapper.deleteById(id);
    }

    private void validateProjectExists(Long id) {
        if (goViewProjectMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.GO_VIEW_PROJECT_NOT_EXISTS);
        }
    }

    @Override
    public GoViewProjectDO getProject(Long id) {
        return goViewProjectMapper.selectById(id);
    }

    @Override
    public PageResult<GoViewProjectDO> getMyProjectPage(PageParam pageReqVO, Long userId) {
        return goViewProjectMapper.selectPage(pageReqVO, userId);
    }

}
