package com.wuliaozhiyuan.service.impl.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliaozhiyuan.bean.shiro.SysRole;
import com.wuliaozhiyuan.dao.base.Dao;
import com.wuliaozhiyuan.mapper.system.SysRoleMapper;
import com.wuliaozhiyuan.service.system.SysRoleService;
import com.wuliaozhiyuan.util.CustomException;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Tools;
/**
 * 角色Service
 * @author wuliaozhiyuan
 *
 */
@Service
public class SysRoleServiceImpl implements SysRoleService{
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private Dao dao;
	
	/**
	 * 查询所有的角色
	 * @param uid
	 * @return
	 */
	@Override
	public List<SysRole> listRole(){
		return sysRoleMapper.listRole();
	}
	
	@Override
	public void save(PageData pd) {
		sysRoleMapper.save(pd);
	}
	
	/**
	 * 获得角色，通过角色id
	 * @param pd
	 * @return
	 */
	@Override
	public SysRole getRoleById(PageData pd){
		return sysRoleMapper.getRoleById(pd.getLong("id"));
	}
	/**
	 * 更新角色通过角色id
	 */
	@Override
	public void update(PageData pd) {
		sysRoleMapper.update(pd);
	}
	/**
	 * 删除角色，通过角色id
	 */
	@Override
	public void delete(PageData pageData) {
		//查询该角色是否已被用户使用，已使用，则不能删除
		pageData.put("roleId", pageData.getLong("id"));
		Integer num = sysRoleMapper.getUserNumByroleId(pageData.getLong("id"));
		if(num > 0){
			//给角色已被用户使用，不能删除
			throw new CustomException("该角色以备用户使用，不能删除");
		}
		//删除角色
		sysRoleMapper.deleteById(pageData);
	}
	/**
	 * 所有的菜单，用于构造ztree树，返回格式为：
	 * [{
	 * id:id,
	 * name:name,
	 * pId: parentId,
	 * checked:true
	 * },..]
	 * @param pd {}
	 * @return
	 */
	@Override
	public List<PageData> listMenu(Long roleId) {
		List<PageData> listMenu = sysRoleMapper.listMenu();
		List<PageData> roleMenus = sysRoleMapper.listMenuByRoleId(roleId);
		//把角色下的菜单id，放到hashmap中，便于查找，判定角色是否已有某个菜单的权限
		Map<Long, Long> roleMenuIdMap = Tools.newHashMapWithExpectedSize(roleMenus.size());
		//这个list，用于把角色的菜单id传入前端，用于之后的菜单id权限修改
//		List<Long> menuIds = new ArrayList<>();
		for(PageData pd : roleMenus){
			roleMenuIdMap.put(pd.getLong("id"), pd.getLong("id"));
//			menuIds.add(pd.getLong("id"));
		}
		//进行格式转换，转换成ztree需要的格式
		for(PageData pd : listMenu){
			pd.put("pId", pd.remove("parentId"));
			Long id = pd.getLong("id");
			if(roleMenuIdMap.get(id) != null){
				//该角色已有这个菜单权限
				pd.put("checked", true);
			}
		}
		Tools.getPageData().put("menuIds", roleMenuIdMap);
		return listMenu;
	}
	
	/**
	 * 保存角色的菜单权限
	 * @param param 格式为{id:roleId, menuId:List}
	 * @throws Exception 
	 */
	@Override
	public void saveMenuqx(Map<String, Object> param) throws Exception{
		//对比之前的menuIds和之后的menuIds，找出需要删除，需要增加的MenuIds
		Map<String, String> oldMenuIds = (Map<String, String>) param.get("oldMenuIds");
		List<String> menuIds = (List<String>) param.get("menuIds");
		List<Long> tempList = new ArrayList<>();
		for(int i = 0; i < menuIds.size(); i++){
			String id = menuIds.get(i);
			if(oldMenuIds.get(id) != null){
				//说明是之前就有的菜单权限，不进行数据库操作
				oldMenuIds.remove(id);
			}else{
				long menuId = Long.parseLong(id);
				tempList.add(menuId);
			}
		}
		//走到这里，oldMenuIds存的id，则需要进行数据库删除操作，tempList则需要进行数据库新增操作
		Long roleId = Long.parseLong(param.get("id").toString());
		List<PageData> deletePermission = new ArrayList<>();
		for(Entry<String, String> e : oldMenuIds.entrySet()){
			long id = Long.parseLong(e.getKey());
			PageData pd = PageData.getCommanInstance(3);
			pd.put("roleId", roleId);
			pd.put("permissionId", id);
			deletePermission.add(pd);
		}
		List<PageData> addPermission = new ArrayList<>();
		for(long id : tempList){
			PageData pd = PageData.getCommanInstance(3);
			pd.put("roleId", roleId);
			pd.put("permissionId", id);
			addPermission.add(pd);
		}
		dao.insertBatch("com.wuliaozhiyuan.mapper.system.SysRoleMapper.deleteRoleMenu", deletePermission, false);
		dao.insertBatch("com.wuliaozhiyuan.mapper.system.SysRoleMapper.saveRoleMenu", addPermission, true);
	}
	
