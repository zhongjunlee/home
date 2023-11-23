package com.johnnylee.cloud.module.promotion.api.combination;

import com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil;
import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.promotion.api.combination.dto.CombinationRecordCreateReqDTO;
import com.johnnylee.cloud.module.promotion.api.combination.dto.CombinationRecordCreateRespDTO;
import com.johnnylee.cloud.module.promotion.api.combination.dto.CombinationValidateJoinRespDTO;
import com.johnnylee.cloud.module.promotion.convert.combination.CombinationActivityConvert;
import com.johnnylee.cloud.module.promotion.dal.dataobject.combination.CombinationRecordDO;
import com.johnnylee.cloud.module.promotion.enums.combination.CombinationRecordStatusEnum;
import com.johnnylee.cloud.module.promotion.service.combination.CombinationRecordService;
import com.johnnylee.cloud.module.promotion.enums.ErrorCodeConstants;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.johnnylee.cloud.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

/**
 * 拼团活动 API 实现类
 *
 * @author HUIHUI
 */
@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class CombinationRecordApiImpl implements CombinationRecordApi {

    @Resource
    private CombinationRecordService recordService;

    @Override
    public CommonResult<Boolean> validateCombinationRecord(Long userId, Long activityId, Long headId, Long skuId, Integer count) {
        recordService.validateCombinationRecord(userId, activityId, headId, skuId, count);
        return success(true);
    }

    @Override
    public CommonResult<CombinationRecordCreateRespDTO> createCombinationRecord(CombinationRecordCreateReqDTO reqDTO) {
        return success(CombinationActivityConvert.INSTANCE.convert4(recordService.createCombinationRecord(reqDTO)));
    }

    @Override
    public CommonResult<Boolean> isCombinationRecordSuccess(Long userId, Long orderId) {
        CombinationRecordDO record = recordService.getCombinationRecord(userId, orderId);
        if (record == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.COMBINATION_RECORD_NOT_EXISTS);
        }
        return success(CombinationRecordStatusEnum.isSuccess(record.getStatus()));
    }

    @Override
    public CommonResult<CombinationValidateJoinRespDTO> validateJoinCombination(
            Long userId, Long activityId, Long headId, Long skuId, Integer count) {
        return success(recordService.validateJoinCombination(userId, activityId, headId, skuId, count));
    }

}
