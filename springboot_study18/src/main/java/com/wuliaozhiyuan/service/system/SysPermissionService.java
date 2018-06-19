package com.wuliaozhiyuan.service.system;

import java.util.List;

import com.wuliaozhiyuan.bean.shiro.SysPermission;
import com.wuliaozhiyuan.util.PageData;
/**
 * 权限菜单Service接口
 * @author wuliaozhiyuan
 *
 */
public interface SysPermissionService {

	/**
	 * 查询指定类型的所有权限，通过UserId、资源权限类型，资源类型为空，则查询所有的资源
	 * @param pd
	 * @return
	 */
	public List<SysPermission> listSysPermissionByUserId(PageData pd);
	
	/**
	 * 通过userid查出所有菜单权限
	 * @param pd
	 * @return
	 */
	public List<SysPermission> listSysPermissionByParentId(PageData pd);
	
	/**
	 * 通过id获取SysPermission对象
	 * @param id
	 * @return
	 */
	public SysPermission getSysPermissionById(long id);
	
	/**
	 * 保存
	 * @param pd
	 */
	public void saveSysPermission(PageData pd);
	/**
	 * 更新
	 * @param pd
	 * @throws Exception
	 */
	public void updateSysPermission(PageData pd) throws Exception;
	/**
	 * 删除
	 * @param pd
	 */
	public void deleteSysPermission(PageData pd);
	
}
