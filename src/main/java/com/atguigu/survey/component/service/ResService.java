package com.atguigu.survey.component.service;

import java.util.List;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.manager.Res;

public interface ResService extends BaseService<Res>{

	boolean checkServletPath(String servletPath);

	Integer getMaxPos();

	Integer getMaxCode(Integer maxPos);

	List<Res> getResList();

	void updateResName(Res res);

	boolean updateResStatus(Res res);

	void batchDelete(List<Integer> resIdList);

	Res getResByName(String resName);

}
