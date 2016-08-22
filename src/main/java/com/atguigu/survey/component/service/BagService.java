package com.atguigu.survey.component.service;

import java.util.List;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.guest.Bag;

public interface BagService extends BaseService<Bag> {

	void saveBag(Bag bag);

	void updateBag(Bag bag);

	List<Bag> getBagListBySurveyId(Integer surveyId);

	void batchAdjustOrder(List<Integer> bagIdList, List<Integer> bagOrderList);

	void updateRelationshipBymove(Integer bagId, Integer surveyId);

	void updateRelationshipBycopy(Integer bagId, Integer surveyId);

}
