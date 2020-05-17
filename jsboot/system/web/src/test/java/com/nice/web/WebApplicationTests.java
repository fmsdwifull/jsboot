package com.nice.web;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@MapperScan(basePackages = "com.nice.mapper")
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.nice.service","com.nice.web"})
class WebApplicationTests {
    @Test
    void contextLoads() {
    }
    //@Test
    //public void menuTest(){
     //   System.out.print(rbacService.getPermissionByUid(3));
    //}


}
