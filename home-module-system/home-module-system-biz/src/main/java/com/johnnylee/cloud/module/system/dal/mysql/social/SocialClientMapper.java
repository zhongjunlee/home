package com.johnnylee.cloud.module.system.dal.mysql.social;

import com.johnnylee.cloud.framework.mybatis.core.mapper.BaseMapperX;
import com.johnnylee.cloud.module.system.dal.dataobject.social.SocialClientDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SocialClientMapper extends BaseMapperX<SocialClientDO> {

    default SocialClientDO selectBySocialTypeAndUserType(Integer socialType, Integer userType) {
        return selectOne(SocialClientDO::getSocialType, socialType,
                SocialClientDO::getUserType, userType);
    }

}
