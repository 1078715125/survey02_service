package com.atguigu.survey.component.handler.guest;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.survey.component.service.UserService;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.utils.GlobalNames;

@Controller
public class UserHandler {

	@Autowired
	private UserService userService;

	@RequestMapping("guest/user/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/index.jsp";
	}

	@RequestMapping("/guest/user/regist")
	public String regist(User user) {

		userService.regist(user);

		return "guest/user_loginUI";
	}

	@RequestMapping("/guest/user/login")
	public String login(User user, HttpSession session) {

		User loginUser = userService.login(user);

		session.setAttribute(GlobalNames.LOGIN_USER, loginUser);
		return "redirect:/index.jsp";
	}
}
