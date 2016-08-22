package com.atguigu.survey.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import com.atguigu.survey.component.service.ResService;
import com.atguigu.survey.entity.manager.Res;
import com.atguigu.survey.utils.DataProcessUtils;

public class GenerateResInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ResService resService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// a.处理静态资源
		if (handler instanceof DefaultServletHttpRequestHandler) {
			return true;
		}

		// b.处理请求
		// 获取当前请求的servletPath
		String servletPath = request.getServletPath();

		// 去除尾部的REST风格的PathVariable
		servletPath = DataProcessUtils.cutTailForServletPath(servletPath);

		// 检查数据库中是否存在该servletPath
		boolean isExists = resService.checkServletPath(servletPath);
		if (isExists) {
			return true;
		}

		// 将servletPath封装到Res对象中保存到数据库
		// 关键：生成权限码和权限位
		Integer resPosFinal = null;
		Integer resCodeFinal = null;

		// 声明一个变量用于保存当前系统允许的最大的权限码的值，便于后面比较
		int systemMaxCode = 1 << 30;

		// 查询当前系统中实际的最大权限位
		Integer maxPos = resService.getMaxPos();
		// 查询当前最大权限位范围内的最大权限码
		Integer maxCode = (maxPos == null) ? null : resService
				.getMaxCode(maxPos);

		// 分具体情况给最终变量赋值
		if (maxPos == null && maxCode == null) {
			// 系统中尚未保存过任何资源
			resPosFinal = 0;
			resCodeFinal = 1;
		} else if (maxCode < systemMaxCode) {
			// 系统有保存过的资源，且权限码没有达到极限
			resPosFinal = maxPos;
			resCodeFinal = maxCode << 1;
		} else {
			// 系统有保存过的资源，且权限码已经达到极限
			resPosFinal = maxPos + 1;
			resCodeFinal = 1;
		}
		//封装Res对象
		Res res = new Res(null, servletPath, resPosFinal, resCodeFinal, false);
		resService.saveEntity(res);
		
		return true;
	}

}
