package com.atguigu.survey.component.service;

import java.util.List;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.manager.Authorize;

public interface AuthService extends BaseService<Authorize>{

	List<Authorize> getAllAuthList();

	void updateAuthName(Authorize auth);

	List<Integer> getCurrentResIdList(Integer authId);

	void batchDelete(List<Integer> authIdList);

	void updateRelationship(Integer authId, List<Integer> resIdList);

}
