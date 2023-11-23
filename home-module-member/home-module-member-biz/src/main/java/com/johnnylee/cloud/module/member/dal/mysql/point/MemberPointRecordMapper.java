package com.johnnylee.cloud.module.member.dal.mysql.point;

import com.johnnylee.cloud.framework.common.pojo.PageParam;
import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.johnnylee.cloud.module.member.controller.admin.point.vo.recrod.MemberPointRecordPageReqVO;
import com.johnnylee.cloud.module.member.dal.dataobject.point.MemberPointRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * 用户积分记录 Mapper
 *
 * @author QingX
 */
@Mapper
public interface MemberPointRecordMapper extends BaseMapperX<MemberPointRecordDO> {

    default PageResult<MemberPointRecordDO> selectPage(MemberPointRecordPageReqVO reqVO, Set<Long> userIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberPointRecordDO>()
                .inIfPresent(MemberPointRecordDO::getUserId, userIds)
                .eqIfPresent(MemberPointRecordDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MemberPointRecordDO::getBizType, reqVO.getBizType())
                .likeIfPresent(MemberPointRecordDO::getTitle, reqVO.getTitle())
                .orderByDesc(MemberPointRecordDO::getId));
    }

    default PageResult<MemberPointRecordDO> selectPage(Long userId, PageParam pageVO) {
        return selectPage(pageVO, new LambdaQueryWrapperX<MemberPointRecordDO>()
                .eq(MemberPointRecordDO::getUserId, userId)
                .orderByDesc(MemberPointRecordDO::getId));
    }

}
