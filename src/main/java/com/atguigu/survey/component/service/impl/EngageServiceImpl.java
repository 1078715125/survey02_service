package com.atguigu.survey.component.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.AnswerDao;
import com.atguigu.survey.component.dao.SurveyDao;
import com.atguigu.survey.component.service.EngageService;
import com.atguigu.survey.entity.guest.Answer;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.model.Page;
import com.atguigu.survey.utils.DataProcessUtils;

@Service
public class EngageServiceImpl extends BaseServiceImpl<Survey> implements
		EngageService {

	@Autowired
	private SurveyDao surveyDao;
	@Autowired
	private AnswerDao answerDao;

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

	public void parseAndSaveAnswer(
			Map<Integer, Map<String, String[]>> allBagMap, Integer surveyId) {
		// 创建一个保存答案数据的集合
		List<Answer> answerList = new ArrayList<Answer>();

		// 从allBagMap中取所有的值
		Collection<Map<String, String[]>> values = allBagMap.values();

		// 遍历values集合
		for (Map<String, String[]> param : values) {

			// 遍历param集合
			Set<Entry<String, String[]>> entrySet = param.entrySet();

			for (Entry<String, String[]> entry : entrySet) {

				// 请求参数名
				String paramName = entry.getKey();
				// 请求参数值
				String[] paramValue = entry.getValue();

				// 检查paramName是否是以q开头
				if (!paramName.startsWith("q")) {
					continue;
				}

				// 从paramName中解析出questionId
				Integer questionId = Integer.parseInt(paramName.substring(1));

				// 将paramValues转换为字符串
				String content = DataProcessUtils
						.convertArrToString(paramValue);

				answerList.add(new Answer(content, questionId, surveyId));
			}

		}
		if (answerList != null && answerList.size() != 0) {
			answerDao.batchSaveAnswerList(answerList);
		}
	}

	public Survey getSurveyById(Integer surveyId) {
		return surveyDao.getEntity(surveyId);
	}

}
