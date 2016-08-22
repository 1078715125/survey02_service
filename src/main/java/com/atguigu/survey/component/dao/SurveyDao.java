package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.guest.Survey;

public interface SurveyDao extends BaseDao<Survey> {

	int getUncompletedTotalRecordNo(Integer userId);

	List<Survey> getLimitedUncompletedList(int pageNo, int pageSize,
			Integer userId);

	void updateSurveyComplete(Integer surveyId);

	int getAvailableTotalRecordNo();

	List<Survey> getLimitedAvailableList(int pageNo, int pageSize);

}
