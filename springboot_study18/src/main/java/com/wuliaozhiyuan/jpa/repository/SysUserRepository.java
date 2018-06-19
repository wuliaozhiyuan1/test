package com.wuliaozhiyuan.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import com.wuliaozhiyuan.bean.shiro.SysUser;

/** 
 * SysUser持久化类; 
 * @author shuyy 
 */  
public interface SysUserRepository extends CrudRepository<SysUser,Long>{  
     
	/**
	 * 通过username查找用户信息
	 * @param username 用户名
	 * @return
	 * */  
    public SysUser findByUsername(String username);  
     
}  
