package com.atguigu.survey.component.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.ResDao;
import com.atguigu.survey.component.dao.RoleDao;
import com.atguigu.survey.component.dao.UserDao;
import com.atguigu.survey.component.service.UserService;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.entity.manager.Role;
import com.atguigu.survey.ex.UserNameAlreadyExistsException;
import com.atguigu.survey.ex.UserNameAndPwdNotMatchException;
import com.atguigu.survey.utils.DataProcessUtils;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements
		UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ResDao resDao;

	public void regist(User user) {

		boolean available = userDao.checkUser(user.getUserName());

		if (!available) {
			// 如果用户名不可用，则抛出异常：UserNameAlreadyExistsException
			throw new UserNameAlreadyExistsException();
		}

		// ※补充：对密码进行加密
		String md5 = DataProcessUtils.md5(user.getUserPwd());
		user.setUserPwd(md5);

		// ※补充：建立User到Role的关联关系
		// 创建一个集合用来保存角色对象
		Set<Role> roleSet = new HashSet<Role>();
		Role role = null;
		// 检查用户类型
		boolean company = user.isCompany();
		// 根据用户类型查询对应的角色
		if (company) {
			role = roleDao.getRoleByName("企业用户");
		} else {
			role = roleDao.getRoleByName("个人用户");
		}
		// 注意：要设置到user对象中才能够保存关联关系
		roleSet.add(role);
		user.setRoleSet(roleSet);
		// 计算用户的权限码
		Integer maxPos = resDao.getMaxPos();
		int[] resCodeArr = DataProcessUtils.calculateResCode(roleSet, maxPos);
		String resCode = DataProcessUtils.convertArrToString(resCodeArr);
		user.setResCode(resCode);
		
		// 将user对象保存到数据库中
		userDao.saveEntity(user);

	}

	public User login(User user) {
		// 对密码进行加密
		String md5 = DataProcessUtils.md5(user.getUserPwd());
		user.setUserPwd(md5);

		User loginUser = userDao.getUserByNamePwd(user);

		if (loginUser == null) {
			throw new UserNameAndPwdNotMatchException();
		}

		return loginUser;
	}

}
