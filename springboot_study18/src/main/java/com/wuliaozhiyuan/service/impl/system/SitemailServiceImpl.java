package com.wuliaozhiyuan.service.impl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliaozhiyuan.bean.system.Sitemail;
import com.wuliaozhiyuan.mapper.system.SitemailMapper;
import com.wuliaozhiyuan.service.system.SitemailService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Tools;
/**
 * 站内信Service
 * @author shuyy
 * @date 2017年11月30日
 */
@Service
public class SitemailServiceImpl implements SitemailService {
	
	@Autowired
	private SitemailMapper sitemailMapper;
	/**
	 * 统计站内信数量
	 * @param pageData格式为：{
	 * status:状态：已读、未读。,
	 * toUsername:收件人用户名
	 * }
	 * @return
	 */
	@Override
	public Integer totalNumByStatus(PageData pageData) {
		return sitemailMapper.totalNumByStatus(pageData);
	}
	/**
	 * 分页查询分类信列表
	 * @param page
	 * @return
	 * @author shuyy
	 * @date 2017年12月3日
	 */
	@Override
	public List<Sitemail> sitemailLIstPage(Page page) {
		PageData pd = page.getPd();
		String type = pd.getString("type");
		if("收件箱".equals(type)){
			pd.put("toUsername", Tools.getCurrentUser().getUsername());
		}else{
			pd.put("fromUsername", Tools.getCurrentUser().getUsername());
		}
		return sitemailMapper.sitemailListPage(page);
	}
}
