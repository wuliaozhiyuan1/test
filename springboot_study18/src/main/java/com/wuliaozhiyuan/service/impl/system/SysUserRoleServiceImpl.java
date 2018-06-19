package com.wuliaozhiyuan.service.impl.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliaozhiyuan.bean.system.SysUserRole;
import com.wuliaozhiyuan.mapper.system.SysUserRoleMapper;
import com.wuliaozhiyuan.service.system.SysUserRoleService;
/**
 * 用户角色关系对应Service对应类
 * @author shuyy
 * @date 2017年12月7日
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {
	
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	/**
	 * 保存
	 * @param sysUserRole
	 */
	@Override
	public void save(SysUserRole sysUserRole) {
		sysUserRoleMapper.save(sysUserRole);
	}

}
