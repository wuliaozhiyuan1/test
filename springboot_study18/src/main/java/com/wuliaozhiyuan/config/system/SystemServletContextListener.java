package com.wuliaozhiyuan.config.system;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * ServletContext监听器，初始化一些系统数据
 * @author wuliaozhiyuan
 *
 */
@WebListener
public class SystemServletContextListener implements ServletContextListener {
	
	@Autowired
	private SystemSetting systemSetting;
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String sysname = systemSetting.getSysname();
		System.out.println("sysname-----------" + sysname);
		sce.getServletContext().setAttribute("SYSNAME", sysname);
	}
	
	@Override
    public void contextDestroyed(ServletContextEvent arg0) {
              System.out.println("ServletContex销毁");
    }
}
