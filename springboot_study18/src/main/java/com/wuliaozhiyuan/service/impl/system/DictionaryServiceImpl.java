package com.wuliaozhiyuan.service.impl.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliaozhiyuan.bean.system.Dictionary;
import com.wuliaozhiyuan.bean.system.vo.DictionaryVo;
import com.wuliaozhiyuan.mapper.system.DictionaryMapper;
import com.wuliaozhiyuan.service.system.DictionaryService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
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
	@Override
	public List<PageData> listDictionaryByParentId(PageData pd) throws Exception {
		String idParam = "id";
		String parentIdParam = "parentId";
		if(pd.getLong(idParam) == null){
			pd.put(parentIdParam, 0);
		}else{
			pd.put(parentIdParam, pd.getLong(idParam));
		}
		List<Dictionary> dictionarys = dictionaryMapper.listDictionaryByParentId(pd);
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
	@Override
	public List<Dictionary> listDictionary(Page page) throws Exception{
		String parentIdParam = "parentId";
		if(page.getPd().getLong(parentIdParam) == null){
			page.getPd().put(parentIdParam, 0);
		}
		return dictionaryMapper.dictionaryListPage(page);
	}

	/**
	 * 获取字典对象，通过字典id
	 * @param id
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@Override
	public Dictionary getDictionaryById(Long id){
		return dictionaryMapper.getDictionaryById(id);
	}
	
	/**
	 * 查询code数量，通过code和parentId
	 * @param pageData 格式为：{code:code, parentId:parentId}
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
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
	}
	/**
	 * 保存修改
	 * @param dictionary
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@Override
	public void update(Dictionary dictionary){
		dictionaryMapper.update(dictionary);
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
		return true;
	}
	
	/**
	 * 查询数据字典，通过parentCode，用于填充下拉框
	 * @param parentCode
	 * @return 格式：[{code:code, name:name}]
	 * @author shuyy
	 * @date 2017年11月26日
	 */
	@Override
	public List<Dictionary> listDictionaryByParentCode(String parentCode){
		return dictionaryMapper.listDictionaryByParentCode(parentCode);
	}

}
