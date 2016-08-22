package com.atguigu.survey.base.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.base.BaseService;

/**
 * service的基类，实现基础的service方法
 * 
 * @author GYX09
 * 
 * @param <T>
 */
public class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired
	private BaseDao<T> baseDao;
	
	
	public T getEntity(Integer id) {
		return baseDao.getEntity(id);
	}

	public void removeEntityById(Integer id) {
		baseDao.removeEntityById(id);
	}

	public Integer saveEntity(T t) {
		return baseDao.saveEntity(t);
	}

	public void updateEntity(T t) {
		baseDao.updateEntity(t);

	}

}
