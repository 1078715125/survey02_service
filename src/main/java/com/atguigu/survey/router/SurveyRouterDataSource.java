package com.atguigu.survey.router;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 路由器数据源类，实现分布式数据库
 * 
 * 
 * @author GYX09
 * @data 2016年7月6日上午8:41:52
 */
public class SurveyRouterDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {

		String token = RoutingToken.getToken();
		//用完一定要移除token
		RoutingToken.removeToken();
		return token;
	}

}
