package com.atguigu.survey.component.handler.guest;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.QuestionService;
import com.atguigu.survey.entity.guest.Question;
import com.atguigu.survey.utils.GlobalNames;

@Controller
public class QuestionHandler {

	@Autowired
	private QuestionService questionService;

	@RequestMapping("guest/bag/updateQuestion")
	public String updateQuestion(Question question,
			@RequestParam("surveyId") Integer surveyId) {
		
		questionService.updateEntity(question);
		return "redirect:/guest/survey/toDesignSurvey/" + surveyId;
	}

	@RequestMapping("/guest/question/toEditUI/{questionId}/{surveyId}")
	public String toUpdateQuestion(
			@PathVariable("questionId") Integer questionId,
			@PathVariable("surveyId") Integer surveyId, Map<String, Object> map) {
		Question question = questionService.getEntity(questionId);
		map.put(GlobalNames.QUESTION_MODEL, question);
		Map<String, String> type = new LinkedHashMap<String, String>();
		type.put("0", "单选题");
		type.put("1", "多选题");
		type.put("2", "简答题");
		map.put("type", type);
		map.put("surveyId", surveyId);
		return "guest/question_editUI";
	}

	@RequestMapping("/guest/question/removeQuestion/{questionId}")
	public String removeQuestion(
			@PathVariable("questionId") Integer questionId,
			HttpServletRequest request) {
		questionService.removeEntityById(questionId);
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping("/guest/bag/saveQuestion")
	public String saveQuestion(Question question,
			@RequestParam("surveyId") Integer surveyId) {

		questionService.saveEntity(question);
		return "redirect:/guest/survey/toDesignSurvey/" + surveyId;
	}

	@RequestMapping("/guest/question/toAddUI/{bagId}/{surveyId}")
	public String toAddUI(@PathVariable("bagId") Integer bagId,
			@PathVariable("surveyId") Integer surveyId, Map<String, Object> map) {
		map.put("bagId", bagId);
		map.put("surveyId", surveyId);
		return "guest/question_addUI";
	}

}
