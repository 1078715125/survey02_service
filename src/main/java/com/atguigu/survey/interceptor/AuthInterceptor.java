package com.atguigu.survey.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import com.atguigu.survey.component.service.ResService;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.entity.manager.Admin;
import com.atguigu.survey.entity.manager.Res;
import com.atguigu.survey.ex.AdminNeedToLoginException;
import com.atguigu.survey.ex.NoRightException;
import com.atguigu.survey.ex.UserNeedToLoginException;
import com.atguigu.survey.utils.DataProcessUtils;
import com.atguigu.survey.utils.GlobalNames;

/**
 * 权限拦截器
 * 
 * @author GYX09
 * @data 2016年7月4日下午9:03:56
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ResService resService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// a.静态资源直接放行
		if (handler instanceof DefaultServletHttpRequestHandler) {
			return true;
		}

		// b.公共资源：项目中不需要登录就可以访问的资源
		// 获取当前请求的URL地址
		String targetUrl = request.getServletPath();

		// 将ServletPath中的多余部分剪掉
		targetUrl = DataProcessUtils.cutTailForServletPath(targetUrl);
		Res res = resService.getResByName(targetUrl);

		if (res.isPublicRes()) {
			return true;
		}

		// c.检查登录状态
		HttpSession session = request.getSession();
		// 以/guest开头代表是用户请求，判断是否有用户登录
		if (targetUrl.startsWith("/guest")) {
			User loginUser = (User) session
					.getAttribute(GlobalNames.LOGIN_USER);
			if (loginUser != null) {

				// 已登录则检查权限
				String resCodeStr = loginUser.getResCode();
				// 12.有权限则放行
				boolean hasRight = DataProcessUtils.checkAuthority(res, resCodeStr);
				
				if (hasRight) {
					return true;
				} else {
					// 13.没有权限则抛出异常
					throw new NoRightException();
				}
				
			} else {
				// 如果没有登录，则不能执行后续操作
				// 跳转回到登录页面，并显示提示消息
				// ※在拦截器中不能使用异常映射机制，所以只能手动进行跳转
				throw new UserNeedToLoginException();// ?为什么直接抛异常也可以？
				// return false;
			}
		}
		// 以/manager开头代表是管理请求，判断是否有管理员登录
		if (targetUrl.startsWith("/manager")) {
			Admin loginAdmin = (Admin) session
					.getAttribute(GlobalNames.LOGIN_ADMIN);
			if (loginAdmin != null) {
				// 如果已登录则检查是否是超级管理员
				if ("superAdmin".equals(loginAdmin.getAdminName())) {
					return true;
				}

				// 如果不是超级管理员，则检查是否具备访问目标资源的权限
				String resCodeStr = loginAdmin.getResCode();

				boolean hasRight = DataProcessUtils.checkAuthority(res,
						resCodeStr);
				if (hasRight) {
					return true;
				} else {
					throw new NoRightException();
				}
				
			} else {
				throw new AdminNeedToLoginException();
			}
		}
		return false;
	}
}
