package com.johnnylee.cloud.module.promotion.dal.dataobject.banner;

import com.johnnylee.cloud.framework.common.enums.CommonStatusEnum;
import com.johnnylee.cloud.framework.mybatis.core.dataobject.BaseDO;
import com.johnnylee.cloud.module.promotion.enums.banner.BannerPositionEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * banner DO
 *
 * @author xia
 */
@TableName("promotion_banner")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDO extends BaseDO {

    /**
     * 编号
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 跳转链接
     */
    private String url;
    /**
     * 图片链接
     */
    private String picUrl;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 定位 {@link BannerPositionEnum}
     */
    private Integer position;

    /**
     * 备注
     */
    private String memo;

    /**
     * 点击次数
     */
    private Integer browseCount;

}
