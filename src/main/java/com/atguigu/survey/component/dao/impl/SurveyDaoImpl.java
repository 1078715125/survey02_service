package com.atguigu.survey.component.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.atguigu.survey.base.impl.BaseDaoImpl;
import com.atguigu.survey.component.dao.SurveyDao;
import com.atguigu.survey.entity.guest.Survey;

@Repository
public class SurveyDaoImpl extends BaseDaoImpl<Survey> implements SurveyDao {

	public int getUncompletedTotalRecordNo(Integer userId) {
		String hql = "select count(*) from Survey s where s.completed = false and s.user.userId = ?";

		return getTotalRecordNoByHql(hql, userId);
	}

	public List<Survey> getLimitedUncompletedList(int pageNo, int pageSize,
			Integer userId) {
		String hql = "from Survey s where s.completed = false and s.user.userId = ? order by s.surveyId desc";

		return getLimitedListByHql(hql, pageNo, pageSize, userId);
	}

	public void updateSurveyComplete(Integer surveyId) {

		String hql = "update Survey s set s.completed = true,s.completedTime = ? where s.surveyId = ?";

		updateByHql(hql, new Date(), surveyId);

	}

	public int getAvailableTotalRecordNo() {
		String hql = "select count(*) from Survey s where s.completed = true";

		return getTotalRecordNoByHql(hql);
	}

	public List<Survey> getLimitedAvailableList(int pageNo, int pageSize) {
		String hql = "from Survey s where s.completed = true order by s.surveyId desc";

		return getLimitedListByHql(hql, pageNo, pageSize);
	}

}
