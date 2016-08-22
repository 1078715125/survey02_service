package com.atguigu.survey.component.service.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.AdminDao;
import com.atguigu.survey.component.dao.ResDao;
import com.atguigu.survey.component.service.AdminService;
import com.atguigu.survey.entity.manager.Admin;
import com.atguigu.survey.entity.manager.Role;
import com.atguigu.survey.ex.AdminDeleteException;
import com.atguigu.survey.ex.AdminNameAlreadyExistsException;
import com.atguigu.survey.ex.AdminNameAndPwdNotMatchException;
import com.atguigu.survey.utils.DataProcessUtils;

@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements
		AdminService {

	@Autowired
	private AdminDao adminDao;
	@Autowired
	private ResDao resDao;

	public List<Admin> getAllAdminList() {

		return adminDao.getAllAdminList();
	}

	public void batchDelete(List<Integer> adminIdList) {

		try {
			adminDao.batchDelete(adminIdList);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ConstraintViolationException) {
				throw new AdminDeleteException();
			}
		}
	}

	public void saveAdmin(Admin admin) {
		// 检查账号是否存在
		boolean exists = adminDao.checkAdminName(admin.getAdminName());
		if (exists) {
			throw new AdminNameAlreadyExistsException();
		}

		String adminPwd = admin.getAdminPwd();
		adminPwd = DataProcessUtils.md5(adminPwd);
		admin.setAdminPwd(adminPwd);

		adminDao.saveEntity(admin);
	}

	public List<Integer> getCurrentRoleIdList(Integer adminId) {

		return adminDao.getCurrentRoleIdList(adminId);
	}

	public void updateRelationship(Integer adminId, List<Integer> roleIdList) {
		// 删除旧的关联
		adminDao.deleteOldRelationship(adminId);

		// 更新新的关联
		if (roleIdList != null && roleIdList.size() != 0) {
			adminDao.updateRelationship(adminId, roleIdList);
		}

		// 特有的操作：计算权限码数组
		// 查询Admin对象(并成为持久化对象)
		Admin admin = adminDao.getEntity(adminId);

		// 计算权限码数组
		// 查询当前系统中最大的权限位的值
		Integer maxPos = resDao.getMaxPos();
		Set<Role> roleSet = admin.getRoleSet();

		int[] codeArr = DataProcessUtils.calculateResCode(roleSet, maxPos);

		// 转换成字符串
		String resCode = DataProcessUtils.convertArrToString(codeArr);

		// 设置到Admin对象中
		admin.setResCode(resCode);
	}

	public Admin login(Admin admin) {

		String adminPwd = admin.getAdminPwd();
		adminPwd = DataProcessUtils.md5(adminPwd);
		admin.setAdminPwd(adminPwd);

		Admin loginAdmin = adminDao.getAdmin(admin);
		if (loginAdmin == null) {
			throw new AdminNameAndPwdNotMatchException();
		}
		return loginAdmin;
	}
}
