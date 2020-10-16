package com.appril.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author zhangyang
 * @date 2020/10/16
 * @description
 **/
@Aspect
@Component
public class SysLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    /**
     * 这里我们使用注解的形式
     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...)
     */
    @Pointcut("execution(* com.appril.controller..*.*(..))")
    public void logPointCut() {}

    /**
     * 环绕通知 @Around  ， 当然也可以使用 @Before (前置通知)  @After (后置通知)
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        saveRequestLog(point);
        Object result = point.proceed();
        long endTime = System.currentTimeMillis();
        saveResponseLog(result,endTime-beginTime);

        return result;
    }

    /**
     * 输出请求日志
     * @param
     * @param
     */
    private void saveRequestLog(ProceedingJoinPoint point) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getContextPath() + request.getRequestURI();
        Object[] args = point.getArgs();
        logger.info("currentTime:{},url:{},params:{}", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"),url, JSONUtil.toJsonStr(args));
    }
    /**
     * 输出响应日志
     * @param
     * @param time
     */
    private void saveResponseLog(Object returnValue, long time) throws  Throwable{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getContextPath() + request.getRequestURI();
        logger.info("currentTime:{},url:{},,response:{},请求耗时:{} ms",DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"),url, JSONUtil.toJsonStr(returnValue),time);
    }
}
