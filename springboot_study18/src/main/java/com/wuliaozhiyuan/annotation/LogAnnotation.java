package com.wuliaozhiyuan.annotation;

import java.lang.annotation.Documented;   
import java.lang.annotation.ElementType;   
import java.lang.annotation.Retention;   
import java.lang.annotation.RetentionPolicy;   
import java.lang.annotation.Target;   
/**
 * 日志注解，再方法上定义此注解，记录日志
 * @author wuliaozhiyuan
 *
 */
//自定义注解相关设置
@Target({ElementType.METHOD})   
@Retention(RetentionPolicy.RUNTIME)   
@Documented 
public @interface LogAnnotation {   

	//自定义注解的属性，default是设置默认值
    String value() default "无描述信息";   
}  