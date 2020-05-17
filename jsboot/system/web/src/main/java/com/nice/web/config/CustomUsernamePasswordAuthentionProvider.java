package com.nice.web.config;

import com.nice.mapper.SysUserMapper;
import com.nice.service.impl.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//@Service
//@Component
public class CustomUsernamePasswordAuthentionProvider  implements AuthenticationProvider {
    @Autowired(required=false)
    private SysUserMapper userMapper;
    @Autowired
    private CustomerUserDetailService userDetailsService;

    //authentication
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //String dbuser = "lisi";
        //String dbpwd = "234";
        //User user = userMapper.getUserByPermiss(username,password);
        System.out.print("------------------------------"+userDetails.getPassword());
        if(userDetails.getPassword().equals(password))
        //if(user!=null)
        //if(username.equals(dbuser)&password.equals(dbpwd))
        {
            return new UsernamePasswordAuthenticationToken(username,password,userDetails.getAuthorities());
        }else{
            throw new BadCredentialsException("无效的密码");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
