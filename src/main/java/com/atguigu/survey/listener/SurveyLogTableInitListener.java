package com.atguigu.survey.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.atguigu.survey.component.service.LogService;
import com.atguigu.survey.router.RoutingToken;
import com.atguigu.survey.utils.DataProcessUtils;

/**
 * 在服务器启动时生成当月与下月的Log表
 * 
 * 
 * @author GYX09
 * @data 2016年7月5日下午8:47:29
 */
public class SurveyLogTableInitListener implements
		ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private LogService logService;

	public void onApplicationEvent(ContextRefreshedEvent event) {

		// 创建当月的数据库表
		String tableName = DataProcessUtils.generateTableName(0);
		RoutingToken.setToken(RoutingToken.DATASOURCE_LOG);// 分布式保存日志
		logService.createLogTable(tableName);
		// 创建下一个月的数据库表
		tableName = DataProcessUtils.generateTableName(1);
		RoutingToken.setToken(RoutingToken.DATASOURCE_LOG);// 分布式保存日志
		logService.createLogTable(tableName);

	}

}
