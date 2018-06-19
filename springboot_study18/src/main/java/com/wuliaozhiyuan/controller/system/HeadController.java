package com.wuliaozhiyuan.controller.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliaozhiyuan.config.system.WebSocketSetting;
import com.wuliaozhiyuan.service.system.SitemailService;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Tools;

/**
 * 获取一些系统头部初始化信息
 * @author shuyy
 * @date 2017年11月29日
 */
@Controller
@RequestMapping("/head")
public class HeadController {
	
	@Autowired
	private WebSocketSetting webSocketSetting;
	
	@Autowired
	private SitemailService sitemailService;
	
	@RequestMapping(value = "/getList")
	@ResponseBody
	public Object getList() {
		Map<String, Object> map = new HashMap<>();
		//即使聊天的websocket端口
		map.put("chatAddress", webSocketSetting.getChatHost() + ":" + webSocketSetting.getChatPort());
		 // 在线管理和站内信服务器IP和端口
		map.put("onlineAddress", webSocketSetting.getOnLineHost() + ":" + webSocketSetting.getOnLinePort());
		
		String name = Tools.getCurrentUser().getName();
		String username = Tools.getCurrentUser().getUsername();
		
		map.put("name", name);
		map.put("username", username);
		//读取站内信未读数量
		PageData pageData = PageData.getCommanInstance(2);
		pageData.put("status", "未读");
		pageData.put("toUsername", username);
		Integer num = sitemailService.totalNumByStatus(pageData);
		map.put("sitemailNum", num);
		//站内信提示音效
		map.put("sitemailSound", webSocketSetting.getSitemailSound());
		return map;
	}

}
