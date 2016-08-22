package com.atguigu.survey.component.dao;

import java.util.Set;

import com.atguigu.survey.base.BaseDao;
import com.atguigu.survey.entity.guest.Question;

public interface QuestionDao extends BaseDao<Question> {

	void batchSave(Set<Question> questionSet);

}
