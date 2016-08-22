package com.atguigu.survey.component.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.LogDao;
import com.atguigu.survey.component.service.LogService;
import com.atguigu.survey.entity.manager.Log;
import com.atguigu.survey.model.Page;

@Service
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService {

	@Autowired
	private LogDao logDao;

	public void createLogTable(String tableName) {
		logDao.createLogTable(tableName);

	}

	public void saveLog(Log log) {
		logDao.saveLog(log);
	}

	public List<String> getLogTableNameList() {

		return logDao.getLogTableNameList();
	}

	public Page<Log> getLogListLimitedfromTableName(String currentLog,
			String pageNoStr) {
		int totalRecordNo = logDao.getTotalRecordNofromTableName(currentLog);
		Page<Log> page = new Page<Log>(pageNoStr, totalRecordNo,
				Page.PAGE_SIZE_MINI);
		int pageNo = page.getPageNo();
		List<Log> list = logDao.getLogListLimitfromTableName(currentLog,
				pageNo, Page.PAGE_SIZE_MINI);// 待分页

		page.setList(list);
		return page;
	}
}
