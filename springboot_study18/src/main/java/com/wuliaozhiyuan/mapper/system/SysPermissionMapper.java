package com.wuliaozhiyuan.mapper.system;

import java.util.List;

import com.wuliaozhiyuan.bean.shiro.SysPermission;
import com.wuliaozhiyuan.util.PageData;
/**
 * 权限（菜单）Mapper
 * @author wuliaozhiyuan
 *
 */
public interface SysPermissionMapper {
	/**
	 * 查询指定类型的所有权限，通过UserId、资源权限类型，资源类型为空，则查询所有的资源
	 * @param pd
	 * @return
	 */
	public List<SysPermission> listPermissionByUserId(PageData pd);
	
	/**
	 * 根据父权限菜单，查询所有的子权限菜单
	 * @param pd
	 * @return
	 */
	public List<SysPermission> listPermissionByParentId(PageData pd);
	/**
	 * 通过id获取SysPermission对象
	 * @param id
	 * @return
	 */
	public SysPermission getSysPermissionById(Long id);
	/**
	 * 更新
	 * @param pd
	 * @return
	 */
	public void update(PageData pd);
	/**
	 * 更新增删改查权限资源，通过permission字段
	 * @param pd
	 * @return
	 */
	public void updateCrudButtonPermissionByPermission(PageData pd);
	/**
	 * 通过id删除SysPermission对象
	 * @param pd
	 * @return
	 */
	public void deleteById(PageData pd);
	/**
	 * 通过permission前缀删除SysPermission对象
	 * @param pd
	 * @return
	 */
	public void deleteByPermissionPrefix(PageData pd);
	/**
	 * 删除权限角色关系表中的有关指定权限前缀的记录
	 * @param pd
	 */
	public void deleteRolePermissionByPermissionPrefix(PageData pd);
	
	
	
}
