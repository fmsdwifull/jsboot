package com.nice.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nice.model.*;
import com.nice.service.impl.CustomTokenService;
import com.nice.service.impl.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
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
    @Autowired
    CustomTokenService tokenService;
    //@Autowired
    //AuthenticationManager authenticationManager;

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
                .antMatchers("/gethellomodule");
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
                //.logoutUrl("/my/logout")
                .logoutSuccessUrl("/logout")
                .logoutSuccessHandler((req, resp, authentication) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new ObjectMapper().writeValueAsString(AjaxResult.success("注销成功!")));
                            out.flush();
                            out.close();
                        }
                )
                .permitAll()
                .and()
                .csrf().disable();
//                .exceptionHandling()
//                //没有认证时，在这里处理结果，不要重定向
//                .authenticationEntryPoint((req, resp, authException) -> {
//                            resp.setContentType("application/json;charset=utf-8");
//                            resp.setStatus(401);
//                            PrintWriter out = resp.getWriter();
//                            RespBean respBean = RespBean.error("访问失败!");
//                            if (authException instanceof InsufficientAuthenticationException) {
//                                respBean.setMsg("请求失败，请联系管理员!");
//                            }
//                            out.write(new ObjectMapper().writeValueAsString(respBean));
//                            out.flush();
//                            out.close();
//                        }
//                );
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
                    LoginUser loginUser = (LoginUser) authentication.getPrincipal();

                    //保存token
                    String token = tokenService.createToken(loginUser);
                    HashMap hashMap = new HashMap();
                    hashMap.put("token",token);
                    AjaxResult ajax = AjaxResult.success(hashMap);
                    String s = new ObjectMapper().writeValueAsString(ajax);
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
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:9527"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //super.configure(auth);
//        //auth.userDetailsService(customUserService).passwordEncoder(new BCryptPasswordEncoder());
//        auth.authenticationProvider(customUsernamePasswordAuthentionProvider);
//    }

}
