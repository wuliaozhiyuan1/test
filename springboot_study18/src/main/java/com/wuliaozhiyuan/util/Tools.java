package com.wuliaozhiyuan.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.wuliaozhiyuan.bean.shiro.SysUser;

/**
 * 工具类
 * 
 * @author wuliaozhiyuan
 *
 */
public class Tools {

	/**
	 * 获取request对象
	 * 
	 * @return
	 */
	public static HttpServletRequest getHttpRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	/**
	 * 获取session对象
	 * 
	 * @return
	 */
	public static Session getSession() {
		Session session = SecurityUtils.getSubject().getSession();
		return session;
	}

	/**
	 * StringList转IntegerList
	 * 
	 * @param list
	 * @return
	 */
	public static List<Integer> listStringToInteger(List<String> list) {
		List<Integer> intList = new ArrayList<>();
		for (String s : list) {
			intList.add(Integer.parseInt(s));
		}
		return intList;
	}

	/**
	 * StringList转FloatList
	 * 
	 * @param list
	 * @return
	 */
	public static List<Float> listStringToFloat(List<String> list) {
		List<Float> floatList = new ArrayList<>();
		for (String s : list) {
			floatList.add(Float.parseFloat(s));
		}
		return floatList;
	}

	/**
	 * 获得PageData对象
	 * 
	 * @return
	 */
	public static PageData getPageData() {
		return PageData.getInstance();
	}

	/**
	 * 得到ModelAndView
	 * 
	 * @return
	 */
	public static ModelAndView getModelAndView() {
		return new ModelAndView();
	}

	/**
	 * 获取基地址
	 * 
	 * @return
	 */
	public static String getBaseUrl() {
		HttpServletRequest request = Tools.getHttpRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		return basePath;
	}

	/**
	 * 获取Mapp，给定初始化容量
	 * 
	 * @param expectedSize
	 * @return
	 */
	public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
		return new HashMap<K, V>(capacity(expectedSize));
	}

	/**
	 * 计算Map里的初始化数组长度，给定map容量
	 * 
	 * @param expectedSize 这传进去个7，返回的是10；
	 * @return
	 */
	public static int capacity(int expectedSize) {
		int minExpectedSize = 3;											
		if (expectedSize < minExpectedSize) {
			if (expectedSize < 0) {
				throw new RuntimeException("初始化容量不能为负数");
			}
			return expectedSize + 1;
		}
		int t = 2;
		if (expectedSize < 1 << (Integer.SIZE - t)) {
			return (int) ((float) expectedSize / 0.75F + 1.0F);
		}
		return Integer.MAX_VALUE;
	}

	public static Logger getLogger() {
		return LoggerFactory.getLogger(Tools.class);
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr() throws Exception {
		HttpServletRequest request = Tools.getHttpRequest();
		String ip = request.getHeader("X-Real-IP");
		String unknown = "unknown";
		if (!StringUtils.isEmpty(ip) && !unknown.equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isEmpty(ip) && !unknown.equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
	/**
	 * 获得当前登录用户
	 * @return
	 */
	public static SysUser getCurrentUser(){
		SysUser sysuser = (SysUser)Tools.getSession().getAttribute("user");
		return sysuser;
	}
	/**
	 * md5加密
	 * //admin + salt + password
	 * @param username
	 * @param password
	 * @return
	 * @author shuyy
	 * @date 2017年12月7日
	 */
	public static Map<String, String> md5(String username, String password){
		String algorithmName = "md5";  
    	String salt1 = username;  
    	String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();  
    	int hashIterations = 2;  
    	SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);  
    	String encodedPassword = hash.toHex(); 
    	Map<String, String> result = new HashMap<>(Tools.capacity(2));
    	result.put("password", encodedPassword);
    	result.put("salt", salt2);
    	return result;
	}
}
