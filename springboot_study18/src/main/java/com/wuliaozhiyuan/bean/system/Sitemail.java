package com.wuliaozhiyuan.bean.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 站内信
 * @author shuyy
 * @date 2017年11月30日
 */
@Entity  
public class Sitemail implements Serializable{
	private static final long serialVersionUID = -4490257924849470369L;

	/**主键id*/
	@Id@GeneratedValue
	private Long id;
	/**内容*/
	private String content;
	
	private String type;
	/**收件人名*/
	private String toUser;
	/**收件人用户名*/
	private String toUsername;
	/**发件人名*/
	private String fromUser;
	/**发件人用户名*/
	private String fromUsername;
	/**发件时间*/
	private Date sendTime;	
	/**状态：已读，未读*/
	private String status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getToUsername() {
		return toUsername;
	}
	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}
	public String getFromUsername() {
		return fromUsername;
	}
	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}
	
	
	
	
	
}
