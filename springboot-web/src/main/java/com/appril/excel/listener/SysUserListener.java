package com.appril.excel.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.appril.entity.SysUser;
import com.appril.excel.dto.SysUserEasyDto;
import com.appril.service.SysUserService;

import java.util.List;

/**
 * @author zhangyang
 * @date 2020/10/21
 * @description
 **/
public class SysUserListener extends AnalysisEventListener<SysUserEasyDto> {

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private SysUserService userService;

    public SysUserListener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param sysUserService
     */
    public SysUserListener(SysUserService sysUserService) {
        this.userService = sysUserService;
    }
    /**
     * 这个每一条数据解析都会来调用
     *
     *
     *  one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     *
     */
    @Override
    public void invoke(SysUserEasyDto sysUserEasyDto, AnalysisContext analysisContext) {
//        System.out.println("********"+JSONUtil.toJsonStr(sysUserEasyDto));
        List<SysUser> list = userService.list();
        System.out.println("********"+JSONUtil.toJsonStr(sysUserEasyDto));
    }


    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        userService.saveBatch();
    }
}
