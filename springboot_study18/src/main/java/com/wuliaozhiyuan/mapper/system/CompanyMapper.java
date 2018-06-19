package com.wuliaozhiyuan.mapper.system;

import java.util.List;

import com.wuliaozhiyuan.bean.system.Company;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
/**
 * 
 * @author wuliaozhiyuan
 *
 */
public interface CompanyMapper {
	/**
	 * 用于构造company树，通过父company，查子company
	 * @param pd
	 * @return
	 */
	public List<Company> listCompanyByParentId(PageData pd);
	/**
	 * 通过传入的参数，查询子公司以及子公司的子公司
	 * @param page 分页参数：含有的查询参数有：{
	 * pd:{treePath:treePath(ps:required),
	 * companyTypeCode:companyTypeCode(ps:optional), 
	 * currentCompanyTreePath:currentCompanyTreePath(ps:optional),
	 *  keywords:keywords(ps:optional.keywords可以是companyName，contactMan，contactPhone)}
	 * }
	 * @return
	 */
	public List<Company> companyListPage(Page page);
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
