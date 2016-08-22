package com.atguigu.survey.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.atguigu.survey.component.service.LogService;
import com.atguigu.survey.router.RoutingToken;
import com.atguigu.survey.utils.DataProcessUtils;

/**
 * 通过石英调度实现在每月15日00：00：00创建下两个月的Log日志表
 * 
 * 
 * @author GYX09
 * @data 2016年7月5日下午9:06:05
 */
public class LogQuartzScheduler extends QuartzJobBean {

	private LogService logService;

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {

		String tableName = DataProcessUtils.generateTableName(1);
		RoutingToken.setToken(RoutingToken.DATASOURCE_LOG);// 分布式保存日志
		logService.createLogTable(tableName);

		tableName = DataProcessUtils.generateTableName(2);
		RoutingToken.setToken(RoutingToken.DATASOURCE_LOG);// 分布式保存日志
		logService.createLogTable(tableName);
		
		System.out.println("石英调度。。。");
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

}
