package com.atguigu.survey.component.service;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.guest.User;

public interface UserService extends BaseService<User> {

	/**
	 * 注册用户
	 * 
	 * @param user
	 */
	void regist(User user);

	/**
	 * 
	 * 用户登录验证
	 * 
	 * @param user
	 */

	User login(User user);

}
