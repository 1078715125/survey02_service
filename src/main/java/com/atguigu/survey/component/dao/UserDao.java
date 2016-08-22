package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.guest.User;

public interface UserDao extends BaseDao<User> {

	/**
	 * 判断当前用户名是否可用
	 * 
	 * @param userName
	 * @return true 可用；false 不可用
	 */
	boolean checkUser(String userName);

	/**
	 * 通过user对象中的name和pwd获取user对象
	 * 
	 * @param user
	 * @return
	 */
	User getUserByNamePwd(User user);

	List<User> getAllAdminList();

}
