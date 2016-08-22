package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.manager.Res;

public interface ResDao extends BaseDao<Res>{

	boolean checkServletPath(String servletPath);

	Integer getMaxPos();

	Integer getMaxCode(Integer maxPos);

	List<Res> getResList();

	void updateResName(Res res);

	void updateResStatus(Res res);

	void batchDelete(List<Integer> resIdList);

	Res getResByName(String resName);

}
