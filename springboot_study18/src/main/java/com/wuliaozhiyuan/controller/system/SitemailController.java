package com.wuliaozhiyuan.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuliaozhiyuan.annotation.LogAnnotation;
import com.wuliaozhiyuan.bean.system.Sitemail;
import com.wuliaozhiyuan.service.system.SitemailService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
/**
 * 站内信Controller
 * @author shuyy
 * @date 2017年12月3日
 */
@Controller
@RequestMapping("/sitemail")
public class SitemailController {
	@Autowired
	private SitemailService sitemailService;
	
	/**
	 * 列表
	 * @param page
	 * @return
	 * @author shuyy
	 * @date 2017年12月3日
	 */
	@RequestMapping("/list")
	@LogAnnotation("查询站内信列表")
	public String list(Page page){
		List<Sitemail> sitemailLIstPage = sitemailService.sitemailLIstPage(page);
		String sitemailListParam = "sitemailList";
		PageData pd = page.getPd();
		pd.put(sitemailListParam, sitemailLIstPage);
		String pageParam = "page";
		pd.put(pageParam, page);
		return "system/sitemail/sitemail_list";
	}
	
	
}
