package com.wuliaozhiyuan.mapper.system;

import java.util.List;

import com.wuliaozhiyuan.bean.system.Dictionary;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
/**
 * 字典Mapper
 * @author wuliaozhiyuan
 *
 */
public interface DictionaryMapper {
	/**
	 * 用于构造字典树，通过父dictionaryId，查所有子字典
	 * @author wuliaozhiyuan
	 * @param pd
	 * @return
	 */
	public List<Dictionary> listDictionaryByParentId(PageData pd); 
	/**
	 * 查询字典列表，通过查询参数  
	 * @param page 分页参数：含有的查询参数有：{
	 * pd:{parentId:parentId(ps:required), keywords:keywords(ps:optional.keywords可以是name，englistname， code)}
	 * }
	 * @author wuliaozhiyuan
	 * @return
	 */
	public List<Dictionary> dictionaryListPage(Page page);
	/**
	 * 获取字典对象，通过字典id
	 * @param id
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public Dictionary getDictionaryById(Long id);
	/**
	 * 查询code数量，通过code
	 * @param pageData 格式为：{code:code, parentId:parentId}
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public Integer countCodeNum(PageData pageData);
	/**
	 * 保存
	 * @param dictionary
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public void save(Dictionary dictionary);
	/**
	 * 保存
	 * @param dictionary
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public void update(Dictionary dictionary);
	/**
	 * 查询子数据字典数量，通过parentId
	 * @param parentId
	 * @return
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public Integer countSubNum(Long parentId);
	/**
	 * 删除
	 * @param id
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	public void delete(Long id);
	/**
	 * 查询数据字典，通过parentCode，用于填充下拉框
	 * @param parentCode
	 * @return 格式：[{code:code, name:name}]
	 * @author shuyy
	 * @date 2017年11月26日
	 */
	public List<Dictionary> listDictionaryByParentCode(String parentCode);
}
