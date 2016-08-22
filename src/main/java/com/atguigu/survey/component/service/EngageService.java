package com.atguigu.survey.component.service;

import java.util.Map;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.model.Page;

public interface EngageService extends BaseService<Survey> {

	Page<Survey> getLimitedAllAvailablePage(String pageNoStr, int pageSize);

	void parseAndSaveAnswer(Map<Integer, Map<String, String[]>> allBagMap,
			Integer surveyId);

	Survey getSurveyById(Integer surveyId);

}
