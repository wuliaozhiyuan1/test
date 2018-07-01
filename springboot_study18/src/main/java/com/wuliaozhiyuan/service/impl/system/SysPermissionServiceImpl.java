package com.wuliaozhiyuan.service.impl.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.transform.impl.AddStaticInitTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wuliaozhiyuan.bean.shiro.SysPermission;
import com.wuliaozhiyuan.config.datasouce.dynamic.TargetDataSource;
import com.wuliaozhiyuan.dao.base.Dao;
import com.wuliaozhiyuan.jpa.repository.SysPermissionRepository;
import com.wuliaozhiyuan.mapper.system.SysPermissionMapper;
import com.wuliaozhiyuan.service.system.SysPermissionService;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Tools;

/**
 * 权限菜单Service
 * @author wuliaozhiyuan
 *
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService{
	@Autowired
	SysPermissionMapper sysPermissionMapper;
	
	@Autowired
	SysPermissionRepository sysPermissionRepository;
	
	@Autowired
	private Dao dao;
	/**
	 * 查询指定类型的所有权限，通过UserId、资源权限类型，资源类型为空，则查询所有的资源
	 * @param uid
	 * @return
	 */
	@Override
	@TargetDataSource("ds1")
	public List<SysPermission> listSysPermissionByUserId(PageData pd){
		return sysPermissionMapper.listPermissionByUserId(pd);
	}
	/**
	 * 通过userid查出所有菜单权限
	 * @param uid
	 * @return
	 */
	@TargetDataSource("ds1")
	@Override
	public List<SysPermission> listSysPermissionByParentId(PageData pd){
		return sysPermissionMapper.listPermissionByParentId(pd);
	}
	
	/**
	 * 通过id获取SysPermission对象
	 * @param id
	 * @return
	 */
	@Override
	@TargetDataSource("ds1")
	public SysPermission getSysPermissionById(long id){
		return sysPermissionMapper.getSysPermissionById(id);
	}
	/**
	 * 保存
	 * @param sysPermission
	 */
	@TargetDataSource("ds1")
	@Override
	public void saveSysPermission(PageData pageData){
		SysPermission sysPermission = (SysPermission)pageData.get("sysPermission");
		//保存菜单
		//默认菜单图标
		sysPermission.setMenuIcon("menu-icon fa fa-leaf black");
		SysPermission permission2 = sysPermissionRepository.save(sysPermission);
		//保存菜单的增删改查权限
		String permission = sysPermission.getPermission();
		String permisionPrefix = permission.split(":")[0];
		String addPermission = permisionPrefix + ":add";
		String updatePermission = permisionPrefix + ":update";
		String deletePermission = permisionPrefix + ":delete";
		String viewPermission = permisionPrefix + ":view";
		SysPermission addSysPermission = new SysPermission(sysPermission.getName() + "-新增",
				SysPermission.ResouceType.CRUDBUTTON, addPermission, true, permission2.getId());
		SysPermission deleteSysPermission = new SysPermission(sysPermission.getName() + "-删除",
				SysPermission.ResouceType.CRUDBUTTON, deletePermission, true,permission2.getId());
		SysPermission updateSysPermission = new SysPermission(sysPermission.getName() + "-更新",
				SysPermission.ResouceType.CRUDBUTTON, updatePermission, true,permission2.getId());
		SysPermission viewSysPermission = new SysPermission(sysPermission.getName() + "-查看",
				SysPermission.ResouceType.CRUDBUTTON, viewPermission, true,permission2.getId());
		sysPermissionRepository.save(addSysPermission);
		sysPermissionRepository.save(updateSysPermission);
		sysPermissionRepository.save(viewSysPermission);
		sysPermissionRepository.save(deleteSysPermission);
		//如果父菜单之前haveChildren为0，则改为1
		String parentMenuHaveChildren = "parentMenuHaveChildren";
		if(pageData.getBoolean(parentMenuHaveChildren)){
			PageData commanInstance = PageData.getCommanInstance(2);
			commanInstance.put("id", sysPermission.getParentId());
			commanInstance.put("haveChildren", 1);
			sysPermissionMapper.update(commanInstance);
		}
	}
	/**
	 * 更新
	 * @param pd
	 * @throws Exception 
	 */
	@Override
	public void updateSysPermission(PageData pd) throws Exception{
		//更新菜单
		sysPermissionMapper.update(pd);
		//更新菜单关联的权限资源
		String permissionPrefix =pd.getString("permission").split(":")[0] + ":";
		String menuName = pd.getString("name");
		String oldPermissionPrefix = pd.getString("oldPermission").split(":")[0] + ":";
		PageData permissionAdd = PageData.getCommanInstance(3);
		permissionAdd.put("permission", permissionPrefix+"add");
		permissionAdd.put("name", menuName + "-新增");
		permissionAdd.put("oldPermission", oldPermissionPrefix + "add");
		PageData permissionUpdate = PageData.getCommanInstance(3);
		permissionUpdate.put("permission", permissionPrefix+"update");
		permissionUpdate.put("name", menuName + "-修改");
		permissionUpdate.put("oldPermission", oldPermissionPrefix + "update");
		PageData permissionDelete = PageData.getCommanInstance(3);
		permissionDelete.put("permission", permissionPrefix+"delete");
		permissionDelete.put("name", menuName + "-删除");
		permissionDelete.put("oldPermission", oldPermissionPrefix + "delete");
		PageData permissionView = PageData.getCommanInstance(3);
		permissionView.put("permission", permissionPrefix+"view");
		permissionView.put("name", menuName + "-查看");
		permissionView.put("oldPermission", oldPermissionPrefix + "view");
		List<PageData> list = new ArrayList<>(4);
		list.add(permissionAdd);
		list.add(permissionUpdate);
		list.add(permissionDelete);
		list.add(permissionView);
		dao.insertBatch("com.wuliaozhiyuan.mapper.system.SysPermissionMapper.updateCrudButtonPermissionByPermission", list, false);
	}
	/**
	 * 删除
	 * @param pd
	 */
	@Override
	public void deleteSysPermission(PageData pd) {
		//先查询是否有子菜单，如果有则不能删除
		pd.put("parentId", pd.getLong("id"));
		pd.put("resourceType", "menu");
		List<SysPermission> sysPermissions = this.listSysPermissionByParentId(pd);
		if(sysPermissions.size() > 0){
			throw new RuntimeException("有子菜单不能删除");
		}
		//县查出权限permission字段，因为，然后根据这个字段的前缀，删除菜单以及菜单有关的增删改查权限
		SysPermission sysPermission = this.getSysPermissionById(pd.getLong("id"));
		//删除权限角色关系表中的有关指定权限前缀的记录
		String prefix = sysPermission.getPermission().split(":")[0];
		pd.put("permissionPrefix", prefix + ":");
		sysPermissionMapper.deleteRolePermissionByPermissionPrefix(pd);
		//删除权限菜单
		sysPermissionMapper.deleteByPermissionPrefix(pd);
	}
}
