package com.atguigu.survey.component.service.impl;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.AdminDao;
import com.atguigu.survey.component.dao.ResDao;
import com.atguigu.survey.component.dao.RoleDao;
import com.atguigu.survey.component.dao.UserDao;
import com.atguigu.survey.component.service.RoleService;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.entity.manager.Admin;
import com.atguigu.survey.entity.manager.Role;
import com.atguigu.survey.ex.RoleDeleteException;
import com.atguigu.survey.utils.DataProcessUtils;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements
		RoleService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ResDao resDao;

	@Autowired
	private AdminDao adminDao;
	@Autowired
	private UserDao userDao;

	public List<Role> getAllRoleList() {

		return roleDao.getAllRoleList();
	}

	public void batchDelete(List<Integer> roleIdList) {
		try {
			roleDao.batchDelete(roleIdList);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ConstraintViolationException) {
				throw new RoleDeleteException();
			}
		}
	}

	public void updateRoleName(Role role) {
		roleDao.updateRoleName(role);

	}

	public List<Integer> getCurrentAuthIdList(Integer roleId) {
		return roleDao.getCurrentAuthIdList(roleId);
	}

	public void updateRelationship(Integer roleId, List<Integer> authIdList) {
		// 先删除旧的数据
		roleDao.deleteOldRelationship(roleId);

		// 若新关联的资源集合不为空，更新新的关联
		if (authIdList != null && authIdList.size() != 0) {
			roleDao.updateRelationship(roleId, authIdList);
		}

		// ※补充：重新计算所有用户的权限码
		// 查询最大资源位
		Integer maxPos = resDao.getMaxPos();
		// 查询所有admin
		List<Admin> adminList = adminDao.getAllAdminList();
		// 查询所有user
		List<User> userList = userDao.getAllAdminList();

		// 重新计算并绑定
		DataProcessUtils.reBindingResCode(adminList, userList, maxPos);

	}

}
