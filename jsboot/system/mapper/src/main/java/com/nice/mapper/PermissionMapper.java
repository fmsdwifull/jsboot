package com.nice.mapper;

import com.nice.model.Permission;

import java.util.List;

public interface PermissionMapper {
    List<Permission> getAllPermissionsWithRole();
}
