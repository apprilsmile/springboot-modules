package com.appril.controller;

import com.appril.biz.OutsideServiceBiz;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private OutsideServiceBiz OutsideServiceBiz;

    @GetMapping("/query")
    public String query(){
        return OutsideServiceBiz.query();
    }


}
