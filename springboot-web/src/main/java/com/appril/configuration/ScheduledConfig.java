package com.appril.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhangyang
 * @date 2020/10/22
 * @description 启动类@EnableScheduling 开启定时任务
 **/
@Component
public class ScheduledConfig {

    Logger logger = LoggerFactory.getLogger(ScheduledConfig.class);

    /**
     * 字段　　允许值　　允许的特殊字符
     * 秒     　 0-59 　　　　, - * /
     * 分     　 0-59　　　　 , - * /
     * 小时      0-23 　　　　, - * /
     * 日期      1-31 　　　　, - * ? / L W C
     * 月份      1-12 　　　　, - * /
     * 星期      1-7 　　　　  , - * ? / L C #
     * 年     1970-2099 　　, - * /
     * “*”字符被用来指定所有的值。
     */
    @Scheduled(cron = "0/5 * * * * ? ")//每隔15秒执行一次
    @Async("taskExecutor")
    public void scheduleCornTest(){
        try{
            Thread.sleep(5000);  //睡眠5秒
            logger.info(Thread.currentThread().getName()+"-scheduleCornTest");

        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 指定两次任务（结束-开始）执行的时间间隔(毫秒)
     */
    @Scheduled(fixedDelay = 1000)
    @Async("taskExecutor")
    public void scheduleFixedDelayTest(){
        try{
            Thread.sleep(5000);  //睡眠5秒
            logger.info(Thread.currentThread().getName()+"-scheduleFixedDelayTest");

        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 指定两次任务（开始-开始）执行的时间间隔(毫秒)
     */
    @Scheduled(fixedRate = 1000)
    @Async("taskExecutor")
    public void scheduleFixedRateTest(){
        try{
            Thread.sleep(5000);  //睡眠5秒
            logger.info(Thread.currentThread().getName()+"-scheduleFixedRateTest");

        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
