package com.wuliaozhiyuan.plugin.websocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.alibaba.fastjson.JSONObject;


/**
 * 即时通讯
 */
public class ChatServer extends WebSocketServer{
	int i = 0;
    public ChatServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public ChatServer(InetSocketAddress address) {
        super(address);
    }

    /**
     * 触发连接事件
     */
    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
    }

    /**
     * 触发关闭事件
     */
    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        userLeave(conn);
    }

    /**
     * 客户端发送消息到服务器时触发事件
     */
    @Override
    public void onMessage(WebSocket conn, String message){
        message = message.toString();
        if(null != message && message.startsWith("[add]")){
            this.userjoin(message.replaceFirst("\\[add\\]", ""),conn);
        }if(null != message && message.startsWith("[leave]")){
            this.userLeave(conn);
        }if(null != message && message.contains("[$sendUser$]")){
            String toUser = message.substring(message.indexOf("[$sendUser$]")+12, message.indexOf("[$sendUser$]"));
            message = message.substring(0, message.indexOf("[$sendUser$]")) +"[私信]  "+ message.substring(message.indexOf("[$sendUser$]")+12, message.length());
            ChatServerPool.sendMessageToUser(ChatServerPool.getWebSocketByUser(toUser),message);//向所某用户发送消息
            ChatServerPool.sendMessageToUser(conn, message);//同时向本人发送消息
        }else{
            ChatServerPool.sendMessage(message.toString());//向所有在线用户发送消息
        }
    }

    public void onFragment( WebSocket conn, Framedata fragment ) {
    }

    /**
     * 触发异常事件
     */
    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
        if( conn != null ) {
            //some errors like port binding failed may not be assignable to a specific websocket
        }
    }


    /**
     * 用户加入处理
     * @param user
     */
    public void userjoin(String user, WebSocket conn){
    	JSONObject j = new JSONObject(2);
//    	com.alibaba.fastjson.JSONObject j = new com.alibaba.fastjson.JSONObject(2);
    	j.put("type", "user_join");
    	j.put("user", "<a onclick=\"toUserMsg('"+user+"');\">"+user+"</a>");
    	 //把当前用户加入到所有在线用户列表中
    	String json = j.toJSONString();
        ChatServerPool.sendMessage(json);             
        String joinMsg = "{\"from\":\"[系统]\",\"content\":\""+user+"上线了\",\"timestamp\":"+new Date().getTime()+",\"type\":\"message\"}";
      //向所有在线用户推送当前用户上线的消息
        ChatServerPool.sendMessage(joinMsg);                        
        j = new com.alibaba.fastjson.JSONObject();
        j.put("type", "get_online_user");
      //向连接池添加当前的连接对象
        ChatServerPool.addUser(user,conn);                          
        j.put("list", ChatServerPool.getOnlineUser());
      //向当前连接发送当前在线用户的列表
        ChatServerPool.sendMessageToUser(conn, j.toJSONString());  
    }
    
/*    *//**
     * 用户加入处理
     * @param user
     *//*
    public void userjoin(String user, WebSocket conn){
    	JSONObject result = new JSONObject();
    	result.element("type", "user_join");
    	result.element("user", "<a onclick=\"toUserMsg('"+user+"');\">"+user+"</a>");
    	ChatServerPool.sendMessage(result.toString());              //把当前用户加入到所有在线用户列表中
    	String joinMsg = "{\"from\":\"[系统]\",\"content\":\""+user+"上线了\",\"timestamp\":"+new Date().getTime()+",\"type\":\"message\"}";
    	ChatServerPool.sendMessage(joinMsg);                        //向所有在线用户推送当前用户上线的消息
    	result = new JSONObject();
    	result.element("type", "get_online_user");
    	ChatServerPool.addUser(user,conn);                          //向连接池添加当前的连接对象
    	result.element("list", ChatServerPool.getOnlineUser());
    	ChatServerPool.sendMessageToUser(conn, result.toString());  //向当前连接发送当前在线用户的列表
    }
*/
    /**
     * 用户下线处理
     * @param user
     */
    public void userLeave(WebSocket conn){
        String user = ChatServerPool.getUserByKey(conn);
        boolean b = ChatServerPool.removeUser(conn);                //在连接池中移除连接
        if(b){
            JSONObject result = new JSONObject();
            result.put("type", "user_leave");
            result.put("user", "<a onclick=\"toUserMsg('"+user+"');\">"+user+"</a>");
            ChatServerPool.sendMessage(result.toJSONString());          //把当前用户从所有在线用户列表中删除
            String joinMsg = "{\"from\":\"[系统]\",\"content\":\""+user+"下线了\",\"timestamp\":"+new Date().getTime()+",\"type\":\"message\"}";
            ChatServerPool.sendMessage(joinMsg);                    //向在线用户发送当前用户退出的消息
        }
    }
/*    *//**
     * 用户下线处理
     * @param user
     *//*
    public void userLeave(WebSocket conn){
    	String user = ChatServerPool.getUserByKey(conn);
    	boolean b = ChatServerPool.removeUser(conn);                //在连接池中移除连接
    	if(b){
    		JSONObject result = new JSONObject();
    		result.element("type", "user_leave");
    		result.element("user", "<a onclick=\"toUserMsg('"+user+"');\">"+user+"</a>");
    		ChatServerPool.sendMessage(result.toString());          //把当前用户从所有在线用户列表中删除
    		String joinMsg = "{\"from\":\"[系统]\",\"content\":\""+user+"下线了\",\"timestamp\":"+new Date().getTime()+",\"type\":\"message\"}";
    		ChatServerPool.sendMessage(joinMsg);                    //向在线用户发送当前用户退出的消息
    	}
    }
*/    public static void main( String[] args ) throws InterruptedException , IOException {
//        WebSocketImpl.DEBUG = false;
//        int port = 8887; //端口
//        ChatServer s = new ChatServer(port);
//        s.start();
//        System.out.println( "服务器的端口" + s.getPort() );
	JSONObject j = new JSONObject(2);
//	com.alibaba.fastjson.JSONObject j = new com.alibaba.fastjson.JSONObject(2);
	j.put("type", "user_join");
	j.put("user", "<a onclick=\"toUserMsg('"+"admin"+"');\">"+"admin"+"</a>");
	 //把当前用户加入到所有在线用户列表中
	String json = j.toJSONString();
	System.out.println(json);
    }

}

