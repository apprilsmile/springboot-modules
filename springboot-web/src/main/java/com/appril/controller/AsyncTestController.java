package com.appril.controller;

import com.appril.service.IAsyncService;
import com.appril.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyang
 * @date 2020/10/22
 * @description 启动类 @EnableAsync 开启@Async注解
 **/
@RestController
@RequestMapping("/async")
public class AsyncTestController {

    @Autowired
    private IAsyncService asyncService;

    @GetMapping("/threadPoolTaskTest")
    public ApiResult asyncTest(){
        try {
            asyncService.sendMessage();
            asyncService.sendEmail();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.isOkMessage("测试成功");
    }
}
