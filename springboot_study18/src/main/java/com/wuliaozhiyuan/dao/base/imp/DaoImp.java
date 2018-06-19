package com.wuliaozhiyuan.dao.base.imp;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.wuliaozhiyuan.dao.base.Dao;
import com.wuliaozhiyuan.util.PageData;

/**
 * 通用的dao操作类，用来处理批量操作
 * 
 * @author wuliaozhiyuan
 *
 */
@Repository("dao")
public class DaoImp implements Dao {

	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	/**
	 * 批量处理
	 * @param mapper 插入调用的mapper接口
	 * @param pageDataList 参数list
	 * @param flag true：插入，false：更新
	 * @throws Exception
	 */
	@Override
	public void insertBatch(String mapper, List<PageData> pageDataList, boolean flag) throws Exception {
		// 新获取一个模式为BATCH，自动提交为false的session
		// 如果自动提交设置为true,将无法控制提交的条数，改为最后统一提交，可能导致内存溢出
		SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		try {
			if (null != pageDataList || pageDataList.size() > 0) {
				int lsize = pageDataList.size();
				for (int i = 0, n = pageDataList.size(); i < n; i++) {
					PageData pageData = pageDataList.get(i);
					// session.insert("com.xx.mapper.UserMapper.insert",user);
					// session.update("com.xx.mapper.UserMapper.updateByPrimaryKeySelective",_entity);
					if(flag){
						session.insert(mapper, pageData);
					}else{
						session.update(mapper, pageData);
					}
					boolean iscommit = (i > 0 && i % 1000 == 0) || i == lsize - 1;
					if (iscommit) {
						// 手动每1000个一提交，提交后无法回滚
						session.commit();
						// 清理缓存，防止溢出
						session.clearCache();
					}
				}
			}
		} catch (Exception e) {
			// 没有提交的数据可以回滚
			session.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	
}
