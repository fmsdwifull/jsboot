package com.nice.service.impl;

import com.nice.common.StringUtils;
import com.nice.mapper.SysUserRoleMapper;
import com.nice.model.SysRole;
import com.nice.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Set<String> getRolesByUid(Long uid) {
        Set<String> roleSet = new HashSet<>();

        List<SysRole> sysRoles = sysUserRoleMapper.getUserRolesById(uid);
        for (SysRole sysRole : sysRoles)
        {
            if (StringUtils.isNotNull(sysRole))
            {
                roleSet.addAll(Arrays.asList(sysRole.getRoleKey().trim().split(",")));
            }
        }
        return roleSet;
    }
}
