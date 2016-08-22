package com.atguigu.survey.component.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.AuthDao;
import com.atguigu.survey.entity.manager.Authorize;

@Repository
public class AuthDaoImpl extends BaseDaoImpl<Authorize> implements AuthDao{

	public List<Authorize> getAllAuthList() {
		
		String hql = "from Authorize a order by a.authId";
		return getListByHql(hql);
	}

	public void updateAuthName(Authorize auth) {
		String sql = "UPDATE `survey_auth` SET `AUTH_NAME` = ? WHERE `AUTH_ID` = ?";
		updateBySql(sql, auth.getAuthName(),auth.getAuthId());
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getCurrentResIdList(Integer authId) {
		String sql = "SELECT `RES_ID` FROM `survey_inner_auth_res` WHERE `AUTH_ID` = ?";
		return getListBySql(sql, authId);
	}

	public void batchDelete(List<Integer> authIdList) {
		String sql = "DELETE FROM `survey_auth` WHERE `AUTH_ID` = ?";
		Object[][] params = new Object[authIdList.size()][1];
		
		for (int i = 0; i < params.length; i++) {
			params[i][0] = authIdList.get(i);
		}
		batchUpdate(sql, params);
	}

	public void deleteOldRelationship(Integer authId) {
		String sql = "DELETE FROM `survey_inner_auth_res` WHERE `AUTH_ID` = ?";
		updateBySql(sql, authId);
	}

	public void updateRelationship(Integer authId, List<Integer> resIdList) {
		String sql = "INSERT INTO `survey_inner_auth_res`(`AUTH_ID`,`RES_ID`) VALUES(?,?)";
		Object[][] params = new Object[resIdList.size()][2];
		
		for (int i = 0; i < params.length; i++) {
			Object[] param = params[i];
			param[0] = authId;
			param[1] = resIdList.get(i);
		}
		batchUpdate(sql, params);
	}

}
