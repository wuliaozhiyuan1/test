package com.wuliaozhiyuan.bean.system;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * 用户角色对应关系表
 * @author shuyy
 * @date 2017年12月7日
 */
public class SysUserRole implements Serializable{
	private static final long serialVersionUID = -3011376102060451954L;
	/**角色id*/
	private Long roleId;
	/**用户id*/
	private Long uid;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
}
