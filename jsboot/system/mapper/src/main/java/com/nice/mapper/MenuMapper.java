package com.nice.mapper;

import com.nice.model.SysMenu;

import java.util.List;
import java.util.Set;

public interface MenuMapper {
    List<SysMenu> getAllMenuWithRole();

    List<SysMenu> selectMenuTreeByUserId(Long userId);

    List<Integer> selectMenuListByRoleId(Long roleId);

    SysMenu checkMenuNameUnique(String menuName, Long parentId);

    List<SysMenu> selectMenuList(SysMenu menu);

    List<SysMenu> selectMenuListByUserId(SysMenu menu);

    Set<String> selectMenuPermsByUserId(Long userId);

    Set<String> getRolesByUid(Long userId);
}
