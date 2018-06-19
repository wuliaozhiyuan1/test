package com.wuliaozhiyuan.aop;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.wuliaozhiyuan.bean.shiro.SysPermission;
import com.wuliaozhiyuan.bean.shiro.SysUser;
import com.wuliaozhiyuan.util.Tools;

/**
 * 权限aop：负责管理所有菜单下的增删改查权限，每个Shiro的requirePermission注解的方法都会被拦截，然后，查询该权限方法下的增删改查权限
 * @author wuliaozhiyuan
 *
 */
@Aspect
@Component
@Order(99)
public class SysPermissionAop {

	@Around("execution(* com.wuliaozhiyuan.controller.*.*.*(..)) && @annotation(requiresPermissions)")
    public Object around(ProceedingJoinPoint joinPoint, RequiresPermissions requiresPermissions) throws Throwable {
        Signature signature = joinPoint.getSignature();    
        MethodSignature methodSignature = (MethodSignature)signature;    
        Method targetMethod = methodSignature.getMethod();  
//        Method realMethod = joinPoint.getTarget().getClass()
//                .getDeclaredMethod(signature.getName(), targetMethod.getParameterTypes());
//        Class clazz = realMethod.getClass();
        	Object result = null;
        	Tools.getLogger().info("in SysPermissionAop");
            try {
            	result = joinPoint.proceed();
			} catch (Throwable e) {
				throw e;
			}
        	//查询该方法下的增删改查权限
            Tools.getLogger().info("SysPermissionAop:queryCrudPermissioni");
            Tools.getLogger().info(requiresPermissions.value()[0]);
            String permissionStr = requiresPermissions.value()[0];
            String permissioinPrefix = permissionStr.split(":")[0];
            String viewPermission = permissioinPrefix + ":view";
            String addPermission = permissioinPrefix + ":add";
            String updatePermission = permissioinPrefix + ":update";
            String deletePermission = permissioinPrefix + ":delete";
            SysUser sysUser = (SysUser)Tools.getSession().getAttribute("user");
            Map<String, SysPermission> crudButtonPermissions = sysUser.getCrudButtonPermissions();
            Map<String, Boolean> crudButtonPermission = Tools.newHashMapWithExpectedSize(4);
            Tools.getPageData();
            if(crudButtonPermissions.get(viewPermission) != null){
            	crudButtonPermission.put("view", true);
            }else{
            	crudButtonPermission.put("view", false);
            }
            if(crudButtonPermissions.get(addPermission) != null){
            	crudButtonPermission.put("add", true);
            }else{
            	crudButtonPermission.put("add", false);
            }
            if(crudButtonPermissions.get(deletePermission) != null){
            	crudButtonPermission.put("delete", true);
            }else{
            	crudButtonPermission.put("delete", false);
            }
            if(crudButtonPermissions.get(updatePermission) != null){
            	crudButtonPermission.put("update", true);
            }else{
            	crudButtonPermission.put("update", false);
            }
            Tools.getHttpRequest().setAttribute("crudButtonPermission", crudButtonPermission);
            return result;
    }
}
