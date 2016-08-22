package com.atguigu.survey.component.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.JFreeChart;

import com.atguigu.survey.base.BaseService;
import com.atguigu.survey.entity.guest.Question;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.model.Page;

public interface StatisticsService extends BaseService<Survey>{

	Page<Survey> getLimitedAllAvailablePage(String pageNoStr,
			int pageSize);

	List<String> getTextListByQuestionId(Integer questionId);

	Question getQuestion(Integer questionId);

	JFreeChart getChart(Integer questionId);

	HSSFWorkbook getWorkBook(Integer surveyId);

	
}
