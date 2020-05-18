package com.nice.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nice.model.AjaxResult;
import com.nice.model.RespJson;
import com.nice.model.SysUser;
import com.nice.model.User;
import com.nice.service.impl.CustomerUserDetailService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;
import java.util.*;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomerUserDetailService CustomerUserDetailService;
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;
    //@Autowired
    //CustomUsernamePasswordAuthentionProvider customUsernamePasswordAuthentionProvider;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(CustomerUserDetailService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode")
                .antMatchers("/gethellomulmodule");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .and()
                .logout()
                .logoutSuccessHandler((req, resp, authentication) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new ObjectMapper().writeValueAsString(RespJson.ok("注销成功!")));
                            out.flush();
                            out.close();
                        }
                )
                .permitAll()
                .and()
                .csrf().disable();
        //http.cors(withDefaults());
        //这个是要替代UsernamePasswordAuthenticationFilter吗？还是什么意思
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    //response.setHeader("Access-Control-Allow-Origin", "*");
                    //response.setHeader("Access-Control-Allow-Methods", "GET,POST");
                    PrintWriter out = response.getWriter();
                    SysUser sysUser = (SysUser) authentication.getPrincipal();
                    String username = sysUser.getUsername();

                    //sysUser.setPassword(null);
                    //authentication.getCredentials().
                    Collection<? extends GrantedAuthority> authorities = sysUser.getAuthorities();
                    Set<String> roles = new HashSet<>();

                    if (CollectionUtils.isNotEmpty(authorities)) {
                        for (GrantedAuthority authority : authorities) {
                            String roleName = authority.getAuthority();
                            roles.add(roleName);
                        }
                    }
                    //TokenService
                    //JwtTokenPair  jwtTokenPair = jwtTokenGenerator.jwtTokenPair(username, roles, null);

//                    RespJson ok = RespJson.ok("登录成功!", sysUser);
//                    String s = new ObjectMapper().writeValueAsString(ok);
                    HashMap hashMap = new HashMap();
                    hashMap.put("token","--SDFSKKKKK====SFFDSEEEEE");
                    hashMap.put("username",username);
                    AjaxResult ajax = AjaxResult.success(hashMap);
                    String s = new ObjectMapper().writeValueAsString(ajax);
//                  String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
//                            loginBody.getUuid());
//                    ajax.put(Constants.TOKEN, token);
                    out.write(s);
                    out.flush();
                    out.close();
                }
        );
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    //response.setHeader("Access-Control-Allow-Origin", "*");
                    //response.setHeader("Access-Control-Allow-Methods", "GET,POST");
                    PrintWriter out = response.getWriter();
                    RespJson respJson = RespJson.error(exception.getMessage());
                    if (exception instanceof LockedException) {
                        respJson.setMsg("账户被锁定，请联系管理员!");
                    } else if (exception instanceof CredentialsExpiredException) {
                        respJson.setMsg("密码过期，请联系管理员!");
                    } else if (exception instanceof AccountExpiredException) {
                        respJson.setMsg("账户过期，请联系管理员!");
                    } else if (exception instanceof DisabledException) {
                        respJson.setMsg("账户被禁用，请联系管理员!");
                    } else if (exception instanceof BadCredentialsException) {
                        respJson.setMsg("用户名或者密码输入错误，请重新输入!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(respJson));
                    out.flush();
                    out.close();
                }
        );
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/dologin");
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        loginFilter.setSessionAuthenticationStrategy(sessionStrategy);
        return loginFilter;
    }

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:9527"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //super.configure(auth);
//        //auth.userDetailsService(customUserService).passwordEncoder(new BCryptPasswordEncoder());
//        auth.authenticationProvider(customUsernamePasswordAuthentionProvider);
//    }

}
