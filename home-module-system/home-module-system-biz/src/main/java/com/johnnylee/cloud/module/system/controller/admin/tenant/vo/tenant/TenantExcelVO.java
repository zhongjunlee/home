package com.johnnylee.cloud.module.system.controller.admin.tenant.vo.tenant;

import com.johnnylee.cloud.framework.excel.core.annotations.DictFormat;
import com.johnnylee.cloud.framework.excel.core.convert.DictConvert;
import com.johnnylee.cloud.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 租户 Excel VO
 *
 * @author Johnny
 */
@Data
public class TenantExcelVO {

    @ExcelProperty("租户编号")
    private Long id;

    @ExcelProperty("租户名")
    private String name;

    @ExcelProperty("联系人")
    private String contactName;

    @ExcelProperty("联系手机")
    private String contactMobile;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
