package com.atguigu.survey.component.service;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.model.Page;

public interface SurveyService extends BaseService<Survey> {

	/**
	 * 获取未完成的调查（分页）
	 * 
	 * @param pageNoStr
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	Page<Survey> getLimitedUncompletedPage(String pageNoStr, int pageSize,
			Integer userId);

	void updateSurveyComplete(Integer surveyId);

	Page<Survey> getLimitedAllAvailablePage(String pageNoStr, int pageSize);

}
