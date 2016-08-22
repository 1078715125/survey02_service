package com.atguigu.survey.component.service;

import java.util.List;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.manager.Role;

public interface RoleService extends BaseService<Role>{

	List<Role> getAllRoleList();

	void batchDelete(List<Integer> roleIdList);

	void updateRoleName(Role role);

	List<Integer> getCurrentAuthIdList(Integer roleId);

	void updateRelationship(Integer roleId, List<Integer> authIdList);

}
