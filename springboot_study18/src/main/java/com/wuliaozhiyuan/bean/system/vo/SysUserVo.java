package com.wuliaozhiyuan.bean.system.vo;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.wuliaozhiyuan.bean.shiro.SysUser;
import com.wuliaozhiyuan.validate.SysUserSaveValidate;

public class SysUserVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3760117656275909377L;
	/**用户*/
	@Valid
	private SysUser sysUser;
	@NotNull(message="角色id不能为空", groups={SysUserSaveValidate.class})
	@Digits(groups={SysUserSaveValidate.class}, integer=2, fraction=0)
	private Long roleId;
	public SysUser getSysUser() {
		return sysUser;
	}
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
}
