package com.nice.service.impl;

import com.nice.common.Constants;
import com.nice.model.LoginUser;
import com.nice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    CustomTokenService customTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;//不出意外，这里应该是空指针。配置了，应该不会空指针了。

    @Override
    public String doSigin(String username, String password) {
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                ;//这里如何获取异常类型？
            }
            else
            {
                ;
            }
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return customTokenService.createToken(loginUser);
    }
}
