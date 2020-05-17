package com.nice.service.impl;

import com.nice.mapper.SysUserMapper;
import com.nice.mapper.SysUserRoleMapper;
import com.nice.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailService  implements UserDetailsService {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.loadUserByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        sysUser.setRoles(sysUserRoleMapper.getUserRolesById(sysUser.getUserId()));
        return sysUser;
    }
}
