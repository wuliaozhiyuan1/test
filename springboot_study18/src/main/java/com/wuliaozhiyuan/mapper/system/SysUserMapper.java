package com.wuliaozhiyuan.mapper.system;

import java.util.List;

import com.wuliaozhiyuan.bean.shiro.SysUser;
import com.wuliaozhiyuan.bean.system.vo.SysUserVo;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;

/**
 * logMapper
 * @author wuliaozhiyuan
 *
 */
public interface SysUserMapper {
	/**
	 * 分页查询站内信列表
	 * @param page
	 * @return
	 * @author shuyy
	 * @date 2017年12月3日
	 */
	public List<PageData> userListPage(Page page);
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
