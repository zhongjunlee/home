package com.johnnylee.cloud.module.trade.service.config;

import com.johnnylee.cloud.framework.common.util.collection.CollectionUtils;
import com.johnnylee.cloud.module.trade.controller.admin.config.vo.TradeConfigSaveReqVO;
import com.johnnylee.cloud.module.trade.convert.config.TradeConfigConvert;
import com.johnnylee.cloud.module.trade.dal.dataobject.config.TradeConfigDO;
import com.johnnylee.cloud.module.trade.dal.mysql.config.TradeConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 交易中心配置 Service 实现类
 *
 * @author owen
 */
@Service
@Validated
public class TradeConfigServiceImpl implements TradeConfigService {

    @Resource
    private TradeConfigMapper tradeConfigMapper;

    @Override
    public void saveTradeConfig(TradeConfigSaveReqVO saveReqVO) {
        // 存在，则进行更新
        TradeConfigDO dbConfig = getTradeConfig();
        if (dbConfig != null) {
            tradeConfigMapper.updateById(TradeConfigConvert.INSTANCE.convert(saveReqVO).setId(dbConfig.getId()));
            return;
        }
        // 不存在，则进行插入
        tradeConfigMapper.insert(TradeConfigConvert.INSTANCE.convert(saveReqVO));
    }

    @Override
    public TradeConfigDO getTradeConfig() {
        List<TradeConfigDO> list = tradeConfigMapper.selectList();
        return CollectionUtils.getFirst(list);
    }

}
