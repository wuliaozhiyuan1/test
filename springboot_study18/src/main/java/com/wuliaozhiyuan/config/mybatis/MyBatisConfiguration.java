package com.wuliaozhiyuan.config.mybatis;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wuliaozhiyuan.plugin.PagePlugin;
import com.wuliaozhiyuan.util.Tools;
/**
 * mybatis配置类
 * @author wuliaozhiyuan
 *
 */
@Configuration
public class MyBatisConfiguration {

	/**
	 * 配置分页插件
	 * @author wuliaozhiyuan 
	 * @return
	 */
	@Bean
	public PagePlugin pagePlugin(){
		Tools.getLogger().info("MyBatisConfiguration.pagePlugin()");
		PagePlugin pagePlugin = new PagePlugin();
		Properties p = new Properties();  
		p.setProperty("dialect", "mysql");  
		p.setProperty("pageSqlId", ".*ListPage.*");  
		pagePlugin.setProperties(p);
		return pagePlugin;
	}
	
}
