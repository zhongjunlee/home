package com.johnnylee.cloud.module.promotion.service.decorate;

import com.johnnylee.cloud.module.promotion.controller.admin.decorate.vo.DecorateComponentSaveReqVO;
import com.johnnylee.cloud.module.promotion.convert.decorate.DecorateComponentConvert;
import com.johnnylee.cloud.module.promotion.dal.dataobject.decorate.DecorateComponentDO;
import com.johnnylee.cloud.module.promotion.dal.mysql.decorate.DecorateComponentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 装修组件 Service 实现
 *
 * @author jason
 */
@Service
public class DecorateComponentServiceImpl implements DecorateComponentService {

    @Resource
    private DecorateComponentMapper decorateComponentMapper;

    @Override
    public void saveDecorateComponent(DecorateComponentSaveReqVO reqVO) {
        // 1. 如果存在，则进行更新
        DecorateComponentDO dbComponent = decorateComponentMapper.selectByPageAndCode(reqVO.getPage(), reqVO.getCode());
        if (dbComponent != null) {
            decorateComponentMapper.updateById(DecorateComponentConvert.INSTANCE.convert(reqVO).setId(dbComponent.getId()));
            return;
        }
        // 2. 不存在，则进行新增
        decorateComponentMapper.insert(DecorateComponentConvert.INSTANCE.convert(reqVO));
    }

    @Override
    public List<DecorateComponentDO> getDecorateComponentListByPage(Integer page, Integer status) {
        return decorateComponentMapper.selectListByPageAndStatus(page, status);
    }

}
