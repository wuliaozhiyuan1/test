package com.wuliaozhiyuan.config.system;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * webSocket配置属性类
 * @author shuyy
 * @date 2017年11月29日
 */
@ConfigurationProperties(prefix="webSocket", locations = "classpath:config/webSocket.properties")
public class WebSocketSetting {
	//聊天websocket的地址
	private String chatHost;
	//聊天websocket端口
	private String chatPort;
	//用户管理websocket的地址
	private String onLineHost;
	//用户管理websocket的端口
	private String onLinePort;
	//站内信提示音效
	private String sitemailSound;
	public String getChatHost() {
		return chatHost;
	}
	public void setChatHost(String chatHost) {
		this.chatHost = chatHost;
	}
	
	public String getChatPort() {
		return chatPort;
	}
	public void setChatPort(String chatPort) {
		this.chatPort = chatPort;
	}
	public String getOnLineHost() {
		return onLineHost;
	}
	public void setOnLineHost(String onLineHost) {
		this.onLineHost = onLineHost;
	}
	public String getOnLinePort() {
		return onLinePort;
	}
	public void setOnLinePort(String onLinePort) {
		this.onLinePort = onLinePort;
	}
	public String getSitemailSound() {
		return sitemailSound;
	}
	public void setSitemailSound(String sitemailSound) {
		this.sitemailSound = sitemailSound;
	}
	
}
