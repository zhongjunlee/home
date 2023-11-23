package com.johnnylee.cloud.module.infra.controller.admin.job;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.error;

@Tag(name = "管理后台 - 定时任务")
@RestController
@RequestMapping("/infra/job")
@Validated
public class JobController {

    @GetMapping("/page")
    @Operation(summary = "获得定时任务分页")
    @PreAuthorize("@ss.hasPermission('infra:job:query')")
    public CommonResult<String> getJobPage() {
        return error(-1, "Cloud 版本使用 XXL-Job 作为定时任务！请参考 https://cloud.iocoder.cn/job/ 文档操作");
    }

}
