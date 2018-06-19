package com.wuliaozhiyuan.service.system;

import java.util.List;
import java.util.Map;

import com.wuliaozhiyuan.bean.shiro.SysRole;
import com.wuliaozhiyuan.util.PageData;

/**
 * 角色Service
 * @author wuliaozhiyuan
 *
 */
public interface SysRoleService {
	/**
	 * 查询所有的角色
	 * @param uid
	 * @return
	 */
	public List<SysRole> listRole();
	/**
	 * 保存角色
	 * @param pd
	 */
	public void save(PageData pd);
	/**
	 * 获得角色，通过角色id
	 * @param pd
	 * @return
	 */
	public SysRole getRoleById(PageData pd);
	
	/**
	 * 更新角色，通过角色id
	 * @param pd
	 */
	public void update(PageData pd);
	/**
	 * 获得角色下的用户数，通过用户id
	 * @param pd
	 * @return
	 */
	public void delete(PageData pd);

	/**
	 * 所有的菜单，用于构造ztree树，返回格式为：
	 * [{
	 * id:id,
	 * name:name,
	 * pid: parentId,
	 * checked:true
	 * },..]
	 * @param roleId
	 * @return
	 */
	public List<PageData> listMenu(Long roleId);
	/**
	 * 保存角色的菜单权限
	 * @param param 格式为{id:roleId, menuId:List}
	 * @throws Exception 
	 */
	public void saveMenuqx(Map<String, Object> param) throws Exception;
	
	/**
	 * 所有的菜单，用于构造ztree树，返回格式为：
	 * [{
	 * id:id,
	 * name:name,
	 * pId: parentId,
	 * checked:true
	 * },..]
	 * @param roleId
	 * @param type 权限类型：add、delete、update、view
	 * @return
	 */
	public List<PageData> listCrudPermission(Long roleId, String type);
	
	/**
	 * 保存角色菜单下的增删改查权限
	 * @param param 格式为{id:roleId, menuId:List}
	 * @throws Exception 
	 */
	public void saveCrudPermission(Map<String, Object> param) throws Exception;
}
