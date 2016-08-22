package com.atguigu.survey.component.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.AdminDao;
import com.atguigu.survey.entity.manager.Admin;

@Repository
public class AdminDaoImpl extends BaseDaoImpl<Admin> implements AdminDao {

	public List<Admin> getAllAdminList() {
		String hql = "from Admin a order by a.adminId";
		return getListByHql(hql);
	}

	public void batchDelete(List<Integer> adminIdList) {
		String sql = "DELETE FROM `survey_admin` WHERE `ADMIN_ID` = ?";
		Object[][] params = new Object[adminIdList.size()][1];

		for (int i = 0; i < params.length; i++) {
			params[i][0] = adminIdList.get(i);
		}
		batchUpdate(sql, params);
	}

	public boolean checkAdminName(String adminName) {
		String sql = "SELECT COUNT(*) FROM `survey_admin` WHERE `ADMIN_NAME` = ?";
		return getTotalRecordNoBySql(sql, adminName) > 0;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getCurrentRoleIdList(Integer adminId) {
		String sql = "SELECT `ROLE_ID` FROM `survey_inner_admin_role` WHERE `ADMIN_ID` = ?";
		return getListBySql(sql, adminId);
	}

	public void deleteOldRelationship(Integer adminId) {
		String sql = "DELETE FROM `survey_inner_admin_role` WHERE `ADMIN_ID` = ?";

		updateBySql(sql, adminId);
	}

	public void updateRelationship(Integer adminId, List<Integer> roleIdList) {
		String sql = "INSERT INTO `survey_inner_admin_role`(`ADMIN_ID`,`ROLE_ID`) VALUES(?,?)";
		Object[][] params = new Object[roleIdList.size()][2];

		for (int i = 0; i < params.length; i++) {
			Object[] param = params[i];

			param[0] = adminId;
			param[1] = roleIdList.get(i);
		}
		
		batchUpdate(sql, params);
	}

	public Admin getAdmin(Admin admin) {
		String hql = "from Admin a where a.adminName = ? and a.adminPwd = ?";
		return getEntityByHql(hql, admin.getAdminName(),admin.getAdminPwd());
	}

}
