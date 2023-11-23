package com.johnnylee.cloud.module.member.api.point;

import cn.hutool.core.lang.Assert;
import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.member.enums.point.MemberPointBizTypeEnum;
import com.johnnylee.cloud.module.member.service.point.MemberPointRecordService;
import com.johnnylee.cloud.module.member.enums.ErrorCodeConstants;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

/**
 * 用户积分的 API 实现类
 *
 * @author owen
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class MemberPointApiImpl implements MemberPointApi {

    @Resource
    private MemberPointRecordService memberPointRecordService;

    @Override
    public CommonResult<Boolean> addPoint(Long userId, Integer point, Integer bizType, String bizId) {
        Assert.isTrue(point > 0);
        MemberPointBizTypeEnum bizTypeEnum = MemberPointBizTypeEnum.getByType(bizType);
        if (bizTypeEnum == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POINT_RECORD_BIZ_NOT_SUPPORT);
        }
        memberPointRecordService.createPointRecord(userId, point, bizTypeEnum, bizId);
        return success(true);
    }

    @Override
    public CommonResult<Boolean> reducePoint(Long userId, Integer point, Integer bizType, String bizId) {
        Assert.isTrue(point > 0);
        MemberPointBizTypeEnum bizTypeEnum = MemberPointBizTypeEnum.getByType(bizType);
        if (bizTypeEnum == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POINT_RECORD_BIZ_NOT_SUPPORT);
        }
        memberPointRecordService.createPointRecord(userId, -point, bizTypeEnum, bizId);
        return success(true);
    }

}
