package com.johnnylee.cloud.framework.common.util.object;

import com.johnnylee.cloud.framework.common.pojo.PageParam;

/**
 * {@link PageParam} 工具类
 *
 * @author Johnny
 */
public class PageUtils {

    public static int getStart(PageParam pageParam) {
        return (pageParam.getPageNo() - 1) * pageParam.getPageSize();
    }

}
