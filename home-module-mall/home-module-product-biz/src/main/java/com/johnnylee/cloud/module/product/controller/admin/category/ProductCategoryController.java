package com.johnnylee.cloud.module.product.controller.admin.category;

import com.johnnylee.cloud.framework.common.pojo.CommonResult;
import com.johnnylee.cloud.module.product.controller.admin.category.vo.ProductCategoryCreateReqVO;
import com.johnnylee.cloud.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import com.johnnylee.cloud.module.product.controller.admin.category.vo.ProductCategoryRespVO;
import com.johnnylee.cloud.module.product.controller.admin.category.vo.ProductCategoryUpdateReqVO;
import com.johnnylee.cloud.module.product.convert.category.ProductCategoryConvert;
import com.johnnylee.cloud.module.product.dal.dataobject.category.ProductCategoryDO;
import com.johnnylee.cloud.module.product.service.category.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 商品分类")
@RestController
@RequestMapping("/product/category")
@Validated
public class ProductCategoryController {

    @Resource
    private ProductCategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "创建商品分类")
    @PreAuthorize("@ss.hasPermission('product:category:create')")
    public CommonResult<Long> createCategory(@Valid @RequestBody ProductCategoryCreateReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品分类")
    @PreAuthorize("@ss.hasPermission('product:category:update')")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody ProductCategoryUpdateReqVO updateReqVO) {
        categoryService.updateCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('product:category:delete')")
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商品分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('product:category:query')")
    public CommonResult<ProductCategoryRespVO> getCategory(@RequestParam("id") Long id) {
        ProductCategoryDO category = categoryService.getCategory(id);
        return success(ProductCategoryConvert.INSTANCE.convert(category));
    }

    @GetMapping("/list")
    @Operation(summary = "获得商品分类列表")
    @PreAuthorize("@ss.hasPermission('product:category:query')")
    public CommonResult<List<ProductCategoryRespVO>> getCategoryList(@Valid ProductCategoryListReqVO treeListReqVO) {
        List<ProductCategoryDO> list = categoryService.getEnableCategoryList(treeListReqVO);
        list.sort(Comparator.comparing(ProductCategoryDO::getSort));
        return success(ProductCategoryConvert.INSTANCE.convertList(list));
    }

}
