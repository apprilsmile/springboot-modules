package com.appril.quartz.job;

import com.appril.quartz.ApplicationContextHelper;
import com.appril.service.SysTimedTaskIService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysTimedTaskJob implements Job {

    Logger logger = LoggerFactory.getLogger(SysTimedTaskJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        String jobName = key.getName();
        String jobGroupName = key.getGroup();
        SysTimedTaskIService sysTimedTaskIService = (SysTimedTaskIService) ApplicationContextHelper.getBean("sysTimedTaskServiceImpl");
        logger.info("Quartz测试定时任务启动：jobName ===>" + jobName + "; jobGroupName===>" + jobGroupName);
        sysTimedTaskIService.simpleTest();
    }
}

