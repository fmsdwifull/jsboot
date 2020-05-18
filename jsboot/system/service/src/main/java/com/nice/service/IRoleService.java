package com.nice.service;

import com.nice.model.SysUser;

import java.util.Set;

public interface IRoleService {
    Set<String> getRolesByUid(Long uid);
}
