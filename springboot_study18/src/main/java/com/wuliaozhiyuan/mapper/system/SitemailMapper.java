package com.wuliaozhiyuan.mapper.system;

import java.util.List;

import com.wuliaozhiyuan.bean.system.Sitemail;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;

/**
 * logMapper
 * @author wuliaozhiyuan
 *
 */
public interface SitemailMapper {
	/**
	 * 统计站内信数量
	 * @param pageData格式为：{
	 * status:状态：已读、未读。,
	 * toUsername:收件人用户名
	 * }
	 * @return
	 */
	public Integer totalNumByStatus(PageData pageData);
	/**
	 * 分页查询站内信列表
	 * @param page
	 * @return
	 * @author shuyy
	 * @date 2017年12月3日
	 */
	public List<Sitemail> sitemailListPage(Page page);
	
}
