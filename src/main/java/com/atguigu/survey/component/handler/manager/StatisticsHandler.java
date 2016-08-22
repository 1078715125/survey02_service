package com.atguigu.survey.component.handler.manager;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.StatisticsService;
import com.atguigu.survey.entity.guest.Question;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.model.Page;
import com.atguigu.survey.utils.GlobalNames;

@Controller
public class StatisticsHandler {

	@Autowired
	private StatisticsService statisticsService;

	@RequestMapping("manager/statistics/exportExcel/{surveyId}")
	public void exportExcel(@PathVariable("surveyId") Integer surveyId,
			HttpServletResponse response) throws IOException {
		HSSFWorkbook workBook =  statisticsService.getWorkBook(surveyId);
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename="+System.nanoTime()+".xls");
		
		ServletOutputStream out = response.getOutputStream();
		
		workBook.write(out);
	}

	@RequestMapping("/manager/statistics/generateChart/{questionId}")
	public void generateChart(@PathVariable("questionId") Integer questionId,
			HttpServletResponse response) throws IOException {
		// 根据questionId生成对应的图表对象
		JFreeChart chart = statisticsService.getChart(questionId);

		// 将图表对象生成的图片数据作为当前请求的响应返回给浏览器

		// 从response对象中获取一个能够返回二进制数据的字节输出流
		// PrintWriter writer = response.getWriter();
		// out是一个字节输出流，writer是一个字符输出流，他们都可以返回响应数据，就看响应数据是什么格式
		ServletOutputStream out = response.getOutputStream();

		// 将chart对象中的数据写入到上述输出流中
		ChartUtilities.writeChartAsJPEG(out, chart, 400, 300);
	}

	@RequestMapping("/manager/statistics/showTextList/{questionId}")
	public String showTextList(@PathVariable("questionId") Integer questionId,
			Map<String, Object> map) {

		Question question = statisticsService.getQuestion(questionId);
		List<String> textList = statisticsService
				.getTextListByQuestionId(questionId);
		// 防止重复数据及空数据
		Set<String> textSet = new HashSet<String>(textList);
		textSet.remove("");
		map.put("textList", textSet);
		map.put("question", question);
		return "manager/statistics_textlist";
	}

	@RequestMapping("/manager/statistics/showSummary/{surveyId}")
	public String showSummary(@PathVariable("surveyId") Integer surveyId,
			Map<String, Object> map) {

		Survey survey = statisticsService.getEntity(surveyId);
		map.put(GlobalNames.SURVEY_MODEL, survey);
		return "manager/statistics_summary";
	}

	@RequestMapping("/manager/statistics/showAllSurvey")
	public String showAllSurvey(
			@RequestParam(value = "pageNo", required = false) String pageNoStr,
			Map<String, Object> map) {

		Page<Survey> page = statisticsService.getLimitedAllAvailablePage(
				pageNoStr, Page.PAGE_SIZE_MIDDLE);
		map.put(GlobalNames.SURVEY_PAGE, page);
		return "manager/statistics_list";
	}
}
