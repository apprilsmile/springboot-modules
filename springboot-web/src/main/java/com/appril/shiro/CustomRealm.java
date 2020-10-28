package com.appril.shiro;

import com.appril.entity.SysUser;
import com.appril.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangyang
 * @date 2020/10/16
 * @description
 **/
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> stringSet = new HashSet<>();
        stringSet.add("user:show");
        stringSet.add("user:admin");
        info.setStringPermissions(stringSet);
        return info;
    }

    /**
     * 这里可以注入userService,为了方便演示，我就写死了帐号了密码
     * private UserService userService;
     * <p>
     * 获取即将需要认证的信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String usrNm = (String) authenticationToken.getPrincipal();
        String pwd = new String((char[]) authenticationToken.getCredentials());
        //根据用户名从数据库获取密码
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUsrNm,usrNm).eq(SysUser::getPwd,pwd);
        SysUser sysUser = sysUserService.getOne(wrapper);
        if ( sysUser== null) {
            throw new IncorrectCredentialsException("用户名或密码不正确");
        }
        return new SimpleAuthenticationInfo(usrNm, pwd,getName());
    }
}