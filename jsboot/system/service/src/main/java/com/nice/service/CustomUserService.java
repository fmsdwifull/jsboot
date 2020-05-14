package com.nice.service;

import com.nice.mapper.UserMapper;
import com.nice.mapper.UserRoleMapper;
import com.nice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService{
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username){
        //这里不知道行不行，返回要求UserDetails，但我返回的是自己定义的User，也就是返回的子类对象，应该可以
        User User = userMapper.loadUserByUsername(username);
        if (User == null) {
            throw  new UsernameNotFoundException("用户名不存在!");
        }
        User.setRoles(userMapper.getUserRolesById(User.getId()));
        return User;
    }

    public String enPassoWord(String pass) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return  encoder.encode(pass);
    }
}
