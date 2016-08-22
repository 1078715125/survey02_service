package com.atguigu.survey.component.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.QuestionDao;
import com.atguigu.survey.entity.guest.Question;

@Repository
public class QuestionDaoImpl extends BaseDaoImpl<Question> implements
		QuestionDao {

	public void batchSave(Set<Question> questionSet) {
		String sql = "INSERT INTO `survey_question`(`QUESTION_NAME`,`QUESTION_TYPE`,`OPTIONS`,`BAG_ID_FK`) "
				+ "VALUES(?,?,?,?)";
		Object[][] params = new Object[questionSet.size()][4];

		List<Question> list = new ArrayList<Question>(questionSet);
		for (int i = 0; i < params.length; i++) {
			Object[] param = params[i];
			Question question = list.get(i);
			
			param[0] = question.getQuestionName();
			param[1] = question.getQuestionType();
			param[2] = question.getOptions();
			param[3] = question.getBag().getBagId();
		}
		
		batchUpdate(sql, params);
	}

}
