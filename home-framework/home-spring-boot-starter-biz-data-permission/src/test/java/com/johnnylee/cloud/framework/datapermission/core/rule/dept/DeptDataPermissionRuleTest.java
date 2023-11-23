package com.johnnylee.cloud.framework.datapermission.core.rule.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.johnnylee.cloud.framework.common.enums.UserTypeEnum;
import com.johnnylee.cloud.framework.common.util.collection.SetUtils;
import com.johnnylee.cloud.framework.security.core.LoginUser;
import com.johnnylee.cloud.framework.security.core.util.SecurityFrameworkUtils;
import com.johnnylee.cloud.framework.test.core.ut.BaseMockitoUnitTest;
import com.johnnylee.cloud.framework.test.core.util.RandomUtils;
import com.johnnylee.cloud.module.system.api.permission.PermissionApi;
import com.johnnylee.cloud.module.system.api.permission.dto.DeptDataPermissionRespDTO;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.util.Map;

import static com.johnnylee.cloud.framework.common.pojo.CommonResult.success;
import static com.johnnylee.cloud.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * {@link DeptDataPermissionRule} 的单元测试
 *
 * @author Johnny
 */
class DeptDataPermissionRuleTest extends BaseMockitoUnitTest {

    @InjectMocks
    private DeptDataPermissionRule rule;

