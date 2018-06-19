package com.wuliaozhiyuan.service.impl.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wuliaozhiyuan.mapper.system.LogMapper;
import com.wuliaozhiyuan.service.system.LogService;
import com.wuliaozhiyuan.util.PageData;
/**
 * LogService
 * @author wuliaozhiyuan
 *
 */

@Service
public class LogServiceImpl implements LogService {
	
	@Autowired
	private LogMapper logMapper;
	
	@Override
	public void save(PageData pd) {
		logMapper.save(pd);

	}

}
