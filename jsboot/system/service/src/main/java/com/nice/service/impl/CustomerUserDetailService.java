package com.nice.service.impl;

import com.nice.mapper.SysUserMapper;
import com.nice.mapper.SysUserRoleMapper;
import com.nice.model.LoginUser;
import com.nice.model.SysRole;
import com.nice.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomerUserDetailService  implements UserDetailsService {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;
    //@Autowired
    private SysUser sysUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //UserDetails userDetails = null;
        sysUser = sysUserMapper.loadUserByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        try {
            ;//Collection<GrantedAuthority> authList = getAuthorities();
            //userDetails = new User(sysUser.getUserName(), sysUser.getPassword(), authList);
        }catch (Exception e) {
            e.printStackTrace();
        }
        //这里最终返回的是用户信息，和角色信息
        return new LoginUser(sysUser);
        //return userDetails;
    }

    private Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        List<SysRole> sysRoles = sysUserRoleMapper.getUserRolesById(sysUser.getUserId());
        sysUser.setRoles(sysRoles);
        for (SysRole sysRole:sysRoles)
        {
            authList.add(new SimpleGrantedAuthority(sysRole.getRoleName()));
        }
        return authList;
    }
}
