package com.wuliaozhiyuan.service.impl.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wuliaozhiyuan.bean.system.Dictionary;
import com.wuliaozhiyuan.bean.system.vo.DictionaryVo;
import com.wuliaozhiyuan.config.datasouce.dynamic.TargetDataSource;
import com.wuliaozhiyuan.mapper.system.DictionaryMapper;
import com.wuliaozhiyuan.service.system.DictionaryService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.RedisService;
import com.wuliaozhiyuan.util.Tools;
/**
 * 字典Service实现类
 * @author shuyy
 * @date 2017年11月26日
 */
@Service
public class DictionaryServiceImpl implements DictionaryService{
	@Autowired
	private DictionaryMapper dictionaryMapper;
	
	@Autowired
    RedisService redisService;
	
	/**
	 * 获取子字典，通过父字典id。返回格式为一个ztree需要的格式
	 * @author wuliaozhiyuan
	 * @param pd
	 * @return 
	 * {id:id,
	 *  idParent:true,
	 *  name:name,
	 *  url:url,
	 *  target:targetFrame
	 *  }
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@TargetDataSource("ds1")
	@Override
	public List<PageData> listDictionaryByParentId(PageData pd) throws Exception {
		String idParam = "id";
		String parentIdParam = "parentId";
		if(pd.getLong(idParam) == null){
			pd.put(parentIdParam, 0);
		}else{
			pd.put(parentIdParam, pd.getLong(idParam));
		}
//		List<Dictionary> dictionarys = dictionaryMapper.listDictionaryByParentId(pd);
		List<Dictionary> dictionarys = new ArrayList<>();
		String listKey = "dictionary:parentId:" + pd.getString("parentId");
		String idKey = "dictionary:id";
		List<String> range = null;
		try {
			range = redisService.redisTemplate.opsForList().range(listKey, 0, -1);
			if(range.isEmpty()){
				dictionarys = dictionaryMapper.listDictionaryByParentId(pd);
				//存放缓存
				List<String> ids = new ArrayList<>();
				for (Dictionary dictionary : dictionarys) {
					ids.add(dictionary.getId().toString());
					Object object = redisService.redisTemplate.opsForHash().get(idKey, dictionary.getId().toString());
					if(object == null){
						redisService.redisTemplate.opsForHash().put(idKey, dictionary.getId().toString(), JSON.toJSON(dictionary));
					}
				}
				redisService.redisTemplate.opsForList().rightPushAll(listKey, ids);
			}else{
				for(String id : range){
					Object object = redisService.redisTemplate.opsForHash().get(idKey, id);
					Dictionary dictionary = null;
					if(object == null){
						dictionary = dictionaryMapper.getDictionaryById(Long.parseLong(id));
						redisService.redisTemplate.opsForHash().put(idKey, id, JSON.toJSON(dictionary));
					}else{
						dictionary = JSON.parseObject(object.toString(), Dictionary.class);
					}
					dictionarys.add(dictionary);
				}
			}
		} catch (Exception e) {
			Tools.getLogger().error("错误", e);;
			dictionarys = dictionaryMapper.listDictionaryByParentId(pd);
		}
		List<PageData> pageDataList = new ArrayList<>(dictionarys.size());
		for(Dictionary dictionary : dictionarys){
			PageData pageData = PageData.getCommanInstance(10);
			pageData.addObjectField(dictionary);
			String url = "dictionary/listDictionary.html?parentId=" + dictionary.getId();
			url = Tools.getBaseUrl() + url;
			String urlParam = "url";
			pageData.put(urlParam, url);
			String targetParam = "target";
			String targetValue = "treeFrame";
			pageData.put(targetParam, targetValue);
			pageDataList.add(pageData);
		}
		return pageDataList;
	}
	
	/**
	 * 查询字典列表，通过查询参数  
	 * @param page 分页参数：含有的查询参数有：{
	 * pd:{parentId:parentId(ps:optional), keywords:keywords(ps:optional.keywords可以是name，englistname， code)}
	 * }
	 * @return 
	 * @throws Exception
	 */
	@TargetDataSource("ds1")
//	@Cacheable(value="demoInfo", key="'dictionary:keywords:parentId:showCount:currentPage:' + "
//			+ "#page.getPd().get('keywords')  + ':' + #page.getPd().get('parentId') + ':' + #page.getShowCount() + ':' + #page.getCurrentPage() ") //缓存,这里没有指定key.  
	@Override
	public List<Dictionary> listDictionary(Page page) throws Exception{
		String parentIdParam = "parentId";
		if(page.getPd().getLong(parentIdParam) == null){
			page.getPd().put(parentIdParam, 0);
		}
		//缓存
		String keywords = page.getPd().getString("keywords"); 
		String parentId = page.getPd().getString("parentId"); 
		int showCount = page.getShowCount();
		int currentPage = page.getCurrentPage();
		List<Dictionary> dictionaryListPage = new ArrayList<>();
		String zsetKey = "{table:dictionary,parentId:" + parentId + ",keywords:" + keywords + "}";
		String parentIdKey = "{table:dictionary,parentId:" + parentId + "}";
		String countKey = "{table:dictionary,sumTotal:sumTotal,parentId:" + parentId + ",keywords:" + keywords +"}";
		//从缓存中取
		try {
			if((currentPage + 1 ) * showCount > 100){
				dictionaryListPage = dictionaryMapper.dictionaryListPage(page);
			}else{
				Set<String> range = redisService.redisTemplate.opsForZSet().range(zsetKey, (currentPage - 1) * showCount, currentPage * showCount - 1);
				if(range.isEmpty()){
					dictionaryListPage = dictionaryMapper.dictionaryListPage(page);
					//存入缓存
					for (Dictionary dictionary : dictionaryListPage) {
						redisService.redisTemplate.opsForZSet().add(zsetKey, dictionary.getId().toString(), dictionary.getOrders());
					}
					redisService.redisTemplate.opsForValue().set(countKey, page.getTotalResult() + "");
					//记录key，便于之后同步删除这个缓存
					redisService.redisTemplate.opsForList().rightPush(parentIdKey, zsetKey);
					redisService.redisTemplate.opsForList().rightPush(parentIdKey, countKey);
				}else{
					//缓存中有
					String idKey = "dictionary:id";
					for (String string : range) {
						Object object = redisService.redisTemplate.opsForHash().get(idKey, string);
						Dictionary dictionary = null;
						if(object == null){
							dictionary = dictionaryMapper.getDictionaryById(Long.parseLong(string));
							redisService.redisTemplate.opsForHash().put(idKey, string, JSON.toJSON(dictionary));
						}else{
							dictionary = JSON.parseObject(object.toString(), Dictionary.class);
						}
						dictionaryListPage.add(dictionary);
					}
					String countNum = redisService.redisTemplate.opsForValue().get(countKey);
					page.setTotalResult(Integer.parseInt(countNum));
					page.setTotalPage(Integer.parseInt(countNum)/showCount + 1);;
				}
				
			}
		} catch (Exception e) {
			Tools.getLogger().error("错误", e);
			dictionaryListPage = dictionaryMapper.dictionaryListPage(page);
		}

//		List<Dictionary> dictionaryListPage = dictionaryMapper.dictionaryListPage(page);
		return dictionaryListPage;
	}

