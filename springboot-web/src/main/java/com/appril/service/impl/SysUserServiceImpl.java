package com.appril.service.impl;

import com.appril.entity.SysUser;
import com.appril.mapper.SysUserMapper;
import com.appril.service.ISysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author appril
 * @since 2020-09-25
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Page<SysUser> getCustomizedPageList(SysUser sysUser, Page<SysUser> userPage) {
        return sysUserMapper.getCustomizedPageList(sysUser, userPage);
    }
}
