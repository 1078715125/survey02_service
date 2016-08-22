package com.atguigu.survey.component.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.BagDao;
import com.atguigu.survey.entity.guest.Bag;

@Repository
public class BagDaoImpl extends BaseDaoImpl<Bag> implements BagDao {

	public void updateBag(Bag bag) {
		String hql = "update Bag b set b.bagName = ? where b.bagId = ?";
		updateByHql(hql, bag.getBagName(), bag.getBagId());
	}

	public List<Bag> getBagListBySurveyId(Integer surveyId) {
		String hql = "from Bag b where b.survey.surveyId = ? order by b.bagOrder";
		return getListByHql(hql, surveyId);
	}

	public void batchAdjustOrder(List<Integer> bagIdList,
			List<Integer> bagOrderList) {

		String sql = "UPDATE `survey_bag` SET `BAG_ORDER`=? WHERE `BAG_ID`=?";
		Object[][] params = new Object[bagIdList.size()][2];

		for (int i = 0; i < params.length; i++) {
			Object[] param = params[i];
			param[0] = bagOrderList.get(i);
			param[1] = bagIdList.get(i);
		}

		batchUpdate(sql, params);
	}

	public void updateRelationshipBymove(Integer bagId, Integer surveyId) {
		String hql = "update Bag b set b.survey.surveyId = ? where b.bagId = ?";
		updateByHql(hql, surveyId, bagId);
	}

}
