package com.atguigu.survey.tag;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.atguigu.survey.component.service.ResService;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.entity.manager.Admin;
import com.atguigu.survey.entity.manager.Res;
import com.atguigu.survey.utils.DataProcessUtils;
import com.atguigu.survey.utils.GlobalNames;

/**
 * 细粒度的权限控制，检查用户是否有权限显示标签体
 * 
 * @author GYX09
 * @data 2016年7月5日上午10:00:22
 */
public class AuthCheckTag extends SimpleTagSupport {

	private String servletPath;

	@Override
	public void doTag() throws JspException, IOException {
		
		PageContext page = (PageContext) getJspContext();
		JspFragment body = getJspBody();
		
		HttpSession session = page.getSession();
		ServletContext servletContext = session.getServletContext();

		// 手动获取当前环境下的IOC容器对象，因为当前自定义标签的类不是IOC容器创建的，需要的组件不能自动注入
		WebApplicationContext ioc = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);

		ResService resService = ioc.getBean(ResService.class);
		//根据servletPath查询对应的Res对象
		Res res = resService.getResByName(servletPath);
		if (res == null) {
			return;
		}
		//检查是否是公共资源
		if (res.isPublicRes()) {
			//执行标签体
			body.invoke(null);
			return;
		}
		
		//检查登录情况(/guest or /manager)
		if (servletPath.startsWith("/guest")) {
			//已登录则检查权限
			User user = (User) session.getAttribute(GlobalNames.LOGIN_USER);
			if (user != null) {
				String resCodeStr = user.getResCode();
				boolean hasRight = DataProcessUtils.checkAuthority(res, resCodeStr);
				
				if (hasRight) {
					body.invoke(null);
					return;
				}
			}
		}
		
		if (servletPath.startsWith("/manager")) {
			//已登录则检查权限
			Admin admin = (Admin) session.getAttribute(GlobalNames.LOGIN_ADMIN);
			if (admin != null) {
				if ("superAdmin".equals(admin.getAdminName())) {
					body.invoke(null);
					return;
				}
				
				String resCodeStr = admin.getResCode();
				boolean hasRight = DataProcessUtils.checkAuthority(res, resCodeStr);
				
				if (hasRight) {
					body.invoke(null);
					return;
				}
			}
		}
		

	}

	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

}
