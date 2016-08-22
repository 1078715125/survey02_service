package com.atguigu.survey.component.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.ResDao;
import com.atguigu.survey.entity.manager.Res;

@Repository
public class ResDaoImpl extends BaseDaoImpl<Res> implements ResDao{

	public boolean checkServletPath(String servletPath) {
		String sql = "SELECT COUNT(*) FROM `survey_res` WHERE `RES_NAME` = ?";
		return getTotalRecordNoBySql(sql, servletPath) > 0;
	}

	public Integer getMaxPos() {
		String sql = "SELECT MAX(`RES_POS`) FROM `survey_res`";
		Integer maxPos = (Integer) getSqlQuery(sql).uniqueResult();
		
		return maxPos;
	}

	public Integer getMaxCode(Integer maxPos) {
		String sql = "SELECT MAX(`RES_CODE`) FROM `survey_res` WHERE `RES_POS` = ?";
		Integer maxCode = (Integer) getSqlQuery(sql,maxPos).uniqueResult();
		return maxCode;
	}

	public List<Res> getResList() {
		String hql = "From Res r order by r.resId";
		return getListByHql(hql);
	}

	public void updateResName(Res res) {
		String hql = "update Res r set r.resName = ? where r.resId = ?";
		updateByHql(hql, res.getResName(),res.getResId());
	}

	public void updateResStatus(Res res) {
		String sql = "UPDATE `survey_res` SET `PUBLIC_RES` = !`PUBLIC_RES` WHERE `RES_ID` = ?";
		updateBySql(sql, res.getResId());
	}

	public void batchDelete(List<Integer> resIdList) {
		String sql = "DELETE FROM `survey_res` WHERE `RES_ID`= ?";
		
		Object[][] params = new Object[resIdList.size()][1];
		for (int i = 0; i < params.length; i++) {
			params[i][0] = resIdList.get(i);
		}
		
		batchUpdate(sql, params);
	}

	public Res getResByName(String resName) {
		String hql = "from Res r where r.resName = ?";
		return getEntityByHql(hql, resName);
	}

}
