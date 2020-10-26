package com.appril.controller;

import com.appril.service.SysUserService;
import com.appril.entity.SysUser;
import com.appril.utils.ApiResult;
import com.appril.utils.HuToolUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/sysUser")
@Api(tags = "用户管理相关接口")
public class SysUserController {
    @Autowired
    public SysUserService sysUserService;

    /**
     * 保存、修改 【区分id即可】
     *
     * @param sysUser 传递的实体
     * @return ApiResult转换结果
     */
    @ApiOperation(value = "添加用户的接口", notes = "保存、修改用户信息")
    @PostMapping("/save")
    public ApiResult save(@RequestBody SysUser sysUser) {
        try {
            if (sysUser.getId() != null) {
                sysUser.setMdfTm(new Date());
                sysUserService.updateById(sysUser);
            } else {
                Date now = new Date();
                sysUser.setId(HuToolUtils.getId());
                sysUser.setCrtTm(now);
                sysUser.setMdfTm(now);
                sysUser.setIsDel(0);
                sysUser.setIsValid(0);
                sysUserService.save(sysUser);
            }
            return ApiResult.isOkMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("保存对象失败！" + e.getMessage());
        }
    }

    //删除对象信息
    @ApiOperation(value = "根据id删除用户", notes = "用户删除")
    @ApiImplicitParam(name = "id", value = "用户ID",  paramType = "path", required = true, dataType =  "Long")
    @PostMapping("/{id}")
    public ApiResult delete(@PathVariable("id") Long id) {
        try {
            sysUserService.removeById(id);
            /*SysUser sysUser = new SysUser();
            sysUser.setId(id);
            sysUser.setIsDel(1);
             sysUserService.updateById(sysUser);*/
            return ApiResult.isOkMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("删除对象失败！" + e.getMessage());
        }
    }

    //获取
    @GetMapping("/{id}")
    public ApiResult get(@PathVariable("id") Long id) {

        return ApiResult.isOkNoToken("查询成功", sysUserService.getById(id));
    }


    //查看
    @GetMapping("/list")
    public ApiResult list(@RequestBody SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        return ApiResult.isOkNoToken("查询成功", sysUserService.list(queryWrapper));
    }


    //分页查看
    @GetMapping("/pageList")
    public ApiResult pageList(@RequestBody SysUser sysUser) {
        Page<SysUser> userPage = new Page<>(1, 10);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        Page<SysUser> page = sysUserService.page(userPage, queryWrapper);
        return ApiResult.isOkNoToken("查询成功", page);
    }
}