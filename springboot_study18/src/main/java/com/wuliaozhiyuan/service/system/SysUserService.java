package com.wuliaozhiyuan.service.system;

import java.util.List;

import com.wuliaozhiyuan.bean.shiro.SysUser;
import com.wuliaozhiyuan.bean.system.vo.SysUserVo;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
/**
 * 用户Service
 * @author wuliaozhiyuan
 *
 */
public interface SysUserService {
	/**
     * 通过username查找用户信息
     *  @param username 用户名
     *  @return
     */
    public SysUser findByUsername(String username);  
    
    /**
	 * 查询用户
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listUser(Page page) throws Exception;
	/**
	 * 查询用户名数量，判断用户名是否已经存在
	 * @param username
	 * @return
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	public Integer hasUsername(String username);
	/**
	 * 保存用户
	 * @param sysUserVo
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	public void save(SysUserVo sysUserVo);
}
