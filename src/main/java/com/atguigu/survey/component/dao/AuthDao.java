package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.manager.Authorize;

public interface AuthDao extends BaseDao<Authorize>{

	List<Authorize> getAllAuthList();

	void updateAuthName(Authorize auth);

	List<Integer> getCurrentResIdList(Integer authId);

	void batchDelete(List<Integer> authIdList);

	void deleteOldRelationship(Integer authId);

	void updateRelationship(Integer authId, List<Integer> resIdList);

}
