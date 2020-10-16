package com.appril.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhangyang
 * @date 2020/10/16
 * @description
 **/
@Controller
public class PageController {
    @GetMapping("/index")
    public String index(){
        return "index"; //当浏览器输入/index时，会返回 /static/index.html页面
    }

    @GetMapping("/403")
    public String to403(){
        return "403";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
