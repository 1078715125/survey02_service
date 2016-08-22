package com.atguigu.survey.component.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.UserDao;
import com.atguigu.survey.entity.guest.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	public boolean checkUser(String userName) {

		String hql = "select count(*) from User u where u.userName = ?";
		Integer rec = getTotalRecordNoByHql(hql, userName);

		return rec == 0;
	}

	public User getUserByNamePwd(User user) {
		String hql = "from User u where u.userName = ? and u.userPwd = ?";
		
		return getEntityByHql(hql, user.getUserName(),user.getUserPwd());
	}

	public List<User> getAllAdminList() {
		String hql = "from User u order by u.userId";
		return getListByHql(hql);
	}

}
