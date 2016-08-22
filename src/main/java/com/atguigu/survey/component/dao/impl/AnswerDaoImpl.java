package com.atguigu.survey.component.dao.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.AnswerDao;
import com.atguigu.survey.entity.guest.Answer;

@Repository
public class AnswerDaoImpl extends BaseDaoImpl<Answer> implements AnswerDao {

	public void batchSaveAnswerList(List<Answer> answerList) {
		String sql = "INSERT INTO `survey_answer`(`CONTENT`,`QUESTION_ID`,`SURVEY_ID`,`UUID`) VALUES(?,?,?,?)";

		Object[][] params = new Object[answerList.size()][4];
		String uuid = UUID.randomUUID().toString();

		for (int i = 0; i < params.length; i++) {
			Object[] param = params[i];
			Answer answer = answerList.get(i);

			param[0] = answer.getContent();
			param[1] = answer.getQuestionId();
			param[2] = answer.getSurveyId();
			param[3] = uuid;
		}

		batchUpdate(sql, params);

	}

	@SuppressWarnings("unchecked")
	public List<String> getTextListByQuestionId(Integer questionId) {
		String sql = "SELECT `CONTENT` FROM `survey_answer` WHERE `QUESTION_ID` = ?";

		return getListBySql(sql, questionId);
	}

	public int getOptionEngagedCount(Integer questionId, int index) {

		String sql = "SELECT COUNT(*) FROM `survey_answer` "
				+ "WHERE `QUESTION_ID`= ? AND CONCAT(',',`CONTENT`,',') LIKE ?";

		String indexStr = "%," + index + ",%";
		return getTotalRecordNoBySql(sql, questionId, indexStr);
	}

	public int getQuestionEngagedCount(Integer questionId) {

		String sql = "SELECT COUNT(*) FROM `survey_answer` WHERE `QUESTION_ID` = ?";

		return getTotalRecordNoBySql(sql, questionId);
	}

	public List<Answer> getAnswerBySurveyId(Integer surveyId) {
		String hql = "from Answer a where a.surveyId = ?";
		return getListByHql(hql, surveyId);
	}

	public int getSurveyEngagedCount(Integer surveyId) {
		String sql = "SELECT COUNT(DISTINCT `UUID`) FROM `survey_answer` WHERE `SURVEY_ID` = ?";
		return getTotalRecordNoBySql(sql, surveyId);
	}

}