    @Mock
    private PermissionApi permissionApi;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        // 清空 rule
        rule.getTableNames().clear();
        ((Map<String, String>) ReflectUtil.getFieldValue(rule, "deptColumns")).clear();
        ((Map<String, String>) ReflectUtil.getFieldValue(rule, "deptColumns")).clear();
    }

    @Test // 无 LoginUser
    public void testGetExpression_noLoginUser() {
        // 准备参数
        String tableName = RandomUtils.randomString();
        Alias tableAlias = new Alias(RandomUtils.randomString());
        // mock 方法

        // 调用
        Expression expression = rule.getExpression(tableName, tableAlias);
        // 断言
        assertNull(expression);
    }

    @Test // 无数据权限时
    public void testGetExpression_noDeptDataPermission() {
        try (MockedStatic<SecurityFrameworkUtils> securityFrameworkUtilsMock
                     = mockStatic(SecurityFrameworkUtils.class)) {
            // 准备参数
            String tableName = "t_user";
            Alias tableAlias = new Alias("u");
            // mock 方法
            LoginUser loginUser = RandomUtils.randomPojo(LoginUser.class, o -> o.setId(1L)
                    .setUserType(UserTypeEnum.ADMIN.getValue()));
            securityFrameworkUtilsMock.when(SecurityFrameworkUtils::getLoginUser).thenReturn(loginUser);
            // mock 方法（permissionApi 返回 null）
            when(permissionApi.getDeptDataPermission(eq(loginUser.getId()))).thenReturn(success(null));

            // 调用
            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> rule.getExpression(tableName, tableAlias));
            // 断言
            assertEquals("LoginUser(1) Table(t_user/u) 未返回数据权限", exception.getMessage());
        }
    }

    @Test // 全部数据权限
    public void testGetExpression_allDeptDataPermission() {
        try (MockedStatic<SecurityFrameworkUtils> securityFrameworkUtilsMock
                     = mockStatic(SecurityFrameworkUtils.class)) {
            // 准备参数
            String tableName = "t_user";
            Alias tableAlias = new Alias("u");
            // mock 方法（LoginUser）
            LoginUser loginUser = RandomUtils.randomPojo(LoginUser.class, o -> o.setId(1L)
                    .setUserType(UserTypeEnum.ADMIN.getValue()));
            securityFrameworkUtilsMock.when(SecurityFrameworkUtils::getLoginUser).thenReturn(loginUser);
            // mock 方法（DeptDataPermissionRespDTO）
            DeptDataPermissionRespDTO deptDataPermission = new DeptDataPermissionRespDTO().setAll(true);
            when(permissionApi.getDeptDataPermission(same(1L))).thenReturn(success(deptDataPermission));

            // 调用
            Expression expression = rule.getExpression(tableName, tableAlias);
            // 断言
            assertNull(expression);
            assertSame(deptDataPermission, loginUser.getContext(DeptDataPermissionRule.CONTEXT_KEY, DeptDataPermissionRespDTO.class));
        }
    }

    @Test // 即不能查看部门，又不能查看自己，则说明 100% 无权限
    public void testGetExpression_noDept_noSelf() {
        try (MockedStatic<SecurityFrameworkUtils> securityFrameworkUtilsMock
                     = mockStatic(SecurityFrameworkUtils.class)) {
            // 准备参数
            String tableName = "t_user";
            Alias tableAlias = new Alias("u");
            // mock 方法（LoginUser）
            LoginUser loginUser = RandomUtils.randomPojo(LoginUser.class, o -> o.setId(1L)
                    .setUserType(UserTypeEnum.ADMIN.getValue()));
            securityFrameworkUtilsMock.when(SecurityFrameworkUtils::getLoginUser).thenReturn(loginUser);
            // mock 方法（DeptDataPermissionRespDTO）
            DeptDataPermissionRespDTO deptDataPermission = new DeptDataPermissionRespDTO();
            when(permissionApi.getDeptDataPermission(same(1L))).thenReturn(success(deptDataPermission));

            // 调用
            Expression expression = rule.getExpression(tableName, tableAlias);
            // 断言
            assertEquals("null = null", expression.toString());
            assertSame(deptDataPermission, loginUser.getContext(DeptDataPermissionRule.CONTEXT_KEY, DeptDataPermissionRespDTO.class));
        }
    }

    @Test // 拼接 Dept 和 User 的条件（字段都不符合）
    public void testGetExpression_noDeptColumn_noSelfColumn() {
        try (MockedStatic<SecurityFrameworkUtils> securityFrameworkUtilsMock
                     = mockStatic(SecurityFrameworkUtils.class)) {
            // 准备参数
            String tableName = "t_user";
            Alias tableAlias = new Alias("u");
            // mock 方法（LoginUser）
            LoginUser loginUser = RandomUtils.randomPojo(LoginUser.class, o -> o.setId(1L)
                    .setUserType(UserTypeEnum.ADMIN.getValue()));
            securityFrameworkUtilsMock.when(SecurityFrameworkUtils::getLoginUser).thenReturn(loginUser);
            // mock 方法（DeptDataPermissionRespDTO）
            DeptDataPermissionRespDTO deptDataPermission = new DeptDataPermissionRespDTO()
                    .setDeptIds(SetUtils.asSet(10L, 20L)).setSelf(true);
            when(permissionApi.getDeptDataPermission(same(1L))).thenReturn(success(deptDataPermission));

            // 调用
            Expression expression = rule.getExpression(tableName, tableAlias);
            // 断言
            assertSame(DeptDataPermissionRule.EXPRESSION_NULL, expression);
            assertSame(deptDataPermission, loginUser.getContext(DeptDataPermissionRule.CONTEXT_KEY, DeptDataPermissionRespDTO.class));
        }
    }

    @Test // 拼接 Dept 和 User 的条件（self 符合）
    public void testGetExpression_noDeptColumn_yesSelfColumn() {
        try (MockedStatic<SecurityFrameworkUtils> securityFrameworkUtilsMock
                     = mockStatic(SecurityFrameworkUtils.class)) {
            // 准备参数
            String tableName = "t_user";
            Alias tableAlias = new Alias("u");
            // mock 方法（LoginUser）
            LoginUser loginUser = RandomUtils.randomPojo(LoginUser.class, o -> o.setId(1L)
                    .setUserType(UserTypeEnum.ADMIN.getValue()));
            securityFrameworkUtilsMock.when(SecurityFrameworkUtils::getLoginUser).thenReturn(loginUser);
            // mock 方法（DeptDataPermissionRespDTO）
            DeptDataPermissionRespDTO deptDataPermission = new DeptDataPermissionRespDTO()
                    .setSelf(true);
            when(permissionApi.getDeptDataPermission(same(1L))).thenReturn(success(deptDataPermission));
            // 添加 user 字段配置
            rule.addUserColumn("t_user", "id");

            // 调用
            Expression expression = rule.getExpression(tableName, tableAlias);
            // 断言
            assertEquals("u.id = 1", expression.toString());
            assertSame(deptDataPermission, loginUser.getContext(DeptDataPermissionRule.CONTEXT_KEY, DeptDataPermissionRespDTO.class));
        }
    }

    @Test // 拼接 Dept 和 User 的条件（dept 符合）
    public void testGetExpression_yesDeptColumn_noSelfColumn() {
        try (MockedStatic<SecurityFrameworkUtils> securityFrameworkUtilsMock
                     = mockStatic(SecurityFrameworkUtils.class)) {
            // 准备参数
            String tableName = "t_user";
            Alias tableAlias = new Alias("u");
            // mock 方法（LoginUser）
            LoginUser loginUser = RandomUtils.randomPojo(LoginUser.class, o -> o.setId(1L)
                    .setUserType(UserTypeEnum.ADMIN.getValue()));
            securityFrameworkUtilsMock.when(SecurityFrameworkUtils::getLoginUser).thenReturn(loginUser);
            // mock 方法（DeptDataPermissionRespDTO）
            DeptDataPermissionRespDTO deptDataPermission = new DeptDataPermissionRespDTO()
                    .setDeptIds(CollUtil.newLinkedHashSet(10L, 20L));
            when(permissionApi.getDeptDataPermission(same(1L))).thenReturn(success(deptDataPermission));
            // 添加 dept 字段配置
            rule.addDeptColumn("t_user", "dept_id");

            // 调用
            Expression expression = rule.getExpression(tableName, tableAlias);
            // 断言
            assertEquals("u.dept_id IN (10, 20)", expression.toString());
            assertSame(deptDataPermission, loginUser.getContext(DeptDataPermissionRule.CONTEXT_KEY, DeptDataPermissionRespDTO.class));
        }
    }

    @Test // 拼接 Dept 和 User 的条件（dept + self 符合）
    public void testGetExpression_yesDeptColumn_yesSelfColumn() {
        try (MockedStatic<SecurityFrameworkUtils> securityFrameworkUtilsMock
                     = mockStatic(SecurityFrameworkUtils.class)) {
            // 准备参数
            String tableName = "t_user";
            Alias tableAlias = new Alias("u");
            // mock 方法（LoginUser）
            LoginUser loginUser = RandomUtils.randomPojo(LoginUser.class, o -> o.setId(1L)
                    .setUserType(UserTypeEnum.ADMIN.getValue()));
            securityFrameworkUtilsMock.when(SecurityFrameworkUtils::getLoginUser).thenReturn(loginUser);
            // mock 方法（DeptDataPermissionRespDTO）
            DeptDataPermissionRespDTO deptDataPermission = new DeptDataPermissionRespDTO()
                    .setDeptIds(CollUtil.newLinkedHashSet(10L, 20L)).setSelf(true);
            when(permissionApi.getDeptDataPermission(same(1L))).thenReturn(success(deptDataPermission));
            // 添加 user 字段配置
            rule.addUserColumn("t_user", "id");
            // 添加 dept 字段配置
            rule.addDeptColumn("t_user", "dept_id");

            // 调用
            Expression expression = rule.getExpression(tableName, tableAlias);
            // 断言
            assertEquals("(u.dept_id IN (10, 20) OR u.id = 1)", expression.toString());
            assertSame(deptDataPermission, loginUser.getContext(DeptDataPermissionRule.CONTEXT_KEY, DeptDataPermissionRespDTO.class));
        }
    }

}
