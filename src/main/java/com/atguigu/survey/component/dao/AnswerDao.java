package com.atguigu.survey.component.dao;

import java.util.List;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.guest.Answer;

public interface AnswerDao extends BaseDao<Answer> {

	void batchSaveAnswerList(List<Answer> answerList);

	List<String> getTextListByQuestionId(Integer questionId);

	int getOptionEngagedCount(Integer questionId, int index);

	int getQuestionEngagedCount(Integer questionId);

	List<Answer> getAnswerBySurveyId(Integer surveyId);

	int getSurveyEngagedCount(Integer surveyId);

}
