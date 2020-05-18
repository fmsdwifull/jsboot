package com.nice.mapper;

import com.nice.model.SysRole;

import java.util.List;
import java.util.Set;

public interface SysUserRoleMapper {
    //以前是List<SysRole>,这个查出来不是list吗？怎么会是string集合呢？
    List<SysRole> getUserRolesById(Long userId);
}
