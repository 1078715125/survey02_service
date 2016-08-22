package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.manager.Log;

public interface LogDao extends BaseDao<Log>{

	void createLogTable(String tableName);

	void saveLog(Log log);

	List<String> getLogTableNameList();


	int getTotalRecordNofromTableName(String currentLog);

	List<Log> getLogListLimitfromTableName(String currentLog, int pageNo,
			int pageSize);

}
