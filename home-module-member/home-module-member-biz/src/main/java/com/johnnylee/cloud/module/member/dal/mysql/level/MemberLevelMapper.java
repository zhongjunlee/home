package com.johnnylee.cloud.module.member.dal.mysql.level;

import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.johnnylee.cloud.module.member.controller.admin.level.vo.level.MemberLevelListReqVO;
import com.johnnylee.cloud.module.member.dal.dataobject.level.MemberLevelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 会员等级 Mapper
 *
 * @author owen
 */
@Mapper
public interface MemberLevelMapper extends BaseMapperX<MemberLevelDO> {

    default List<MemberLevelDO> selectList(MemberLevelListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MemberLevelDO>()
                .likeIfPresent(MemberLevelDO::getName, reqVO.getName())
                .eqIfPresent(MemberLevelDO::getStatus, reqVO.getStatus())
                .orderByAsc(MemberLevelDO::getLevel));
    }


    default List<MemberLevelDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<MemberLevelDO>()
                .eq(MemberLevelDO::getStatus, status)
                .orderByAsc(MemberLevelDO::getLevel));
    }

}
