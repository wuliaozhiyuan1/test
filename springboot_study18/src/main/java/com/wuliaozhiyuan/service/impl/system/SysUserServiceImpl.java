package com.wuliaozhiyuan.service.impl.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliaozhiyuan.bean.shiro.SysUser;
import com.wuliaozhiyuan.bean.system.SysUserRole;
import com.wuliaozhiyuan.bean.system.vo.SysUserVo;
import com.wuliaozhiyuan.config.datasouce.dynamic.TargetDataSource;
import com.wuliaozhiyuan.jpa.repository.SysUserRepository;
import com.wuliaozhiyuan.mapper.system.SysUserMapper;
import com.wuliaozhiyuan.service.system.SysUserRoleService;
import com.wuliaozhiyuan.service.system.SysUserService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Tools;
/**
 * 
 * @author wuliaozhiyuan
 *
 */
@Service
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	@Resource  
    private SysUserRepository sysUserRepository;  
	/**
     * 通过username查找用户信息
     *  @param username 用户名
     *  @return
     */
	@Override
	@TargetDataSource("ds1")
	public SysUser findByUsername(String username) {
		return sysUserRepository.findByUsername(username);
	}
	/**
	 * 查询用户
	 * @return
	 * @throws Exception
	 */
	@TargetDataSource("ds1")
	@Override
	public List<PageData> listUser(Page page) throws Exception {
		return sysUserMapper.userListPage(page);
	}
	/**
	 * 查询用户名数量，判断用户名是否已经存在
	 * @param username
	 * @return
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	@TargetDataSource("ds1")
	@Override
	public Integer hasUsername(String username) {
		return sysUserMapper.hasUsername(username);
	}
	/**
	 * 保存用户
	 * @param sysUserVo
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	public void save(SysUserVo sysUserVo){
		SysUser sysUser = sysUserVo.getSysUser();
		String username = sysUser.getUsername();
		String password = sysUser.getPassword();
		Map<String, String> md5 = Tools.md5(username, password);
		sysUser.setSalt(md5.get("salt").toString());
		sysUser.setPassword(md5.get("password").toString());
		sysUserMapper.save(sysUserVo);
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setRoleId(sysUserVo.getRoleId());
		sysUserRole.setUid(sysUserVo.getSysUser().getUid());
		sysUserRoleService.save(sysUserRole);
	}

}
