package com.atguigu.survey.base.impl;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.survey.base.BaseDao;

/**
 * dao的基类，实现基础的dao方法
 * 
 * @author GYX09
 * 
 * @param <T>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseDaoImpl<T> implements BaseDao<T> {

	@Autowired
	private SessionFactory factory;

	private Class<T> entityType;

	public BaseDaoImpl() {
		ParameterizedType pt = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		entityType = (Class<T>) pt.getActualTypeArguments()[0];
	}

	// 为实现其他抽象方法做一些准备
	public Session getSession() {
//		 return factory.openSession();
		return factory.getCurrentSession();
	}

	public Query getQuery(String hql, Object... params) {
		Query query = getSession().createQuery(hql);
		if (params != null) {

			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}

		return query;
	}

	public SQLQuery getSqlQuery(String sql, Object... params) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (params != null) {

			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}

		return query;
	}

	public T getEntity(Integer id) {
		return (T) getSession().get(entityType, id);
	}

	public void removeEntityById(Integer id) {

		String simpleName = entityType.getSimpleName();
		String idName = factory.getClassMetadata(entityType)
				.getIdentifierPropertyName();

		String hql = "delete from " + simpleName + " e where e." + idName
				+ " = ?";
		updateByHql(hql, id);
	}

	public Integer saveEntity(T t) {
		return (Integer) getSession().save(t);
	}

	public void updateEntity(T t) {
		getSession().update(t);
	}

	public void updateBySql(String sql, Object... params) {
		getSqlQuery(sql, params).executeUpdate();
	}

	public List getListBySql(String sql, Object... params) {
		return getSqlQuery(sql, params).list();
	}

	public Integer getTotalRecordNoBySql(String sql, Object... params) {
		BigInteger rec = (BigInteger) getSqlQuery(sql, params).uniqueResult();
		return rec.intValue();
	}

	public List getLimitedListBySql(String sql, int pageNo, int pageSize,
			Object... params) {
		int index = (pageNo - 1) * pageSize;
		return getSqlQuery(sql, params).setFirstResult(index)
				.setMaxResults(pageSize).list();
	}

	public T getEntityByHql(String hql, Object... params) {
		return (T) getQuery(hql, params).uniqueResult();
	}

	public void updateByHql(String hql, Object... params) {
		getQuery(hql, params).executeUpdate();
	}

	public List<T> getListByHql(String hql, Object... params) {
		return getQuery(hql, params).list();
	}

	public Integer getTotalRecordNoByHql(String hql, Object... params) {
		long rec = (Long) getQuery(hql, params).uniqueResult();
		return (int) rec;
	}

	public List<T> getLimitedListByHql(String hql, int pageNo, int pageSize,
			Object... params) {
		int index = (pageNo - 1) * pageSize;
		return getQuery(hql, params).setFirstResult(index)
				.setMaxResults(pageSize).list();
	}

	public void batchUpdate(final String sql, final Object[][] params) {
		getSession().doWork(new Work() {

			public void execute(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql);
				if (params != null) {

					for (int i = 0; i < params.length; i++) {

						Object[] param = params[i];
						if (param != null) {

							for (int j = 0; j < param.length; j++) {

								ps.setObject(j + 1, param[j]);
							}
						}
						ps.addBatch();
					}
				}

				ps.executeBatch();

				if (ps != null) {
					ps.close();
				}
				// ※注意：connection对象还会被后续的操作用到，所以不能关闭。
			}
		});

	}

}
