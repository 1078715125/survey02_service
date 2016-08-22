package com.atguigu.survey.aspect;

import javax.servlet.http.HttpServletRequest;

/**
 * 将request对象绑定到当前线程上
 * 
 * 
 * @author GYX09
 * @data 2016年7月5日下午4:47:57
 */
public class RequestBinder {

	private static ThreadLocal<HttpServletRequest> local;
	
	static{
		local = new ThreadLocal<HttpServletRequest>();
	}
	
	public static void bindRequest(HttpServletRequest request){
		local.set(request);
	}
	
	public static HttpServletRequest getRequest(){
		return local.get();
	}
	
	public static void removeRequest(){
		local.remove();
	}
}
