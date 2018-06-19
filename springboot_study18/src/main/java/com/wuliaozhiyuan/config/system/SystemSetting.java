package com.wuliaozhiyuan.config.system;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * 自定义属性类properties绑定类
 * @author wuliaozhiyuan
 *
 */
@ConfigurationProperties(prefix="system", locations = "classpath:config/system.properties")
public class SystemSetting {
	/**平台名称*/
	private String sysname;
	/**分页每页默认显示数*/
	private Integer pageCount;
	
	
	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	
	
}
