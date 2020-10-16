package com.appril.controller;

import com.appril.entity.SysUser;
import com.appril.utils.ApiResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyang
 * @date 2020/10/16
 * @description
 **/
@RestController
public class LoginController {

    @PostMapping("/login")
    public ApiResult login(@RequestBody SysUser sysUser){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(sysUser.getUsrNm(), sysUser.getPwd());

        //用Subject对象执行登录方法，没有抛出任何异常说明登录成功
        try {
            subject.login(token);
            return ApiResult.isOkMessage("登录成功");
        }  catch (IncorrectCredentialsException e) {
            return ApiResult.isErrMessage(e.getMessage());
        }
    }
    @PostMapping("/logout")
    public ApiResult logout(@RequestBody SysUser sysUser){
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout();
            return ApiResult.isOkMessage("退出登录成功");
        }catch (Exception e){
            return ApiResult.isErrMessage("退出登录失败");
        }
    }
}
