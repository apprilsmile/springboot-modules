package com.appril.service.impl;

import com.appril.service.IAsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zhangyang
 * @date 2020/10/22
 * @description
 **/
@Service
public class AsyncServiceImpl implements IAsyncService {
    Logger log = LoggerFactory.getLogger(AsyncServiceImpl.class);

    // 发送提醒短信
    @Override
    @Async("taskExecutor")// 指定线程池，也可以直接写@Async
    public void sendMessage() throws InterruptedException {
        log.info("发送短信方法执行开始");
        Thread.sleep(5000); // 模拟耗时
        log.info("发送短信方法执行结束");
    }

    // 发送提醒邮件
    @Override
    @Async("taskExecutor") // 指定线程池，也可以直接写@Async
    public void sendEmail() throws InterruptedException {
        log.info("发送邮件方法执行开始");
        Thread.sleep(2000); // 模拟耗时
        log.info("发送邮件方法执行结束");
    }
}
