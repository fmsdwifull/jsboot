package com.nice.mapper;

import com.nice.model.SysRole;

import java.util.List;

public interface SysUserRoleMapper {
    List<SysRole> getUserRolesById(Long userId);
}
