package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.guest.Bag;

public interface BagDao extends BaseDao<Bag> {

	void updateBag(Bag bag);

	List<Bag> getBagListBySurveyId(Integer surveyId);

	void batchAdjustOrder(List<Integer> bagIdList, List<Integer> bagOrderList);

	void updateRelationshipBymove(Integer bagId, Integer surveyId);


}
