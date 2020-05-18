package com.nice.web.controller;

import com.nice.common.ServletUtils;
import com.nice.model.*;
import com.nice.service.IMenuService;
import com.nice.service.IRoleService;
import com.nice.service.impl.CustomTokenService;
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
    @Autowired
    private IRoleService roleService;

    @Autowired
    CustomTokenService customTokenService;
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
    @GetMapping("/getinfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = customTokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = roleService.getRolesByUid(user.getUserId());
        // 权限集合
        Set<String> menus = menuService.getMenusByUid(user.getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("menus", menus);
        return ajax;
    }
}
