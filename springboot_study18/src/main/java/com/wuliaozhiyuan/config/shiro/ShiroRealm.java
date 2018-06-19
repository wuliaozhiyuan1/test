package com.wuliaozhiyuan.config.shiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.wuliaozhiyuan.bean.shiro.SysPermission;
import com.wuliaozhiyuan.bean.shiro.SysUser;
import com.wuliaozhiyuan.service.system.SysPermissionService;
import com.wuliaozhiyuan.service.system.SysUserService;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Tools;
/**
 * 
 * @author wuliaozhiyuan
 *
 */
public class ShiroRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private SysUserService sysUserService;

	@Autowired
	private SysPermissionService sysPermissionService;

	/**
	 * 认证信息.(身份验证) : Authentication 是用来验证用户身份
	 * 
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.info("MyShiroRealm.doGetAuthenticationInfo()");
		// 获取用户的输入的账号.
		String username = (String) token.getPrincipal();
		System.out.println(token.getCredentials());

		// 通过username从数据库中查找 User对象，如果找到，没找到.
		// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		SysUser sysUser = sysUserService.findByUsername(username);
		logger.info("----->>sysUser=" + sysUser);
		if (sysUser == null) {
			return null;
		}

		/*
		 * 获取权限信息:这里没有进行实现， 请自行根据UserInfo,Role,Permission进行实现；
		 * 获取之后可以在前端for循环显示所有链接;
		 */
		// userInfo.setPermissions(userService.findPermissions(user));

		// 账号判断;

		// 加密方式;
		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				// 用户名
				sysUser,
				// 密码
				sysUser.getPassword(), 
				// salt=username+salt
				ByteSource.Util.bytes(sysUser.getCredentialsSalt()), 
				// realm name
				getName() 
		);

		// 明文: 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
		// SimpleAuthenticationInfo authenticationInfo = new
		// SimpleAuthenticationInfo(
		// userInfo, //用户名
		// userInfo.getPassword(), //密码
		// getName() //realm name
		// );

		return authenticationInfo;
	}

	/**
	 * 此方法调用 hasRole,hasPermission的时候才会进行回调.
	 * 
	 * 权限信息.(授权): 1、如果用户正常退出，缓存自动清空； 2、如果用户非正常退出，缓存自动清空；
	 * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。 （需要手动编程进行实现；放在service进行调用）
	 * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例， 调用clearCached方法；
	 * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
	 * 
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*
		 * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行， 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
		 * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了， 缓存过期之后会再次执行。
		 */
		logger.info("权限配置-->ShiroRealm.doGetAuthorizationInfo()");

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		SysUser sysUser = (SysUser) principals.getPrimaryPrincipal();
		
		// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		// UserInfo userInfo = userInfoService.findByUsername(username)

		// 权限单个添加;
		// 或者按下面这样添加
		// 添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色
		// authorizationInfo.addRole("admin");
		// 添加权限
		// authorizationInfo.addStringPermission("userInfo:query");

		/*
		 * 这段代码使用的jpa级联查询权限，我觉得效率不好，就去掉了，改成了，使用mybatis一条语句查所有权限。 ///在认证成功之后返回.
		 * //设置角色信息. //支持 Set集合, //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要 //
		 * List<Role> roleList=user.getRoleList(); // for (Role role : roleList)
		 * { // info.addStringPermissions(role.getPermissionsName()); // }
		 * for(SysRole role:userInfo.getRoleList()){
		 * authorizationInfo.addRole(role.getRole()); for(SysPermission
		 * p:role.getPermissions()){
		 * authorizationInfo.addStringPermission(p.getPermission()); } }
		 */
		PageData pageData = PageData.getCommanInstance(2);
		pageData.put("uid", sysUser.getUid());
		pageData.put("available", true);
		List<SysPermission> sysPermissions = sysPermissionService.listSysPermissionByUserId(pageData);
		//初始用户的各个权限属性
		Map<String, SysPermission> menuPermissions = Tools.newHashMapWithExpectedSize(sysPermissions.size());
		Map<String, SysPermission> crudButtonPermissions = Tools.newHashMapWithExpectedSize(sysPermissions.size());
		Map<String, SysPermission> buttonPermissions = Tools.newHashMapWithExpectedSize(sysPermissions.size());
		for(SysPermission sysPermission : sysPermissions){
			if("menu".equals(sysPermission.getResourceType())){
				menuPermissions.put(sysPermission.getPermission(), sysPermission);
			}else if("crudButton".equals(sysPermission.getResourceType())){
				crudButtonPermissions.put(sysPermission.getPermission(), sysPermission);
			}else if("button".equals(sysPermission.getResourceType())){
				buttonPermissions.put(sysPermission.getPermission(), sysPermission);
			}
		}
		sysUser.setMenuPermissions(menuPermissions);
		sysUser.setCrudButtonPermissions(crudButtonPermissions);
		sysUser.setButtonPermissions(buttonPermissions);
		Session session = SecurityUtils.getSubject().getSession();
		//进入了授权方法中，则说明已经认证成功了，可以直接往session里面放user
		session.setAttribute("user", sysUser);
		for (SysPermission sysPermission : sysPermissions) {
			authorizationInfo.addStringPermission(sysPermission.getPermission());
		}
		//构造菜单树
		List<SysPermission> menuList = new ArrayList<>();
		for(Entry<String, SysPermission> entry : menuPermissions.entrySet()){
			menuList.add(entry.getValue());
		}
		SysPermission root = this.structureTree3(menuList);
		session.setAttribute("permissionTree", root);
		// 设置权限信息.
		// authorizationInfo.setStringPermissions(getStringPermissions(userInfo.getRoleList()));

		return authorizationInfo;
	}

	/**
	 * 构造权限树
	 * @return 返回这个树的根节点
	 */
	public SysPermission structureTree(List<SysPermission> sysPermissions) {
		SysPermission rootSysPermission = new SysPermission();
		SysPermission node = rootSysPermission;
		for (SysPermission sysPermission : sysPermissions) {
			String parentIds = sysPermission.getParentIds();
			String[] parentIdArray = parentIds.split("/");
			//构造一个空壳的树结构
			for (int i = 0; i < parentIdArray.length; i++) {
				SysPermission tmp = node.getSubSysPermissions().get(Long.parseLong(parentIdArray[i]));
				if (tmp == null) {
					SysPermission s = new SysPermission();
					node.getSubSysPermissions().put(Long.parseLong(parentIdArray[i]), s);
					node = s;
				}else{
					node = tmp;
				}
				
			}
			//添加真正的节点
			if(node.getSubSysPermissions().get(sysPermission.getId()) != null){
				//进入了这里，说明之前已经在这个节点上构造了一个空壳的节点
				//替换这个空壳节点
				SysPermission sysPermission2 = node.getSubSysPermissions().get(sysPermission.getId());
				sysPermission.setSubSysPermissions(sysPermission2.getSubSysPermissions());
				node.getSubSysPermissions().put(sysPermission.getId(), sysPermission);
			}else{
				node.getSubSysPermissions().put(sysPermission.getId(), sysPermission);
			}
			node = rootSysPermission;
		}
		if(rootSysPermission.getSubSysPermissions().size() != 0){
			//因为，顶级菜单的还有一个上级菜单0，所以这里直接把顶级菜单的上级菜单返回
			rootSysPermission = rootSysPermission.getSubSysPermissions().get((long)0);
		}
		return rootSysPermission;
	}
	/**
	 * 构造权限树
	 * @return 返回这个树的根节点
	 */
	public SysPermission structureTree2(List<SysPermission> sysPermissions) {
		//把sysPermission装进map，便于通过id，查找到sysPermissions;
		Map<Long, SysPermission> map = new HashMap<Long, SysPermission>((int)(sysPermissions.size() / 0.75 + 1));
		Comparator<Long> comparator = new ValueComparator(map);
		SysPermission sysPermission2 = new SysPermission();
		sysPermission2.setSubSysPermissions(new TreeMap<>(comparator));
		sysPermission2.setMenuOrder(1);
		map.put((long)0, sysPermission2);
		for(SysPermission sysPermission : sysPermissions){
			sysPermission.setSubSysPermissions(new TreeMap<>(comparator));
			map.put(sysPermission.getId(), sysPermission);
		}
		SysPermission rootSysPermission = map.get((long)0);
		SysPermission node = rootSysPermission;
		for (SysPermission sysPermission : sysPermissions) {
			String parentIds = sysPermission.getParentIds();
			String[] parentIdArray = parentIds.split("/");
			//构造树结构
			for (int i = 0; i < parentIdArray.length; i++) {
				long sysPermissionId = Long.parseLong(parentIdArray[i]);
				//剔除id为0的权限
				if(sysPermissionId == 0){
					continue;
				}
				SysPermission tmp = node.getSubSysPermissions().get(sysPermissionId);
				if (tmp == null) {
					SysPermission s = map.get(Long.parseLong(parentIdArray[i]));
					node.getSubSysPermissions().put(Long.parseLong(parentIdArray[i]), s);
					node = s;
				}else{
					node = tmp;
				}
			}
			if(node.getSubSysPermissions().get(sysPermission.getId()) == null){
				//树结构还没有添加此节点，则添加节点
				node.getSubSysPermissions().put(sysPermission.getId(), sysPermission);
			}
			node = rootSysPermission;
			String json = JSON.toJSONString(node);
			System.out.println(json);
		}
		String json = JSON.toJSONString(node);
		System.out.println(json);
		return rootSysPermission;
	}
	public SysPermission structureTree3(List<SysPermission> sysPermissions) {
		//把sysPermission装进map，便于通过parentsId结构，再通过id，查找到sysPermissions;
		Map<Long, SysPermission> map = new HashMap<Long, SysPermission>(Tools.capacity(sysPermissions.size()));
		SysPermission sysPermission2 = new SysPermission();
		sysPermission2.setSubSysPermissions(new HashMap<>(Tools.capacity(16)));
		sysPermission2.setMenuOrder(1);
		map.put((long)0, sysPermission2);
		for(SysPermission sysPermission : sysPermissions){
			sysPermission.setSubSysPermissions(new HashMap<>(Tools.capacity(16)));
			map.put(sysPermission.getId(), sysPermission);
		}
		SysPermission rootSysPermission = map.get((long)0);
		SysPermission node = rootSysPermission;
		for (SysPermission sysPermission : sysPermissions) {
			String parentIds = sysPermission.getParentIds();
			String[] parentIdArray = parentIds.split("/");
			//构造树结构
			for (int i = 0; i < parentIdArray.length; i++) {
				long sysPermissionId = Long.parseLong(parentIdArray[i]);
				//剔除id为0的权限
				if(sysPermissionId == 0){
					continue;
				}
				SysPermission tmp = node.getSubSysPermissions().get(sysPermissionId);
				if (tmp == null) {
					SysPermission s = map.get(Long.parseLong(parentIdArray[i]));
					node.getSubSysPermissions().put(Long.parseLong(parentIdArray[i]), s);
					node = s;
				}else{
					node = tmp;
				}
			}
			if(node.getSubSysPermissions().get(sysPermission.getId()) == null){
				//树结构还没有添加此节点，则添加节点
				node.getSubSysPermissions().put(sysPermission.getId(), sysPermission);
			}
			node = rootSysPermission;
			String json = JSON.toJSONString(node);
			System.out.println(json);
		}
		String json = JSON.toJSONString(node);
		System.out.println(json);
		/*将所有的SysPermission，包括root的根节点：id为0的虚构的节点，因为根节点的subSysPermissions就是第一层菜单，也需要排序。
		*它们的subSysPermissions的map都进行排序，按照menuOrder。*/
		for(Entry<Long, SysPermission> e : map.entrySet()){
			SysPermission sysPermission = e.getValue();
			Map<Long, SysPermission> subSysPermissions = e.getValue().getSubSysPermissions();
			sysPermission.setSubSysPermissions(this.sortMapByValue(subSysPermissions));
		}
		return rootSysPermission;
	}
	class ValueComparator implements Comparator<Long>{
		private Map<Long, SysPermission> map;
		public ValueComparator(Map<Long, SysPermission> map2) {
			this.map = map2;
		}
		@Override
		public int compare(Long o1, Long o2) {
			SysPermission sysPermission1 = map.get(o1);
			SysPermission sysPermission2 = map.get(o2);
			if(sysPermission1.getMenuOrder() == null || sysPermission2.getMenuOrder() == null){
				return 0;
			}
			return sysPermission1.getMenuOrder() - sysPermission2.getMenuOrder();
		}
	}
	
	  /**
     * 使用 Map按value进行排序
     * @param map
     * @return
     */
    public Map<Long, SysPermission> sortMapByValue(Map<Long, SysPermission> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return new LinkedHashMap<Long, SysPermission>();
        }
        Map<Long, SysPermission> sortedMap = new LinkedHashMap<Long, SysPermission>();
        List<Map.Entry<Long, SysPermission>> entryList = new ArrayList<Map.Entry<Long, SysPermission>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());
        Iterator<Map.Entry<Long, SysPermission>> iter = entryList.iterator();
        Map.Entry<Long, SysPermission> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
    class MapValueComparator implements Comparator<Map.Entry<Long, SysPermission>> {
		
        @Override
        public int compare(Entry<Long, SysPermission> me1, Entry<Long, SysPermission> me2) {
        	return me1.getValue().getMenuOrder().compareTo(me2.getValue().getMenuOrder());
        }
    }
    
}
