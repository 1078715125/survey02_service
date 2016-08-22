package com.atguigu.survey.component.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.LogDao;
import com.atguigu.survey.entity.manager.Log;
import com.atguigu.survey.utils.DataProcessUtils;

@Repository
public class LogDaoImpl extends BaseDaoImpl<Log> implements LogDao {

	public void createLogTable(String tableName) {
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName
				+ " LIKE `survey160315`.`survey_log`";
		updateBySql(sql);
	}

	public void saveLog(Log log) {
		String tableName = DataProcessUtils.generateTableName(0);
		String sql = "INSERT INTO `" + tableName
				+ "`(`OPERATOR`,`OPERATE_TIME`,`METHOD_NAME`,"
				+ "`TYPE_NAME`,`PARAMS`,`RETURN_VALUE`,`EXCEPTION_TYPE`"
				+ ",`EXCEPTION_MSG`) VALUES(?,?,?,?,?,?,?,?)";
		updateBySql(sql, log.getOperator(), log.getOperateTime(),
				log.getMethodName(), log.getTypeName(), log.getParams(),
				log.getReturnValue(), log.getExceptionType(),
				log.getExceptionMsg());
	}

	@SuppressWarnings("unchecked")
	public List<String> getLogTableNameList() {
		String sql = "SELECT `TABLE_NAME` FROM `information_schema`.`TABLES` WHERE `TABLE_SCHEMA` = 'survey160315_log'";
		return getListBySql(sql);
	}

	private List<Log> convertToLogList(List<Object[]> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		List<Log> logList = new ArrayList<Log>();
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = list.get(i);
			if (objs.length < 8) {
				continue;
			}
			Log log = new Log();
			log.setLogId((Integer) objs[0]);
			log.setOperator((String) objs[1]);
			log.setOperateTime((String) objs[2]);
			log.setMethodName((String) objs[3]);
			log.setTypeName((String) objs[4]);
			log.setParams((String) objs[5]);
			log.setReturnValue((String) objs[6]);
			log.setExceptionType((String) objs[7]);
			log.setExceptionMsg((String) objs[8]);

			logList.add(log);
		}

		return logList;
	}

	public int getTotalRecordNofromTableName(String currentLog) {
		String sql = "SELECT COUNT(*) FROM `" + currentLog + "`";
		return getTotalRecordNoBySql(sql);
	}

	@SuppressWarnings("unchecked")
	public List<Log> getLogListLimitfromTableName(String currentLog,
			int pageNo, int pageSize) {
		String sql = "SELECT `LOG_ID` logId,`OPERATOR`,"
				+ "`OPERATE_TIME` operateTime,`METHOD_NAME` methodName,"
				+ "`TYPE_NAME` typeName,`PARAMS`,`RETURN_VALUE` returnValue,"
				+ "`EXCEPTION_TYPE` exceptionType,"
				+ "`EXCEPTION_MSG` exceptionMsg FROM `" + currentLog + "`";
		List<Object[]> list = getLimitedListBySql(sql, pageNo, pageSize);

		return convertToLogList(list);
	}

}
