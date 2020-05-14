package com.nice.mapper;

import com.nice.model.Role;
import com.nice.model.User;

import java.util.List;


public interface UserMapper {
    User loadUserByUsername(String username);

    List<Role> getUserRolesById(Integer id);
}
