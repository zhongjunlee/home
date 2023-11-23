package com.johnnylee.cloud.module.system.convert.tenant;

import com.johnnylee.cloud.framework.common.pojo.PageResult;
import com.johnnylee.cloud.module.system.controller.admin.tenant.vo.packages.TenantPackageCreateReqVO;
import com.johnnylee.cloud.module.system.controller.admin.tenant.vo.packages.TenantPackageRespVO;
import com.johnnylee.cloud.module.system.controller.admin.tenant.vo.packages.TenantPackageSimpleRespVO;
import com.johnnylee.cloud.module.system.controller.admin.tenant.vo.packages.TenantPackageUpdateReqVO;
import com.johnnylee.cloud.module.system.dal.dataobject.tenant.TenantPackageDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 租户套餐 Convert
 *
 * @author Johnny
 */
@Mapper
public interface TenantPackageConvert {

    TenantPackageConvert INSTANCE = Mappers.getMapper(TenantPackageConvert.class);

    TenantPackageDO convert(TenantPackageCreateReqVO bean);

    TenantPackageDO convert(TenantPackageUpdateReqVO bean);

    TenantPackageRespVO convert(TenantPackageDO bean);

    List<TenantPackageRespVO> convertList(List<TenantPackageDO> list);

    PageResult<TenantPackageRespVO> convertPage(PageResult<TenantPackageDO> page);

    List<TenantPackageSimpleRespVO> convertList02(List<TenantPackageDO> list);

}
