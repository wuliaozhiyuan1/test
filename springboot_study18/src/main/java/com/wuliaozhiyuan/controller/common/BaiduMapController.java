package com.wuliaozhiyuan.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 百度地图Controller
 * @author shuyy
 * @date 2017年11月26日
 */
@Controller
@RequestMapping("/baiduMap")
public class BaiduMapController {
	/**
	 * 访问百度地图选择位置的页面
	 * @return
	 * @author shuyy
	 * @date 2017年11月26日
	 */
	@RequestMapping("/chooseSite")
	public String chooseSite(){
		return "common/baiduMap/chooseSite";
	}
}
