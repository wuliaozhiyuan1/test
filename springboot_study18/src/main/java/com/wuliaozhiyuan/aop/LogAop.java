package com.wuliaozhiyuan.aop;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.wuliaozhiyuan.annotation.LogAnnotation;
import com.wuliaozhiyuan.bean.shiro.SysUser;
import com.wuliaozhiyuan.bean.system.Log;
import com.wuliaozhiyuan.service.system.LogService;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Tools;

/**
 * 日志记录aop
 * @author wuliaozhiyuan
 *
 */
@Aspect
@Component
@Order(98)
public class LogAop {
	
	@Autowired
	private LogService logService;
	
	@Around("execution(* com.wuliaozhiyuan.controller.*.*.*(..)) && @annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, LogAnnotation logAnnotation) throws Throwable {
		//记录日志
		String message = logAnnotation.value();
		String ip = Tools.getIpAddr();
		SysUser user = (SysUser)Tools.getSession().getAttribute("user");
		//是否已经认证，session中有user则代表已经认证。因为第一次访问首页之前，还没有认证，session中还没有user对象
		boolean authorized = user != null;
		if(authorized){
			this.save(user, message, ip);
		}
		Object result = null;
		try {
        	result = joinPoint.proceed();
		} catch (Throwable e) {
			throw e;
		}
		if(!authorized){
			user = (SysUser)Tools.getSession().getAttribute("user");
			this.save(user, message, ip);
		}
		return result;
    }
	public void save(SysUser user, String message, String ip) throws IllegalArgumentException, IllegalAccessException{
		String username = user.getUsername();
		String name = user.getName();
		long uid = user.getUid();
		Date data = new Date();
		Log log = new Log(username,name, uid, message, data,ip);
		PageData pageData = PageData.getCommanInstance(8);
		pageData.addObjectField(log);
		logService.save(pageData);
	}
}
