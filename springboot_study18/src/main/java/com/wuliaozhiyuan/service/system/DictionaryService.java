package com.wuliaozhiyuan.service.system;

import java.util.List;

import com.wuliaozhiyuan.bean.system.Dictionary;
import com.wuliaozhiyuan.bean.system.vo.DictionaryVo;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;

/**
 * 字典service
 * @author wuliaozhiyuan
 *
 */
public interface DictionaryService {
	/**
	 * 获取子字典，通过父字典id。返回格式为一个ztree需要的格式
	 * @author wuliaozhiyuan
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listDictionaryByParentId(PageData pd) throws Exception;
	/**
	 * 查询字典列表，通过查询参数  
	 * @param page 分页参数：含有的查询参数有：{
	 * pd:{parentId:parentId(ps:optional), keywords:keywords(ps:optional.keywords可以是name，englistname， code)}
	 * }
	 * @return
	 * @throws Exception
	 */
	public List<Dictionary> listDictionary(Page page) throws Exception;
	
	/**
	 * 获取字典对象，通过字典id
	 * @param id
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public Dictionary getDictionaryById(Long id);
	
	/**
	 * 查询code数量，通过code和parentId
	 * @param pageData 格式为：{code:code, parentId:parentId}
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public Integer countCodeNum(PageData pageData);
	/**
	 * 保存
	 * @param dictionaryVo
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public void save(DictionaryVo dictionaryVo);
	/**
	 * 保存修改
	 * @param dictionary
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public void update(Dictionary dictionary);
	/**
	 * 删除
	 * @param id
	 * @param parentId
	 * @author shuyy
	 * @date 2017年11月25日
	 * @return 是否删除成功
	 */
	public boolean delete(Long id, Long parentId);
	/**
	 * 查询数据字典，通过parentCode，用于填充下拉框
	 * @param parentCode
	 * @return 格式：[{code:code, name:name}]
	 * @author shuyy
	 * @date 2017年11月26日
	 */
	public List<Dictionary> listDictionaryByParentCode(String parentCode);
}
