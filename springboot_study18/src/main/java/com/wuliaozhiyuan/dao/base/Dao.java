package com.wuliaozhiyuan.dao.base;

import java.util.List;

import com.wuliaozhiyuan.util.PageData;
/**
 * 批量处理
 * @author wuliaozhiyuan
 *
 */
public interface Dao {

	/**
	 * 批量处理
	 * @param mapper 插入调用的mapper接口
	 * @param pageDataList 参数list
	 * @param flag true：插入，false：更新
	 * @throws Exception
	 */
	public void insertBatch(String mapper, List<PageData> pageDataList, boolean flag) throws Exception;
	
}
