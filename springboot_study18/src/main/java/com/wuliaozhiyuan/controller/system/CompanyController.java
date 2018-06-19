package com.wuliaozhiyuan.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliaozhiyuan.annotation.LogAnnotation;
import com.wuliaozhiyuan.bean.shiro.SysUser;
import com.wuliaozhiyuan.bean.system.Company;
import com.wuliaozhiyuan.service.system.CompanyService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Result;
import com.wuliaozhiyuan.util.Tools;
import com.wuliaozhiyuan.validate.CompanySaveValidate;
import com.wuliaozhiyuan.validate.DictionarySaveValidate;
/**
 * 企业Controller
 * @author wuliaozhiyuan
 *
 */
@RequestMapping("/company")
@Controller
public class CompanyController {
	@Autowired
	private CompanyService companyService;
	/**
	 * 显示企业列表ztree(菜单管理)
	 * @param model
	 * 
	 */
	@RequestMapping("/toCompanyTree")
	@RequiresPermissions("companyManager:view")
	@LogAnnotation("访问企业树页面")
	public String toCompanyTree(){
		String parentIdParam = "parentId";
		Long  parentId = Tools.getPageData().getLong(parentIdParam);
		//将当前登录者企业id传入页面
		SysUser user = Tools.getCurrentUser();
		Long currentCompanyId = null;
		if(user.getCompany() != null){
			//说明是不是admin登录
			currentCompanyId = user.getCompany().getId();
			Tools.getPageData().put(parentIdParam, currentCompanyId);
		}else{
			Tools.getPageData().put(parentIdParam, 0);
		}
		return "system/company/company_ztree";
	}
	
	/**
	 * 获取子company数据，拼装成zTree需要的格式
	 * @param model
	 */
	@RequestMapping(value="/getSubCompany")
	@ResponseBody
	@RequiresPermissions("companyManager:view")
	@LogAnnotation("获取子企业")
	public Object getSubCompany()throws Exception{
		PageData pageData = Tools.getPageData();
		return companyService.getSubCompanpy(pageData);
	}
	/**
	 * 企业列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping("/listCompany")
	@RequiresPermissions("companyManager:view")
	@LogAnnotation("显示企业列表")
	public String listCompany(Page page) throws Exception{
		SysUser user = Tools.getCurrentUser();
		if(user.getCompany() != null){
			//说明是不是admin登录
			String currentCompanyTreePath = user.getCompany().getTreePath();
			String currentCompanyTreePathParam = "currentCompanyTreePath";
			page.getPd().put(currentCompanyTreePathParam, currentCompanyTreePath);
		}
		List<Company> listCompany = companyService.listCompany(page);
		page.getPd().put("companyList", listCompany);
		page.getPd().put("page", page);
		return "system/company/company_list";
	}
	/**
	 * 访问新增企业页面
	 * @author shuyy
	 * @return
	 */
	@RequestMapping("/goAdd")
	@RequiresPermissions("companyManager:add")
	@LogAnnotation("访问新增企业页面")
	public String goAdd(){
		PageData pd = Tools.getPageData();
		String parentIdParam = "parentId";
		if(pd.getLong(parentIdParam) == null){
			pd.put(parentIdParam, 0);
		}
		return "system/company/company_add";
	}
	
	/**
	 * 获取当前登录用户下的所有company
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping("/listAllCompany")
	@LogAnnotation("获取所有企业")
	@ResponseBody
	public Object listAllCompany() throws Exception{
		SysUser user = Tools.getCurrentUser();
		if(user.getCompany() != null){
			//说明是不是admin登录
			String currentCompanyTreePath = user.getCompany().getTreePath();
			List<Company> companyList = companyService.companyList(currentCompanyTreePath);
			return Result.success(companyList);
		}
		List<Company> companyList = companyService.companyList(null);
		return Result.success(companyList);
	}
	/**
	 * 通过id，获取企业
	 * @return
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	@RequestMapping("/getCompany")
	@ResponseBody
	@LogAnnotation("根据id，获取企业")
	public Result getCompany(){
		PageData pageData = Tools.getPageData();
		Long id = pageData.getLong("id");
		Company company = companyService.getCompany(id);
		return Result.success(company);
	}
	@RequestMapping("/save")
	@LogAnnotation("保存企业")
	@RequiresPermissions("companyManager:add")
	public String save(@Validated({CompanySaveValidate.class})Company company, BindingResult result){
		
		
		return "save_result";
	}
	

}
