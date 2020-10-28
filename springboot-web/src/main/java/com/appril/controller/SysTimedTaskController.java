package com.appril.controller;

import cn.hutool.core.util.StrUtil;
import com.appril.entity.SysTimedTask;
import com.appril.quartz.QuartzManager;
import com.appril.quartz.job.SysTimedTaskJob;
import com.appril.service.SysTimedTaskIService;
import com.appril.utils.ApiResult;
import com.appril.utils.HuToolUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/sysTimedTask")
public class SysTimedTaskController {
    @Autowired
    public SysTimedTaskIService sysTimedTaskService;

    /**
     * 保存、修改 【区分id即可】
     *
     * @param sysTimedTask 传递的实体
     * @return ApiResult转换结果
     */
    @PostMapping("/save")
    public ApiResult save(@RequestBody SysTimedTask sysTimedTask) {
        try {
            if (sysTimedTask.getId() != null) {
                SysTimedTask timedTask = sysTimedTaskService.getById(sysTimedTask.getId());
                sysTimedTask.setMdfTm(new Date());
                sysTimedTaskService.updateById(sysTimedTask);
                if (1==timedTask.getTaskStatus()){//启用
                    if (2==sysTimedTask.getTaskStatus()){
                        QuartzManager.removeJob(sysTimedTask.getJobName(),sysTimedTask.getJobGroupName(),sysTimedTask.getTriggerName(),sysTimedTask.getTriggerGroupName());
                    }else {
                        if (!StrUtil.isBlankIfStr(sysTimedTask.getDateCron())&&!sysTimedTask.getDateCron().equals(timedTask.getDateCron())){
                            QuartzManager.modifyJobTime(sysTimedTask.getJobName(),sysTimedTask.getJobGroupName(),sysTimedTask.getTriggerName(),sysTimedTask.getTriggerGroupName(),sysTimedTask.getDateCron());
                        }
                    }
                }else {//终止
                    if (1==sysTimedTask.getTaskStatus()){
                        QuartzManager.addJob(sysTimedTask.getJobName(),sysTimedTask.getJobGroupName(),sysTimedTask.getTriggerName(),sysTimedTask.getTriggerGroupName(), SysTimedTaskJob.class,sysTimedTask.getDateCron());
                    }
                }
            } else {
                Date now = new Date();
                sysTimedTask.setId(HuToolUtils.getId());
                sysTimedTask.setCrtTm(now);
                sysTimedTask.setMdfTm(now);
                sysTimedTask.setIsDel(0);
                sysTimedTaskService.save(sysTimedTask);
                QuartzManager.addJob(sysTimedTask.getJobName(),sysTimedTask.getJobGroupName(),sysTimedTask.getTriggerName(),sysTimedTask.getTriggerGroupName(), SysTimedTaskJob.class,sysTimedTask.getDateCron());
            }
            return ApiResult.isOkMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("保存对象失败！" + e.getMessage());
        }
    }

    //删除对象信息
    @PostMapping("/{id}")
    public ApiResult delete(@PathVariable("id") Long id) {
        try {
            sysTimedTaskService.removeById(id);
            /*SysTimedTask sysTimedTask = new SysTimedTask();
            sysTimedTask.setId(id);
            sysTimedTask.setIsDel(1);
             sysTimedTaskService.updateById(sysTimedTask);*/
            return ApiResult.isOkMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.isErrMessage("删除对象失败！" + e.getMessage());
        }
    }

    //获取
    @GetMapping("/{id}")
    public ApiResult get(@PathVariable("id") Long id) {

        return ApiResult.isOkNoToken("查询成功", sysTimedTaskService.getById(id));
    }


    //查看
    @GetMapping("/list")
    public ApiResult list(@RequestBody SysTimedTask sysTimedTask) {
        QueryWrapper<SysTimedTask> queryWrapper = new QueryWrapper<>();
        return ApiResult.isOkNoToken("查询成功", sysTimedTaskService.list(queryWrapper));
    }


    //分页查看
    @GetMapping("/pageList")
    public ApiResult pageList(@RequestBody SysTimedTask sysTimedTask) {
        Page<SysTimedTask> queryPage = new Page<>(1, 10);
        QueryWrapper<SysTimedTask> queryWrapper = new QueryWrapper<>();
        Page<SysTimedTask> page = sysTimedTaskService.page(queryPage, queryWrapper);
        return ApiResult.isOkNoToken("查询成功", page);
    }
}