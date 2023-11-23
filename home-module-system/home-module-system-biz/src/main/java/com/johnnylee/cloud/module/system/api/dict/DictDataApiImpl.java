package com.johnnylee.cloud.module.system.api.dict;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.system.api.dict.dto.DictDataRespDTO;
import com.johnnylee.cloud.module.system.convert.dict.DictDataConvert;
import com.johnnylee.cloud.module.system.dal.dataobject.dict.DictDataDO;
import com.johnnylee.cloud.module.system.service.dict.DictDataService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class DictDataApiImpl implements DictDataApi {

    @Resource
    private DictDataService dictDataService;

    @Override
    public CommonResult<Boolean> validateDictDataList(String dictType, Collection<String> values) {
        dictDataService.validateDictDataList(dictType, values);
        return success(true);
    }

    @Override
    public CommonResult<DictDataRespDTO> getDictData(String dictType, String value) {
        DictDataDO dictData = dictDataService.getDictData(dictType, value);
        return success(DictDataConvert.INSTANCE.convert02(dictData));
    }

    @Override
    public CommonResult<DictDataRespDTO> parseDictData(String dictType, String label) {
        DictDataDO dictData = dictDataService.parseDictData(dictType, label);
        return success(DictDataConvert.INSTANCE.convert02(dictData));
    }

}
