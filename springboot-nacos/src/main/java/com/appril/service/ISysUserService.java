package com.appril.service;

import com.appril.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author appril
 * @since 2020-09-25
 */
public interface ISysUserService extends IService<SysUser> {

    Page<SysUser> getCustomizedPageList(SysUser sysUser, Page<SysUser> userPage);
}
