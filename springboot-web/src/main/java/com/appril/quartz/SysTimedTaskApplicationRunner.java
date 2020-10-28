package com.appril.quartz;


import com.appril.mapper.SysTimedTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动时，自启动
 */
@Component
public class SysTimedTaskApplicationRunner implements ApplicationRunner {


    public static final Integer VALID_TASK_STATUS = 1;
    public static final Integer INVALID_TASK_STATUS = 2;

    @Autowired
    private SysTimedTaskMapper timedTaskMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
//            QueryWrapper<SysTimedTask> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().eq(SysTimedTask::getTaskStatus, VALID_TASK_STATUS).eq(SysTimedTask::getIsDel, 0);
//            List<SysTimedTask> sysTimedTasks = timedTaskMapper.selectList(queryWrapper);
//            if (CollectionUtil.isNotEmpty(sysTimedTasks)) {
//                sysTimedTasks.forEach(task -> {
//                    if (!StrUtil.isEmptyIfStr(task.getDateCron())) {
//                        QuartzManager.addJob(task.getJobName(), task.getJobGroupName(), task.getTriggerName(), task.getTriggerGroupName(), SysTimedTaskJob.class, task.getDateCron());
//                    }
//                });
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
