package com.atguigu.survey.component.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.SurveyDao;
import com.atguigu.survey.component.service.SurveyService;
import com.atguigu.survey.entity.guest.Bag;
import com.atguigu.survey.entity.guest.Question;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.ex.BagEmptyException;
import com.atguigu.survey.ex.SurveyEmptyException;
import com.atguigu.survey.model.Page;

@Service
public class SurveyServiceImpl extends BaseServiceImpl<Survey> implements
		SurveyService {

	@Autowired
	private SurveyDao surveyDao;

	public Page<Survey> getLimitedUncompletedPage(String pageNoStr,
			int pageSize, Integer userId) {

		int totalRecordNo = surveyDao.getUncompletedTotalRecordNo(userId);

		Page<Survey> page = new Page<Survey>(pageNoStr, totalRecordNo, pageSize);
		// 获取经过修正以后的pageNo
		int pageNo = page.getPageNo();

		List<Survey> list = surveyDao.getLimitedUncompletedList(pageNo,
				pageSize, userId);
		page.setList(list);
		return page;
	}

	public void updateSurveyComplete(Integer surveyId) {
		// 根据surveyId查询调查对象
		Survey survey = surveyDao.getEntity(surveyId);
		// 检查是否可以完成
		Set<Bag> bagSet = survey.getBagSet();
		if (bagSet == null || bagSet.size() == 0) {
			// 说明调查中没有包裹
			throw new SurveyEmptyException();
		}

		Iterator<Bag> iterator = bagSet.iterator();
		while (iterator.hasNext()) {
			Bag bag = (Bag) iterator.next();
			Set<Question> questionSet = bag.getQuestionSet();
			if (questionSet == null || questionSet.size() == 0) {
				// 说明包裹中没有问题
				throw new BagEmptyException();
			}
		}

		// 设置完成状态
		surveyDao.updateSurveyComplete(surveyId);

	}

	public Page<Survey> getLimitedAllAvailablePage(String pageNoStr,
			int pageSize) {
		int totalRecordNo = surveyDao.getAvailableTotalRecordNo();

		Page<Survey> page = new Page<Survey>(pageNoStr, totalRecordNo, pageSize);
		// 获取经过修正以后的pageNo
		int pageNo = page.getPageNo();

		List<Survey> list = surveyDao.getLimitedAvailableList(pageNo, pageSize);
		page.setList(list);
		return page;
	}

}
