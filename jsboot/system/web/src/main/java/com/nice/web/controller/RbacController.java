package com.nice.web.controller;

import com.nice.model.AjaxResult;
import com.nice.model.SysMenu;
import com.nice.model.SysUser;
import com.nice.model.User;
import com.nice.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class RbacController {
    @Autowired
    private IMenuService menuService;

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getrouters")
    public AjaxResult getRouters()
    {
        Long uid = ((SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(uid);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }
}
