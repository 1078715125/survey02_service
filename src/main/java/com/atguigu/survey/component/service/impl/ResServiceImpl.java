package com.atguigu.survey.component.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.ResDao;
import com.atguigu.survey.component.service.ResService;
import com.atguigu.survey.entity.manager.Res;

@Service
public class ResServiceImpl extends BaseServiceImpl<Res> implements ResService {

	@Autowired
	private ResDao resDao;

	public boolean checkServletPath(String servletPath) {

		return resDao.checkServletPath(servletPath);
	}

	public Integer getMaxPos() {
		
		return resDao.getMaxPos();
	}

	public Integer getMaxCode(Integer maxPos) {
		
		return resDao.getMaxCode(maxPos);
	}

	public List<Res> getResList() {
		
		return resDao.getResList();
	}

	public void updateResName(Res res) {
		resDao.updateResName(res);
	}

	public boolean updateResStatus(Res res) {
		resDao.updateResStatus(res);
		return resDao.getEntity(res.getResId()).isPublicRes();
	}

	public void batchDelete(List<Integer> resIdList) {
		resDao.batchDelete(resIdList);
	}

	public Res getResByName(String resName) {
		
		return resDao.getResByName(resName);
	}
}
