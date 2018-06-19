package com.wuliaozhiyuan.aop;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliaozhiyuan.util.CustomException;
import com.wuliaozhiyuan.util.Result;
import com.wuliaozhiyuan.util.Tools;
/**
 * 处理异常的aop，@responseBody异常返回json，其他返回error页面
 * @author wuliaozhiyuan
 *
 */
@Aspect
@Component
@Order(1)
public class ExceptionHandleAop {

    @Around("execution(* com.wuliaozhiyuan.controller.*.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws NoSuchMethodException, SecurityException {
        Signature signature = joinPoint.getSignature();    
        MethodSignature methodSignature = (MethodSignature)signature;    
        Method targetMethod = methodSignature.getMethod();  
        Object result = null;
        try {
//        	//捕捉BindResult异常
        	BindingResult bindingResult = null;
            for(Object arg:joinPoint.getArgs()){
                if(arg instanceof BindingResult){
                    bindingResult = (BindingResult) arg;
                }
            }
            if(bindingResult != null){
            	if(bindingResult.hasErrors()){
            		for(FieldError fieldError : bindingResult.getFieldErrors()){
            			Tools.getLogger().error(fieldError.getDefaultMessage());
            		};
            		throw new CustomException("参数异常");
            	}
            }
            result = joinPoint.proceed();
        } catch (Throwable e) {
        	Tools.getLogger().error("in exceptionHandleAop ");
        	StringWriter sw = new StringWriter();
        	PrintWriter pw = new PrintWriter(sw);
        	e.printStackTrace(pw);
        	Tools.getLogger().error(sw.toString());
            if(targetMethod.isAnnotationPresent(ResponseBody.class)){
            	if(e instanceof org.apache.shiro.authz.UnauthorizedException){
            		//无权操作
            		return Result.build("您无权操作", null, false);
            	}else if(e instanceof CustomException){
            		return Result.build(e.getMessage(), null, false);
            	}
            	return Result.build("系统繁忙", null, false);
            }else{
            	if(e instanceof org.apache.shiro.authz.UnauthorizedException){
            		//无权操作
            		return "notAuthorized";
            	}else if(e instanceof CustomException){
            		Tools.getPageData().put("msg", e.getMessage());
            	}
                return "error";
            }
        }
        return result;
    }
}
