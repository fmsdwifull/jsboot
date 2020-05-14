package com.nice.service;

import com.nice.mapper.PermissionMapper;
import com.nice.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> getAllPermissionsWithRole() {
            return permissionMapper.getAllPermissionsWithRole();
    }
}
