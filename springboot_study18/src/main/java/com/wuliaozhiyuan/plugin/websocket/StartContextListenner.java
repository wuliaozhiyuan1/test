package com.wuliaozhiyuan.plugin.websocket;

import java.net.UnknownHostException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.java_websocket.WebSocketImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.wuliaozhiyuan.config.system.WebSocketSetting;
/**
 * 监听器，启动websocket服务
 * @author shuyy
 * @date 2017年11月29日
 */
@WebListener
public class StartContextListenner implements ServletContextListener {
	
	@Autowired
	private WebSocketSetting webSocketSetting;
    /**
     * 启动即时聊天服务
     */
    public void startWebsocketInstantMsg(){
        WebSocketImpl.DEBUG = false;
        ChatServer s = null;
        try {
        	if(!StringUtils.isEmpty(webSocketSetting.getChatPort())){
        		s = new ChatServer(Integer.parseInt(webSocketSetting.getChatPort()));
        		s.start();
        	}
            System.out.println( "websocket服务器启动,端口" + s.getPort() );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动在线管理服务
     */
    public void startWebsocketOnline(){
        WebSocketImpl.DEBUG = false;
        OnlineChatServer s = null;
        try {
        	if(!StringUtils.isEmpty(webSocketSetting.getOnLinePort())){
        		int port = Integer.parseInt(webSocketSetting.getOnLinePort());
        		s = new OnlineChatServer(port);
        		s.start();
        	}
            System.out.println( "websocket服务器启动,端口" + s.getPort() );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }




	@Override
	public void contextInitialized(ServletContextEvent sce) {
		this.startWebsocketInstantMsg();
        this.startWebsocketOnline();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