	/**
	 * 获取字典对象，通过字典id
	 * @param id
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@TargetDataSource("ds1")
	@Override
	public Dictionary getDictionaryById(Long id){
		Dictionary dictionary = null;
		try {
			String idKey = "dictionary:id";
			Object object = redisService.redisTemplate.opsForHash().get(idKey, id.toString());
			if(object == null){
				dictionary = dictionaryMapper.getDictionaryById(id);
				redisService.redisTemplate.opsForHash().put(idKey, id, JSON.toJSON(dictionary));
			}else{
				dictionary = JSON.parseObject(object.toString(), Dictionary.class);
			}
		} catch (Exception e) {
			Tools.getLogger().error("错误", e);
			dictionary = dictionaryMapper.getDictionaryById(id);
		}
		return dictionary;
	}
	
	/**
	 * 查询code数量，通过code和parentId
	 * @param pageData 格式为：{code:code, parentId:parentId}
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@TargetDataSource("ds1")
	@Override
	public Integer countCodeNum(PageData pageData){
		return dictionaryMapper.countCodeNum(pageData);
	}
	
	/**
	 * 保存
	 * @param dictionary
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@Override
	public void save(DictionaryVo dictionaryVo){
		Dictionary dictionary = dictionaryVo.getDictionary();
		dictionary.setIsParent(false);
		dictionaryMapper.save(dictionary);
		if(!dictionaryVo.getParentDictionaryIsParent() && dictionary.getParentId() != 0){
			//说明父字典之前是没有子字典的，所以这里更新
			Dictionary dictionary2 = new Dictionary();
			dictionary2.setId(dictionary.getParentId());
			dictionary2.setIsParent(true);
			dictionaryMapper.update(dictionary2);
		}
		//删除缓存
//		过期两种缓存：1、String zsetKey = "{table:dictionary,parentId:" + parentId + ",keywords:" + keywords + "}";
//				  2、String listKey = "dictionary:parentId:" + parentId;
		Long parentId = dictionary.getParentId();
		String listKey = "dictionary:parentId:" + parentId;
		String parentIdKey = "{table:dictionary,parentId:" + parentId + "}";
			//删除所有
		try {
			redisService.redisTemplate.opsForList().trim(listKey, 1, 0);
			List<String> range = redisService.redisTemplate.opsForList().range(parentIdKey, 0, -1);
			for (String string : range) {
				redisService.redisTemplate.opsForZSet().removeRange(string, 0, -1);
			}
		} catch (Exception e) {
			Tools.getLogger().error("过期缓存失败", e);
		}
	}
	/**
	 * 保存修改
	 * @param dictionary
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@Override
	@CacheEvict(value="demoInfo", key="")
	public void update(Dictionary dictionary){
		dictionaryMapper.update(dictionary);
		//过期缓存
		//过期三种缓存
//			1、String zsetKey = "{table:dictionary,parentId:" + parentId + ",keywords:" + keywords + "}";
//		  	2、String listKey = "dictionary:parentId:" + parentId;
//			3、String IDKey= "dictionary:id";
		Long parentId = dictionary.getParentId();
		Long id = dictionary.getId();
		String IDKey= "dictionary:id";
		String listKey = "dictionary:parentId:" + parentId;
		String parentIdKey = "{table:dictionary,parentId:" + parentId + "}";
		try {
			redisService.redisTemplate.opsForHash().delete(IDKey, id.toString());
			redisService.redisTemplate.opsForList().trim(listKey, 1, 0);
			List<String> range = redisService.redisTemplate.opsForList().range(parentIdKey, 0, -1);
			for (String string : range) {
				redisService.redisTemplate.opsForZSet().removeRange(string, 0, -1);
			}
		} catch (Exception e) {
			Tools.getLogger().error("过期缓存失败", e);
		}
	}
	
	
	/**
	 * 删除
	 * @param id
	 * @param parentId
	 * @author shuyy
	 * @date 2017年11月25日
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(Long id, Long parentId){
		//查询是否有子数据字典，有则不能删除
		Integer num = dictionaryMapper.countSubNum(id);
		if(num > 0){
			return false;
		}
		dictionaryMapper.delete(id);
		//查询父级数据字典下的数据字典数量，如果为0，则把父级数据字典的isParent字段更新为false
		if(parentId != 0){
			num = dictionaryMapper.countSubNum(parentId);
			if(num == 0){
				Dictionary dictionary = new Dictionary();
				dictionary.setId(parentId);
				dictionary.setIsParent(false);
				dictionaryMapper.update(dictionary);
			}
		}
		//过期缓存
		//过期三种缓存
//		1、String zsetKey = "{table:dictionary,parentId:" + parentId + ",keywords:" + keywords + "}";
//	  	2、String listKey = "dictionary:parentId:" + parentId;
//		3、String IDKey= "dictionary:id";
		String IDKey= "dictionary:id";
		String listKey = "dictionary:parentId:" + parentId;
		String parentIdKey = "{table:dictionary,parentId:" + parentId + "}";
		try {
			redisService.redisTemplate.opsForHash().delete(IDKey, id.toString());
			redisService.redisTemplate.opsForList().trim(listKey, 1, 0);
			List<String> range = redisService.redisTemplate.opsForList().range(parentIdKey, 0, -1);
			for (String string : range) {
				redisService.redisTemplate.opsForZSet().removeRange(string, 0, -1);
			}
		} catch (Exception e) {
			Tools.getLogger().error("过期缓存失败", e);
		}
		return true;
	}
	
	/**
	 * 查询数据字典，通过parentCode，用于填充下拉框
	 * @param parentCode
	 * @return 格式：[{code:code, name:name}]
	 * @author shuyy
	 * @date 2017年11月26日
	 */
	@TargetDataSource("ds1")
	@Override
	public List<Dictionary> listDictionaryByParentCode(String parentCode){
		String key = "{table:dictionary,parentCode:" + parentCode + "}";
		List<String> idList = redisService.redisTemplate.opsForList().range(key, 0, -1);
		List<Dictionary> dictionarys = new ArrayList<>();
		try {
			if(idList.isEmpty()){
				//从数据库中取
				dictionarys = dictionaryMapper.listDictionaryByParentCode(parentCode);
				List<String> listId = new ArrayList<>();
				for (Dictionary dictionary : dictionarys) {
					Long id = dictionary.getId();
					listId.add(id.toString());
				}
				//存入缓存
				redisService.redisTemplate.opsForList().rightPushAll(key, listId);
			}else{
				//缓存中有
				String idKey = "dictionary:id";
				for (String string : idList) {
					Object object = redisService.redisTemplate.opsForHash().get(idKey, string);
					Dictionary dictionary = null;
					if(object == null){
						dictionary = dictionaryMapper.getDictionaryById(Long.parseLong(string));
						redisService.redisTemplate.opsForHash().put(idKey, string, JSON.toJSON(dictionary));
					}else{
						dictionary = JSON.parseObject(object.toString(), Dictionary.class);
					}
					dictionarys.add(dictionary);
				}
			}
		} catch (Exception e) {
			Tools.getLogger().error("错误", e);
			//从数据库中取
			dictionarys = dictionaryMapper.listDictionaryByParentCode(parentCode);
		}
		return dictionarys;
	}

}
