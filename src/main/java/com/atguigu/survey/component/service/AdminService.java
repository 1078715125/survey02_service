package com.atguigu.survey.component.service;

import java.util.List;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.manager.Admin;

public interface AdminService extends BaseService<Admin>{

	List<Admin> getAllAdminList();

	void batchDelete(List<Integer> adminIdList);

	void saveAdmin(Admin admin);

	List<Integer> getCurrentRoleIdList(Integer adminId);

	void updateRelationship(Integer adminId, List<Integer> roleIdList);

	Admin login(Admin admin);

}
