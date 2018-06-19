package com.wuliaozhiyuan.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.wuliaozhiyuan.util.Tools;
/**
 * 请求前后初始化或者处理一些数据
 * @author wuliaozhiyuan
 *
 */
@Aspect
@Component
@Order(99)
public class InitializeDataAop {

    @Around("execution(* com.wuliaozhiyuan.controller.*.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        result = joinPoint.proceed();
        //每个请求后，都把PageData传送给前台展示层
        Tools.getHttpRequest().setAttribute("pd", Tools.getPageData());
        return result;
    }
}
