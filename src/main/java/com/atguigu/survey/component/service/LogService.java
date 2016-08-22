package com.atguigu.survey.component.service;

import java.util.List;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.manager.Log;
import com.atguigu.survey.model.Page;

public interface LogService extends BaseService<Log>{

	void createLogTable(String tableName);

	void saveLog(Log log);

	List<String> getLogTableNameList();


	Page<Log> getLogListLimitedfromTableName(String currentLog, String pageNoStr);

}
