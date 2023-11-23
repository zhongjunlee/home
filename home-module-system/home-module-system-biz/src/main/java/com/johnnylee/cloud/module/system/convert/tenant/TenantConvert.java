package com.johnnylee.cloud.module.system.convert.tenant;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.system.controller.admin.tenant.vo.tenant.TenantCreateReqVO;
import com.johnnylee.cloud.module.system.controller.admin.tenant.vo.tenant.TenantExcelVO;
import com.johnnylee.cloud.module.system.controller.admin.tenant.vo.tenant.TenantRespVO;
import com.johnnylee.cloud.module.system.controller.admin.tenant.vo.tenant.TenantUpdateReqVO;
import com.johnnylee.cloud.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import com.johnnylee.cloud.module.system.dal.dataobject.tenant.TenantDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 租户 Convert
 *
 * @author Johnny
 */
@Mapper
public interface TenantConvert {

    TenantConvert INSTANCE = Mappers.getMapper(TenantConvert.class);

    TenantDO convert(TenantCreateReqVO bean);

    TenantDO convert(TenantUpdateReqVO bean);

    TenantRespVO convert(TenantDO bean);

    List<TenantRespVO> convertList(List<TenantDO> list);

    PageResult<TenantRespVO> convertPage(PageResult<TenantDO> page);

    List<TenantExcelVO> convertList02(List<TenantDO> list);

    default UserCreateReqVO convert02(TenantCreateReqVO bean) {
        UserCreateReqVO reqVO = new UserCreateReqVO();
        reqVO.setUsername(bean.getUsername());
        reqVO.setPassword(bean.getPassword());
        reqVO.setNickname(bean.getContactName()).setMobile(bean.getContactMobile());
        return reqVO;
    }

}
