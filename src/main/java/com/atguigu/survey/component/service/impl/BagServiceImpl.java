package com.atguigu.survey.component.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.BagDao;
import com.atguigu.survey.component.dao.QuestionDao;
import com.atguigu.survey.component.service.BagService;
import com.atguigu.survey.entity.guest.Bag;
import com.atguigu.survey.entity.guest.Question;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.utils.DataProcessUtils;

@Service
public class BagServiceImpl extends BaseServiceImpl<Bag> implements BagService {

	@Autowired
	private BagDao bagDao;
	
	@Autowired
	private QuestionDao questionDao;

	public void saveBag(Bag bag) {

		Integer bagId = bagDao.saveEntity(bag);

		bag.setBagOrder(bagId);// 同一事务中持久化对象属性变化在flush时会自动update

	}

	public void updateBag(Bag bag) {
		bagDao.updateBag(bag);

	}

	public List<Bag> getBagListBySurveyId(Integer surveyId) {

		return bagDao.getBagListBySurveyId(surveyId);
	}

	public void batchAdjustOrder(List<Integer> bagIdList,
			List<Integer> bagOrderList) {
		bagDao.batchAdjustOrder(bagIdList, bagOrderList);

	}

	public void updateRelationshipBymove(Integer bagId, Integer surveyId) {
		bagDao.updateRelationshipBymove(bagId, surveyId);
	}

	public void updateRelationshipBycopy(Integer bagId, Integer surveyId) {
		// 加载一个Bag对象
		Bag bag = bagDao.getEntity(bagId);
		// 复制这个Bag对象
		Bag targetBag = (Bag) DataProcessUtils.deepCopy(bag);
		// 保存这个Bag对象
		// 由于survey没有参与序列化，所以需要额外设置一下
		Survey survey = new Survey(surveyId);
		targetBag.setSurvey(survey);
		bagDao.saveEntity(targetBag);
		
		// 保存这个Bag对象关联的Question集合对象
		Set<Question> questionSet = targetBag.getQuestionSet();
		if (questionSet != null && questionSet.size() > 0) {
			questionDao.batchSave(questionSet);
		}
	}

}
