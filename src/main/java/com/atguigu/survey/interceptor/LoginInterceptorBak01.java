package com.atguigu.survey.interceptor;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.ex.UserNeedToLoginException;
import com.atguigu.survey.utils.GlobalNames;
import com.atguigu.survey.utils.GlobalValues;

/**
 * 检测用户登录的拦截器
 * 
 * @author GYX09
 * 
 */
public class LoginInterceptorBak01 extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// a.静态资源直接放行
		if (handler instanceof DefaultServletHttpRequestHandler) {
			return true;
		}

		// b.公共资源：项目中不需要登录就可以访问的资源（用set方便检查集合中有无该元素）
		Set<String> res = GlobalValues.ALLOWD_RES;

		// 获取当前请求的URL地址
		String targetUrl = request.getServletPath();
		if (res.contains(targetUrl)) {
			return true;
		}

		// c.检查登录状态
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute(GlobalNames.LOGIN_USER);
		if (loginUser != null) {

			return true;
		} else {

			// 如果没有登录，则不能执行后续操作
			// 跳转回到登录页面，并显示提示消息
			// ※在拦截器中不能使用异常映射机制，所以只能手动进行跳转
			throw new UserNeedToLoginException();// ?为什么直接抛异常也可以？
			// return false;
		}
	}
}
