package com.wuliaozhiyuan.service.system;

import java.util.List;

import com.wuliaozhiyuan.bean.system.Company;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;

/**
 * 企业Service接口
 * @author wuliaozhiyuan
 *
 */
public interface CompanyService {
	/**
	 * 获得子企业，通过父企业id。返回格式为一个ztree需要的格式
	 * @param pd
	 * @return
	 * {id:id,
	 *  idParent:true,
	 *  name:name,
	 *  url:url,
	 *  target:targetFrame
	 *  }
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public List<PageData> getSubCompanpy(PageData pd) throws Exception;
	/**
	 * 获得企业列表
	 * @param page 分页参数：含有的查询参数有：{
	 * pd:{
	 *\/登录用户企业树路径。用于限制只显示这个用户下的企业列表，为空，则没有用户企业限制
	 * currentCompanyTreePath:currentCompanyTreePath(ps:optional),
	 * treePath:treePath(ps:optional),
	 * companyTypeCode:companyTypeCode(ps:optional), 
	 * keywords:keywords(ps:optional.keywords可以是companyName，contactMan，contactPhone)}
	 * }
	 * @return
	 */
	public List<Company> listCompany(Page page);
	
	/**
	 * 查询当前登录用户下的所有company 
	 * @param currentCompanyTreePath
	 * @return
	 * @author shuyy
	 * @date 2017年11月26日
	 */
	public List<Company> companyList(String currentCompanyTreePath);
	/**
	 * 根据id，获取company
	 * @param id
	 * @return
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	public Company getCompany(Long id);
}
