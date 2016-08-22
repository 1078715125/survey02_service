package com.atguigu.survey.component.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.RoleDao;
import com.atguigu.survey.entity.manager.Role;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {

	public List<Role> getAllRoleList() {
		String hql = "from Role r order by r.roleId";
		return getListByHql(hql);
	}

	public void batchDelete(List<Integer> roleIdList) {
		String sql = "DELETE FROM `survey_role` WHERE `ROLE_ID` = ?";
		Object[][] params = new Object[roleIdList.size()][1];

		for (int i = 0; i < params.length; i++) {
			params[i][0] = roleIdList.get(i);
		}
		batchUpdate(sql, params);
	}

	public void updateRoleName(Role role) {
		String hql = "update Role r set r.roleName = ? where r.roleId = ?";
		updateByHql(hql, role.getRoleName(), role.getRoleId());

	}

	@SuppressWarnings("unchecked")
	public List<Integer> getCurrentAuthIdList(Integer roleId) {
		String sql = "SELECT `AUTH_ID` FROM `survey_inner_role_auth` WHERE `ROLE_ID` = ?";
		return getListBySql(sql, roleId);
	}

	public void deleteOldRelationship(Integer roleId) {
		String sql = "DELETE FROM `survey_inner_role_auth` WHERE `ROLE_ID` = ?";
		updateBySql(sql, roleId);
	}

	public void updateRelationship(Integer roleId, List<Integer> authIdList) {
		String sql = "INSERT INTO `survey_inner_role_auth`(`ROLE_ID`,`AUTH_ID`) VALUES(?,?)";
		Object[][] params = new Object[authIdList.size()][2];

		for (int i = 0; i < params.length; i++) {
			Object[] param = params[i];

			param[0] = roleId;
			param[1] = authIdList.get(i);
		}
		batchUpdate(sql, params);
	}

	public Role getRoleByName(String roleName) {
		String hql = "from Role r where r.roleName = ?";
		return getEntityByHql(hql, roleName);
	}

}
