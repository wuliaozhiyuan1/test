package com.wuliaozhiyuan.service.impl.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wuliaozhiyuan.bean.system.Company;
import com.wuliaozhiyuan.mapper.system.CompanyMapper;
import com.wuliaozhiyuan.service.system.CompanyService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Tools;
/**
 * 企业Service
 * @author wuliaozhiyuan
 *
 */
@Service
public class CompanyServiceImpl implements CompanyService {
	
	@Autowired
	private CompanyMapper companyMapper;
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
	@Override
	public List<PageData> getSubCompanpy(PageData pd) throws Exception{
		String idParam = "id";
		String parentIdParam = "parentId";
		if(pd.getLong(idParam) == null){
			pd.put(parentIdParam, 0);
		}else{
			pd.put(parentIdParam, pd.getLong(idParam));
		}
		List<Company> companyList = companyMapper.listCompanyByParentId(pd);
		List<PageData> resultList = new ArrayList<PageData>(companyList.size());
		for(Company company : companyList){
			PageData pageData = PageData.getCommanInstance(10);
			pageData.addObjectField(company);
			pageData.put("name", company.getCompanyName());
			String url = "company/listCompany.html?parentId=" + company.getId();
			url = Tools.getBaseUrl() + url;
			String urlParam = "url";
			pageData.put(urlParam,url);
			String targetParam = "target";
			String targetValue = "treeFrame";
			pageData.put(targetParam, targetValue);
			resultList.add(pageData);
		}
		return resultList;
	}
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
	@Override
	public List<Company> listCompany(Page page) {
		PageData pd = page.getPd();
		String treePathParam = "treePath";
		if(page.getPd().getString(treePathParam) == null){
			page.getPd().put(treePathParam, "/");
		}
		return companyMapper.companyListPage(page);
	}
	/**
	 * 查询当前登录用户下的所有company 
	 * @param currentCompanyTreePath
	 * @return
	 * @author shuyy
	 * @date 2017年11月26日
	 */
	@Override
	public List<Company> companyList(String currentCompanyTreePath){
		return companyMapper.companyList(currentCompanyTreePath);
	}
	
	/**
	 * 根据id，获取company
	 * @param id
	 * @return
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	@Override
	public Company getCompany(Long id){
		return companyMapper.getCompany(id);
	}
}
