package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.manager.Role;

public interface RoleDao extends BaseDao<Role>{

	List<Role> getAllRoleList();

	void batchDelete(List<Integer> roleIdList);

	void updateRoleName(Role role);

	List<Integer> getCurrentAuthIdList(Integer roleId);

	void deleteOldRelationship(Integer roleId);

	void updateRelationship(Integer roleId, List<Integer> authIdList);

	Role getRoleByName(String roleName);

}
