package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.manager.Admin;

public interface AdminDao extends BaseDao<Admin>{

	List<Admin> getAllAdminList();

	void batchDelete(List<Integer> adminIdList);

	boolean checkAdminName(String adminName);

	List<Integer> getCurrentRoleIdList(Integer adminId);

	void deleteOldRelationship(Integer adminId);

	void updateRelationship(Integer adminId, List<Integer> roleIdList);

	Admin getAdmin(Admin admin);

}
