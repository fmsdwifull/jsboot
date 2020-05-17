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

@RestController
public class RbacController {
    @Autowired
    private IMenuService menuService;

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public AjaxResult getRouters()
    {
        Long uid = ((SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(uid);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
