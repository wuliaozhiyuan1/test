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
import com.wuliaozhiyuan.bean.system.Dictionary;
import com.wuliaozhiyuan.bean.system.Sitemail;
import com.wuliaozhiyuan.bean.system.vo.DictionaryVo;
import com.wuliaozhiyuan.bean.system.vo.SysUserVo;
import com.wuliaozhiyuan.service.system.SitemailService;
import com.wuliaozhiyuan.service.system.SysUserService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Result;
import com.wuliaozhiyuan.util.Tools;
import com.wuliaozhiyuan.validate.DictionarySaveValidate;
import com.wuliaozhiyuan.validate.SysUserSaveValidate;
/**
 * 站内信Controller
 * @author shuyy
 * @date 2017年12月3日
 */
@Controller
@RequestMapping("/user")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 列表
	 * @param page
	 * @return
	 * @author shuyy
	 * @throws Exception 
	 * @date 2017年12月3日
	 */
	@RequestMapping("/list")
	@LogAnnotation("查询用户列表")
	@RequiresPermissions("userManager:view")
	public String list(Page page) throws Exception{
		List<PageData> users = sysUserService.listUser(page);
		String userlListParam = "userList";
		PageData pd = page.getPd();
		pd.put(userlListParam, users);
		String pageParam = "page";
		pd.put(pageParam, page);
		return "system/user/user_list";
	}
	
	/**
	 * 访问新增页面
	 * @return
	 * @throws Exception
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@LogAnnotation("访问新增用户页面")
	@RequestMapping(value="/goAdd")
	@RequiresPermissions("userManager:add")
	public String goAdd()throws Exception{
		PageData pd = Tools.getPageData();
		return "system/user/user_add";
	}
	/**
	 * 判断用户名是否已经存在
	 * @return
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	@RequestMapping("/hasUsername")
	@LogAnnotation("判断用户名是否已经存在")
	@ResponseBody
	public Result hasUsername(){
		PageData pageData = Tools.getPageData();
		String username = pageData.getString("username");
		Integer count = sysUserService.hasUsername(username);
		if(count > 0){
			return Result.build("用户名已经存在", null, false);
		}
		return Result.success();
	}
	/**
	 * 保存用户
	 * @param sysUserVo
	 * @param result
	 * @return
	 * @throws Exception
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	@RequestMapping("/save")
	@LogAnnotation("保存用户字典")
	@RequiresPermissions("userManager:add")
	public String save(@Validated({SysUserSaveValidate.class}) SysUserVo sysUserVo,  BindingResult result) throws Exception{
		sysUserService.save(sysUserVo);
		return "save_result";
	}
}
