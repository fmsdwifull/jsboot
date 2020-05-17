package com.nice.mapper;

import com.nice.model.SysUser;

public interface SysUserMapper {
    SysUser loadUserByUsername(String username);
}
