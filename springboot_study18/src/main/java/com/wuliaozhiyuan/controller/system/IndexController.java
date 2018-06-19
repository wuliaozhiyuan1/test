package com.wuliaozhiyuan.controller.system;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wuliaozhiyuan.annotation.LogAnnotation;
import com.wuliaozhiyuan.service.system.SysUserService;
/**
 * 首页、登录
 * @author wuliaozhiyuan
 *
 */
@Controller
public class IndexController {
	private Logger logger =  LoggerFactory.getLogger(this.getClass());
	@Resource
	private SysUserService sysUserService;
	
	@RequestMapping("/login")
	public String login(){
		return "system/index/login";
	}
	@RequestMapping("/tab")
	public String tab(){
		return "system/index/tab";
	}
	@RequestMapping("/index_default")
	public String indexDefault(){
		return "system/index/default";
	}
	
	
	/**
	 * 系统首页
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/", "index"})
	@LogAnnotation("进入系统首页")
	@RequiresPermissions("index:view")
	public String index(HttpServletRequest request){
		return "system/index/index";
	}
	
	/**
	 * 登录提交地址和applicationontext-shiro.xml配置的loginurl一致。 (配置文件方式的说法)  
	 * @param request
	 * @param map
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value="/login",method=RequestMethod.POST)  
    public String login(HttpServletRequest request, Map<String, Object> map) throws Exception {  
       logger.info("IndexController.login()");  
       // 登录失败从request中获取shiro处理的异常信息。  
       // shiroLoginFailure:就是shiro异常类的全类名.  
       String exception = (String) request.getAttribute("shiroLoginFailure");  
       logger.error("exception=" + exception);  
       String msg = "";  
       if (exception != null) {  
    	   String codeValidateFailed = "codeValidateFailed";
           if (UnknownAccountException.class.getName().equals(exception)) {  
              logger.error("UnknownAccountException -- > 账号不存在：");  
              msg = "UnknownAccountException -- > 账号不存在：";  
           } else if (IncorrectCredentialsException.class.getName().equals(exception)) {  
              logger.error("IncorrectCredentialsException -- > 密码不正确：");  
              msg = "IncorrectCredentialsException -- > 密码不正确：";  
           } else if (codeValidateFailed.equals(exception)) {  
              logger.error("codeValidateFailed -- > 验证码错误");  
              msg = "codeValidateFailed -- > 验证码错误";  
           } else {  
              msg = "else >> "+exception;  
              logger.error("else -- >" + exception);  
           }  
       }  
       map.put("msg", msg);  
       // 此方法不处理登录成功,由shiro进行处理.  
       return "system/index/login";  
    } 
	
}
