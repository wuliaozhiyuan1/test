package com.wuliaozhiyuan.bean.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 日志对象
 * @author wuliaozhiyuan
 *
 */
@Entity  
public class Log implements Serializable{
	/**主键*/
	@Id@GeneratedValue
	private Long id;
	/**用户账号名*/
	private String username;
	/**用户名*/
	private String name;
	/**用户id*/
	private Long userid;
	/**日志信息*/
	private String logContent;
	/**创建时间*/
	private Date createTime;
	/**用户ip*/
	private String ip;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Log(){
		
	}
	public Log(String username, String name, Long userid, String logContent, Date createTime, String ip) {
		super();
		this.username = username;
		this.name = name;
		this.userid = userid;
		this.logContent = logContent;
		this.createTime = createTime;
		this.ip = ip;
	}
	
	
	
}
