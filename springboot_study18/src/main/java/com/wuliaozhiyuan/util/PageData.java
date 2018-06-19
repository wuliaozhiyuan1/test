package com.wuliaozhiyuan.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
/**
 * 前后台数据交换的包装类
 * @author wuliaozhiyuan
 *
 */
public class PageData extends HashMap {

	private static final long serialVersionUID = 1L;
	/**
	 * 每个request对应一个PageData
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public static PageData getInstance(){
		HttpServletRequest request2 = Tools.getHttpRequest();
		String pageDataAttributeName = "pd";
		if(request2.getAttribute(pageDataAttributeName) == null){
			//进入这里说明这是新请求
			PageData pageData = new PageData();
			request2.setAttribute(pageDataAttributeName, pageData);
			return pageData;
		}else{
			PageData pageData = (PageData)request2.getAttribute(pageDataAttributeName);
			return pageData;
		}
	}
	/**
	 * 获得普通的纯净的PageData对象，不包含request中的数据，也不是单例的。
	 * @param expectedSize 期望的PageData容量大小，为null则使用默认的。
	 * @return
	 */
	public static PageData getCommanInstance(Integer expectedSize){
		if(expectedSize == null){
			return new PageData(16);
		}else{
			return new PageData(Tools.capacity(expectedSize));
		}
	}
	private PageData(Integer capacity){
		super(capacity);
	}
	/**
	 * 构造方法初始化所有的request参数
	 */
	private PageData(){
		super();
		HttpServletRequest request = Tools.getHttpRequest();
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for(Entry<String, String[]> entry : entrySet){
			String key = entry.getKey();
			String[] value = entry.getValue();
			//如果只有参数名，没有参数值，则去掉这个没有意义的参数
			if(value != null && value.length > 0){
				//一个key对应多个参数
				if(value.length > 1){
					//这个参数是否有意义，如果value数组中的值都是""则没有意义，则不作为参数
					boolean flag = false;
					List<String> valueList = new ArrayList<>();
					for(String s : value){
						s = s.trim();
						if(!"".equals(s)){
							flag = true;
							valueList.add(s);
						}
					}
					if(flag){
						this.put(key, valueList);
					}
				}else{
					String s = value[0].trim();
					//一个key只有一个参数
					//boolean类型转换
					if("true".equals(s)){
						this.put(key, true);
					}else if("false".equals(s)){
						this.put(key, false);
					}else if(!"".equals(s)){
						this.put(key, s);
					}
				}
			}
		}
	}
	/**
	 * 获得String类型value
	 * @param key
	 * @return
	 */
	public String getString(Object key){
		return this.get(key) == null ? null : this.get(key).toString();
	}
	/**
	 * 获得Integer类型value
	 * @param key
	 * @return
	 */
	public Integer getInteger(Object key){
		return this.get(key) == null ? null : Integer.parseInt(this.get(key).toString());
	}
	public Long getLong(Object key){
		return this.get(key) == null ? null : Long.parseLong(this.get(key).toString());
	}
	/**
	 * 获得float类型value
	 * @param key
	 * @return
	 */
	public Float getFloat(Object key){
		return this.get(key) == null ? null : Float.parseFloat(this.get(key).toString());
	}
	/**
	 * 获得double类型value
	 * @param key
	 * @return
	 */
	public Double getDouble(Object key){
		return this.get(key) == null ? null : Double.parseDouble(this.get(key).toString());
	}
	/**
	 * 获得StringList类型value
	 * @param key
	 * @return
	 */
	public List<String> getListString(Object key){
		return this.get(key) == null ? null : (List<String>)this.get(key);
	}
	/**
	 * 获得IntegerList类型value
	 * @param key
	 * @return
	 */
	public List<Integer> getListInteger(Object key){
		return this.get(key) == null ? null : Tools.listStringToInteger((List<String>)this.get(key));
	}
	/**
	 * 获得FloatList类型value
	 * @param key
	 * @return
	 */
	public List<Float> getListFloat(Object key){
		return this.get(key) == null ? null : Tools.listStringToFloat((List<String>)this.get(key));
	}
	/**
	 * 获得Boolean类型的value
	 * @param key
	 * @return
	 */
	public boolean getBoolean(Object key){
		return this.get(key) == null ? null : Boolean.parseBoolean(this.get(key).toString());
	}
	/**
	 * 将传入的对象的所有成员变量加入到PageData中
	 * @param obj
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void addObjectField(Object obj) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field : fields){
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			//禁用访问控制，允许访问私有变量
			field.setAccessible(true);
			//静态成员变量不添加
			if(!isStatic){
				String name = field.getName();
				Object value = field.get(obj);
				if(value != null){
					this.put(name, value);
				}
			}
		}
	}
	
}
