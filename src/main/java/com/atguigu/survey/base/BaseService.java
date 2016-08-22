package com.atguigu.survey.base;

/**
 * Service根接口
 * 
 * @author GYX09
 * 
 * @param <T>
 */
public interface BaseService<T> {

	/**
	 * 根据entityId查询单个实体类对象
	 * 
	 * @param id
	 * @return
	 */
	T getEntity(Integer id);

	/**
	 * 根据entityId删除实体类对象
	 * 
	 * @param id
	 */
	void removeEntityById(Integer id);

	/**
	 * 保存实体类对象
	 * 
	 * @param t
	 * @return 保存后自增的主键值
	 */
	Integer saveEntity(T t);

	/**
	 * 根据实体类对象进行更新
	 * 
	 * @param t
	 */
	void updateEntity(T t);
}
