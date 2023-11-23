package com.johnnylee.cloud.module.trade.dal.mysql.brokerage;

import cn.hutool.core.bean.BeanUtil;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.johnnylee.cloud.module.trade.controller.admin.brokerage.vo.withdraw.BrokerageWithdrawPageReqVO;
import com.johnnylee.cloud.module.trade.dal.dataobject.brokerage.BrokerageWithdrawDO;
import com.johnnylee.cloud.module.trade.service.brokerage.bo.BrokerageWithdrawSummaryRespBO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 佣金提现 Mapper
 *
 * @author Johnny
 */
@Mapper
public interface BrokerageWithdrawMapper extends BaseMapperX<BrokerageWithdrawDO> {

    default PageResult<BrokerageWithdrawDO> selectPage(BrokerageWithdrawPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BrokerageWithdrawDO>()
                .eqIfPresent(BrokerageWithdrawDO::getUserId, reqVO.getUserId())
                .eqIfPresent(BrokerageWithdrawDO::getType, reqVO.getType())
                .likeIfPresent(BrokerageWithdrawDO::getName, reqVO.getName())
                .eqIfPresent(BrokerageWithdrawDO::getAccountNo, reqVO.getAccountNo())
                .likeIfPresent(BrokerageWithdrawDO::getBankName, reqVO.getBankName())
                .eqIfPresent(BrokerageWithdrawDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BrokerageWithdrawDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(BrokerageWithdrawDO::getStatus).orderByDesc(BrokerageWithdrawDO::getId));
    }

    default int updateByIdAndStatus(Integer id, Integer status, BrokerageWithdrawDO updateObj) {
        return update(updateObj, new LambdaUpdateWrapper<BrokerageWithdrawDO>()
                .eq(BrokerageWithdrawDO::getId, id)
                .eq(BrokerageWithdrawDO::getStatus, status));
    }

    default List<BrokerageWithdrawSummaryRespBO> selectCountAndSumPriceByUserIdAndStatus(Collection<Long> userIds, Integer status) {
        List<Map<String, Object>> list = selectMaps(new MPJLambdaWrapper<BrokerageWithdrawDO>()
                .select(BrokerageWithdrawDO::getUserId)
                .selectCount(BrokerageWithdrawDO::getId, BrokerageWithdrawSummaryRespBO::getCount)
                .selectSum(BrokerageWithdrawDO::getPrice)
                .in(BrokerageWithdrawDO::getUserId, userIds)
                .eq(BrokerageWithdrawDO::getStatus, status)
                .groupBy(BrokerageWithdrawDO::getUserId));
        return BeanUtil.copyToList(list, BrokerageWithdrawSummaryRespBO.class);
        // selectJoinList有BUG，会与租户插件冲突：解析SQL时，发生异常 https://gitee.com/best_handsome/mybatis-plus-join/issues/I84GYW
//        return selectJoinList(UserWithdrawSummaryBO.class, new MPJLambdaWrapper<BrokerageWithdrawDO>()
//                .select(BrokerageWithdrawDO::getUserId)
//                    .selectCount(BrokerageWithdrawDO::getId, UserWithdrawSummaryBO::getCount)
//                .selectSum(BrokerageWithdrawDO::getPrice)
//                .in(BrokerageWithdrawDO::getUserId, userIds)
//                .eq(BrokerageWithdrawDO::getStatus, status)
//                .groupBy(BrokerageWithdrawDO::getUserId));
    }

}
