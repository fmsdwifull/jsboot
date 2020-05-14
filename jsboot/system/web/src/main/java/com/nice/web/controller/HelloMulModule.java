package com.nice.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nice.service.TestMulModule;

@RestController
public class HelloMulModule{
    @Autowired
    private TestMulModule testMulModule;

    @RequestMapping("/gethellomulmodule")
    public String getHelloMulModule()
    {
        return "getHelloMulModule";
    }

    @RequestMapping("/getservicetest")
    public String getServiceTest()
    {
        return testMulModule.getServiceTst();
    }
}