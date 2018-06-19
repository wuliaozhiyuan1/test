package com.wuliaozhiyuan.mapper.system;

import java.util.List;

import com.wuliaozhiyuan.bean.shiro.SysRole;
import com.wuliaozhiyuan.util.PageData;

/**
 * 角色Mapper
 * @author wuliaozhiyuan
 *
 */
public interface SysRoleMapper {
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
	 * @param id
	 * @return
	 */
	public SysRole getRoleById(Long id);
	
	/**
	 * 更新角色，通过角色id
	 * @param pd
	 */
	public void update(PageData pd);
	/**
	 * 查询角色下的用户数量，通过用户id
	 * @param roleId 用户id
	 * @return
	 */
	public Integer getUserNumByroleId(Long roleId);
	/**
	 * 删除角色
	 * @param pd
	 */
	public void deleteById(PageData pd);
	/**
	 * 所有的菜单，用于构造ztree树，返回格式为：
	 * [{
	 * id:id,
	 * name:name,
	 * parentId, parentId
	 * },..]
	 * @return
	 */
	public List<PageData> listMenu();
	/**
	 * 查出角色下的菜单id，返回格式为：
	 * [{
	 * id:id
	 * },..]
	 * @param roleId
	 * @return
	 */
	public List<PageData> listMenuByRoleId(Long roleId);
	/**
	 * 删除角色的菜单权限，通过角色id：roleId，菜单id:permissionId
	 * @param pd
	 */
	public void deleteRoleMenu(PageData pd);
	/**
	 * 保存角色的菜单权限。
	 * @param pd {permissionId: permissionId, roleId:roleId}
	 */
	public void saveRoleMenu(PageData pd);
	/**
	 * 查出该角色下的所有指定类型的增删改查权限
	 * @param pageData 格式为：{permissionType:permissionType, roleId:roleId}
	 * @return
	 */
	public List<PageData> listCrudPermissionByRoleId(PageData pageData);
	
	/**
	 * 删除角色的菜单下的增删改查权限，通过角色id，和权限标识符permission
	 * @param pd
	 */
	public void deleteRolePermissionByPermission(PageData pd);
	/**
	 * 保存角色菜单下的增删改查权.限，通过角色id，权限标识符
	 * @param pd {permissionId: permissionId, roleId:roleId}
	 */
	public void saveRolePermissionByPermission(PageData pd);
}
