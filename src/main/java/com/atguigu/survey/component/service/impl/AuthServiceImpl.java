package com.atguigu.survey.component.service.impl;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.AdminDao;
import com.atguigu.survey.component.dao.AuthDao;
import com.atguigu.survey.component.dao.ResDao;
import com.atguigu.survey.component.dao.UserDao;
import com.atguigu.survey.component.service.AuthService;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.entity.manager.Admin;
import com.atguigu.survey.entity.manager.Authorize;
import com.atguigu.survey.ex.AuthDeleteException;
import com.atguigu.survey.utils.DataProcessUtils;

@Service
public class AuthServiceImpl extends BaseServiceImpl<Authorize> implements
		AuthService {

	@Autowired
	private AuthDao authDao;
	@Autowired
	private ResDao resDao;

	@Autowired
	private AdminDao adminDao;
	@Autowired
	private UserDao userDao;

	public List<Authorize> getAllAuthList() {

		return authDao.getAllAuthList();
	}

	public void updateAuthName(Authorize auth) {
		authDao.updateAuthName(auth);

	}

	public List<Integer> getCurrentResIdList(Integer authId) {

		return authDao.getCurrentResIdList(authId);
	}

	public void batchDelete(List<Integer> authIdList) {
		try {
			authDao.batchDelete(authIdList);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ConstraintViolationException) {
				throw new AuthDeleteException();
			}
		}
	}

	public void updateRelationship(Integer authId, List<Integer> resIdList) {
		// 先删除旧的关联
		authDao.deleteOldRelationship(authId);

		// 若新关联的资源集合不为空，更新新关联
		if (resIdList != null && resIdList.size() != 0) {
			authDao.updateRelationship(authId, resIdList);
		}

		// ※补充：重新计算所有用户的权限码
		// 查询最大资源位
		Integer maxPos = resDao.getMaxPos();
		// 查询所有admin
		List<Admin> adminList = adminDao.getAllAdminList();
		// 查询所有user
		List<User> userList = userDao.getAllAdminList();
		
		//重新计算并绑定
		DataProcessUtils.reBindingResCode(adminList, userList, maxPos);
	}
}
