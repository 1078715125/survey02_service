package com.atguigu.survey.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class SurveyKeyGenerator implements KeyGenerator {

	/**
	 * @param target
	 *            ：调用目标方法的对象
	 * @param method
	 *            ：目标方法
	 * @param params
	 *            ：调用目标方法时传入的参数
	 */
	public Object generate(Object target, Method method, Object... params) {

		StringBuilder sb = new StringBuilder();

		sb.append(target.getClass().getName()).append(".");
		sb.append(method.getName()).append(".");
		if (params != null) {
			for (Object obj : params) {
				if (obj == null) {
					continue;
				}
				sb.append(obj.toString()).append(".");
			}
		}
		
		String key = sb.substring(0, sb.lastIndexOf("."));
		System.out.println(key);
		return key;
	}

}
