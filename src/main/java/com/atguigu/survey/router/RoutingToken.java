package com.atguigu.survey.router;

/**
 * 通过线程本地化实现对路由器数据源的路由通知的工具类
 *
 *
 * @author GYX09
 * @data 2016年7月6日上午10:13:18
 */
public class RoutingToken {

	public static final String DATASOURCE_DEFAULT = "DATASOURCE_MAIN";
	public static final String DATASOURCE_MAIN = "DATASOURCE_MAIN";
	public static final String DATASOURCE_LOG = "DATASOURCE_LOG";
	
	private static ThreadLocal<String> local ;
	static {
		local = new ThreadLocal<String>();
	}
	
	public static void setToken(String token){
		local.set(token);
	}
	
	public static String getToken(){
		return local.get();
	}
	
	public static void removeToken(){
		local.remove();
	}
}
