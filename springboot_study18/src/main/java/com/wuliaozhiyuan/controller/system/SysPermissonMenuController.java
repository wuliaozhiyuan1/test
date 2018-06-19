package com.wuliaozhiyuan.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliaozhiyuan.annotation.LogAnnotation;
import com.wuliaozhiyuan.bean.shiro.SysPermission;
import com.wuliaozhiyuan.service.system.SysPermissionService;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Result;
import com.wuliaozhiyuan.util.Tools;
/**
 * 权限菜单Controller
 * @author wuliaozhiyuan
 *
 */
@Controller
@RequestMapping("/menu")
public class SysPermissonMenuController {
	
	@Autowired
	private SysPermissionService sysPermissionService;
	/**
	 * 显示菜单列表ztree(菜单管理)
	 * @param model
	 * 
	 */
	@RequestMapping("/listAllMenu")
	@RequiresPermissions("menuManager:menu")
	@LogAnnotation("显示菜单列表")
	public String listAllMenu(Model model,Long menuId){
		model.addAttribute("menu_id", menuId);
		return "system/menu/menu_ztree";
	}
	
	/**
	 * 获取子menu数据，拼装成zTree需要的格式
	 * @param model
	 */
	@RequestMapping(value="/getSubMenu")
	@ResponseBody
	@RequiresPermissions("menuManager:view")
	@LogAnnotation("获取子菜单")
	public Object getSubMenu(Model model,Long id)throws Exception{
		if(id == null){
			id = (long)0;
		}
		PageData pageData = Tools.getPageData();
		pageData.put("parentId", id);
		pageData.put("resourceType", "menu");
		List<SysPermission> sysPermissionList = sysPermissionService.listSysPermissionByParentId(pageData);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(SysPermission s : sysPermissionList){
			Map<String, Object> result = new HashMap<>((int)(3 / 0.75f + 1f));
			result.put("id", s.getId() + "");
			result.put("isParent", s.getHaveChildren());
			result.put("name", s.getName());
			String url = "menu/toUpdate.html?id=" + s.getId();
			url = Tools.getBaseUrl() + url;
			result.put("url", url);
			result.put("target", "treeFrame");
			resultList.add(result);
		}
		return resultList;
	}
	/**
	 * 请求新增菜单页面
	 * @param model
	 * @return
	 */
	@LogAnnotation("访问新增菜单页面")
	@RequestMapping(value="/toAdd")
	public String toAdd()throws Exception{
		PageData pd = Tools.getPageData();
		//接收传过来的上级菜单ID,如果上级为顶级就取值“0”
		String menuId = (null == pd.get("menu_id") || "".equals(pd.get("menu_id").toString()))?"0":pd.get("menu_id").toString();
		pd.put("menuId",menuId);
		//传入父菜单所有信息
		SysPermission sysPermission = sysPermissionService.getSysPermissionById(Long.parseLong(menuId));
		pd.put("pds", sysPermission);	
		return "system/menu/menu_add";
	}	
	/**
	 * 保存菜单信息
	 * @param menu
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	@RequiresPermissions("menuManager:add")
	@ResponseBody
	@LogAnnotation("保存菜单")
	public Object add(SysPermission sysPermission)throws Exception{
		PageData pd = Tools.getPageData();
//		//因为is开头的属性，springmvc绑定不了参数，所以这里手动绑定
//		sysPermission.setHaveChildren(pd.getBoolean("haveChildren"));;
		pd.put("sysPermission", sysPermission);
		sysPermissionService.saveSysPermission(pd);
		return Result.success();
	}
	/**
	 * 访问编辑菜单页面
	 * @return
	 * @throws Exception
	 */
	/**
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toUpdate")
	@RequiresPermissions("menuManager:update")
	@LogAnnotation("访问编辑菜单页面")
	public String toEdit() throws Exception{
		PageData pageData = Tools.getPageData();
		SysPermission sysPermission = sysPermissionService.getSysPermissionById(pageData.getLong("id"));
		pageData.put("menu", sysPermission);
		return "system/menu/menu_update";
	}
	/**
	 * 更新菜单
	 * @param sysPermission
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update")
	@RequiresPermissions("menuManager:update")
	@ResponseBody
	@LogAnnotation("更新菜单")
	public Object update() throws Exception{
		PageData pd = Tools.getPageData();
		sysPermissionService.updateSysPermission(pd);
		return Result.success();
	}
	/**
	 * 删除菜单
	 * @param sysPermission
	 * @return
	 * @throws Exception
	 */
	@LogAnnotation("删除菜单")
	@RequestMapping("/delete")
	@RequiresPermissions("menuManager:delete")
	@ResponseBody
	public Object delete() throws Exception{
		PageData pageData = Tools.getPageData();
		sysPermissionService.deleteSysPermission(pageData);
		return Result.success();
	}
	
	/**
	 * 显示菜单列表
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("menuManager:view")
	@RequestMapping
	@LogAnnotation("显示菜单列表")
	public String list(Model model) throws Exception{
		PageData pageData = Tools.getPageData();
		Long menuId = pageData.get("menu_id") == null 
				|| pageData.get("menu_id").equals("") ? (long)0 
						: pageData.getLong("menu_id");
		pageData.put("resourceType", "menu");		
		pageData.put("parentId", menuId);
		if(menuId != 0){
			SysPermission sysPermission = sysPermissionService.getSysPermissionById(menuId);
			pageData.put("parentIdSparentId",sysPermission.getParentId());
		}
		List<SysPermission> sysPermissionList = sysPermissionService.listSysPermissionByParentId(pageData);
		model.addAttribute("menuList", sysPermissionList);		
		return "system/menu/menu_list";
	}
	
}
