package com.nice.web.controller;

import com.nice.common.ServletUtils;
import com.nice.model.*;
import com.nice.service.IMenuService;
import com.nice.service.IRoleService;
import com.nice.service.impl.CustomTokenService;
import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        System.out.print("------user-----------"+ user);
        // 角色集合
        Set<String> roles = roleService.getRolesByUid(user.getUserId());
        System.out.print("------roles-----------"+ roles);
        // 权限集合
        Set<String> menus = menuService.getMenusByUid(user.getUserId());
        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        hashMap.put("user", user);
        hashMap.put("roles", roles);
        hashMap.put("menus", menus);
        AjaxResult ajax = AjaxResult.success(hashMap);
        return ajax;
    }
}
