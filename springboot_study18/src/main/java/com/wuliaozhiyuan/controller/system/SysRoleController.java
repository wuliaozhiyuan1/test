package com.wuliaozhiyuan.controller.system;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliaozhiyuan.annotation.LogAnnotation;
import com.wuliaozhiyuan.bean.shiro.SysRole;
import com.wuliaozhiyuan.service.system.SysRoleService;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Result;
import com.wuliaozhiyuan.util.Tools;

/**
 * 权限Controller
 * @author wuliaozhiyuan
 *
 */
@Controller
@RequestMapping("/role")
public class SysRoleController {

	@Autowired
	private SysRoleService roleService;
	
	/** 访问权限列表
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions("roleManager:view")
	@LogAnnotation("访问权限列表")
	public String list(Model model)throws Exception{
		List<SysRole> roleList = roleService.listRole();
		model.addAttribute("roleList", roleList);
		return "system/role/role_list";
	}
	
	/**访问新增角色页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	@RequiresPermissions("roleManager:add")
	@LogAnnotation("访问新增角色页面")
	public String toAdd(){
		return "system/role/role_add";
	}
	@RequestMapping("/add")
	@RequiresPermissions("roleManager:add")
	@LogAnnotation("保存新增角色")
	public String add() throws Exception{
		PageData pageData = Tools.getPageData();
		roleService.save(pageData);
		return "save_result";
	}
	/**访问新增角色页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/toUpdate")
	@RequiresPermissions("roleManager:update")
	@LogAnnotation("访问修改角色页面")
	public String toUpdate(){
		PageData pageData = Tools.getPageData();
		SysRole role = roleService.getRoleById(pageData);
		pageData.put("role", role);
		return "system/role/role_update";
	}
	/**
	 * 保存修改角色
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update")
	@RequiresPermissions("roleManager:update")
	@LogAnnotation("保存修改的角色")
	public String update() throws Exception{
		PageData pageData = Tools.getPageData();
		roleService.update(pageData);
		return "save_result";
	}
	/**
	 * 删除角色
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("roleManager:delete")
	@ResponseBody
	@LogAnnotation("删除角色")
	public Object delete(Long id) throws Exception{
		roleService.delete(Tools.getPageData());
		return Result.success();
	}
	/**
	 * 列举所有的菜单页面，Pagedata中有一个属性zTreeNodes：用于构造ztree树，格式为：
	 * [{
	 * id:id,
	 * name:name,
	 * pid: parentId,
	 * checked:true
	 * },..]
	 * @return
	 */
	@RequestMapping("/listMenu")
	@RequiresPermissions("roleManager:update")
	@LogAnnotation("修改菜单权限：列举所有菜单")
	public String listMenu(){
		PageData pageData = Tools.getPageData();
		pageData.put("zTreeNodes", roleService.listMenu(pageData.getLong("id")));
		return "system/role/menuqx";
	}
	/**
	 * 保存角色的菜单权限
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveMenuqx")
	@ResponseBody
	@LogAnnotation("/保存角色的的菜单权限")
	@RequiresPermissions("roleManager:update")
	public Object saveMenuqx(@RequestBody Map<String, Object> params) throws Exception{
		roleService.saveMenuqx(params);
		return Result.success();
	}
	/**
	 * 菜单的增删改查权限，列举所有的菜单页面，Pagedata中有一个属性zTreeNodes：用于构造ztree树，格式为：
	 * [{
	 * id:id,
	 * name:name,
	 * pid: parentId,
	 * checked:true
	 * },..]
	 * @return
	 */
	@RequestMapping("/crudButtonPermission")
	@RequiresPermissions("roleManager:update")
	@LogAnnotation("修改角色菜单下的增删改查权限")
	public String crudButtonPermission(){
		PageData pageData = Tools.getPageData();
		pageData.put("zTreeNodes", roleService.listCrudPermission(pageData.getLong("id"),
				pageData.getString("type")));
		return "system/role/crudPermission";
	}
	/**
	 * 保存菜单的增删改查权限
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveCrudPermission")
	@ResponseBody
	@RequiresPermissions("roleManager:update")
	@LogAnnotation("保存菜单的增删改查权限")
	public Object saveCrudPermission(@RequestBody Map<String, Object> params) throws Exception{
		roleService.saveCrudPermission(params);
		return Result.success();
	}
	/**
	 * 获取所有角色，供下拉框使用
	 * @return
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	@RequestMapping("/listRole")
	@ResponseBody
	@LogAnnotation("获取所有角色，供下拉框使用")
	public Result listRole(){
		List<SysRole> listRole = roleService.listRole();
		return Result.success(listRole);
	}
	
}