	/**
	 * 菜单的增删改查权限，列举出所有的菜单，用于构造ztree树，返回格式为：
	 * [{
	 * id:id,
	 * name:name,
	 * pId: parentId,
	 * checked:true
	 * },..]
	 * @param pd {}
	 * @param type 权限类型：add、delete、update、view
	 * @return
	 */
	@Override
	public List<PageData> listCrudPermission(Long roleId, String type) {
		//查出指定的类型的所有权限，然后判定这个权限的标识符的前缀是否和菜单前缀一致
		PageData pageData = PageData.getCommanInstance(2);
		pageData.put("roleId", roleId);
		pageData.put("permissionType", type);
		
		List<PageData> listMenu = sysRoleMapper.listMenu();
		List<PageData> crudPermissions = sysRoleMapper.listCrudPermissionByRoleId(pageData);
		//把角色下的菜单标识符、id，放到hashmap中，便于查找，判定角色是否已有某个菜单的权限
		Map<String, Long> roleMenuMap = Tools.newHashMapWithExpectedSize(crudPermissions.size());
		for(PageData pd : crudPermissions){
			roleMenuMap.put(pd.getString("permission"), pd.getLong("id"));
		}
		//进行格式转换，转换成ztree需要的格式
		for(PageData pd : listMenu){
			pd.put("pId", pd.remove("parentId"));
			pd.put("permission", pd.getString("permission").split(":")[0] + ":" + type);
			if(null != roleMenuMap.get(pd.get("permission"))){
				//该角色已有这个菜单权限
				pd.put("checked", true);
			}
		}
		//这个Map，用于把角色的菜单id传入前端，用于之后的菜单id权限修改
		Map<String, String> roleMenuIdMap = Tools.newHashMapWithExpectedSize(crudPermissions.size());
		for(PageData pd : crudPermissions){
			roleMenuIdMap.put(pd.getString("permission"), pd.getString("permission"));
		}
		Tools.getPageData().put("crudPermissions", roleMenuIdMap);
		return listMenu;
	}
	/**
	 * 保存角色菜单下的增删改查权限
	 * @param param 格式为{id:roleId, menuId:List}
	 * @throws Exception 
	 */
	@Override
	public void saveCrudPermission(Map<String, Object> param) throws Exception{
		//对比之前的menuIds和之后的menuIds，找出需要删除，需要增加的MenuIds
		Map<String, String> oldCrudPermissions = (Map<String, String>) param.get("oldCrudPermissions");
		List<String> newCrudPermissions = (List<String>) param.get("newCrudPermissions");
		List<String> tempList = new ArrayList<>();
		for(int i = 0; i < newCrudPermissions.size(); i++){
			String permission = newCrudPermissions.get(i);
			if(oldCrudPermissions.get(permission) != null){
				//说明是之前就有的菜单权限，不进行数据库操作
				oldCrudPermissions.remove(permission);
			}else{
				tempList.add(permission);
			}
		}
		//走到这里，oldCrudPermissions存的permission，则需要进行数据库删除操作，tempList则需要进行数据库新增操作
		Long roleId = Long.parseLong(param.get("id").toString());
		List<PageData> deletePermission = new ArrayList<>();
		for(Entry<String, String> e : oldCrudPermissions.entrySet()){
			String permission = e.getKey();
			PageData pd = PageData.getCommanInstance(3);
			pd.put("roleId", roleId);
			pd.put("permission", permission);
			deletePermission.add(pd);
		}
		List<PageData> addPermission = new ArrayList<>();
		for(String permission : tempList){
			PageData pd = PageData.getCommanInstance(3);
			pd.put("roleId", roleId);
			pd.put("permission", permission);
			addPermission.add(pd);
		}
		dao.insertBatch("com.wuliaozhiyuan.mapper.system.SysRoleMapper.deleteRolePermissionByPermission", deletePermission, false);
		dao.insertBatch("com.wuliaozhiyuan.mapper.system.SysRoleMapper.saveRolePermissionByPermission", addPermission, true);
	}
}
