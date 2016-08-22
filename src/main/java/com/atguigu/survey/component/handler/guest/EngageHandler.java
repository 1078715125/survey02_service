package com.atguigu.survey.component.handler.guest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.EngageService;
import com.atguigu.survey.entity.guest.Bag;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.model.Page;
import com.atguigu.survey.utils.DataProcessUtils;
import com.atguigu.survey.utils.GlobalNames;

@Controller
public class EngageHandler {

	@Autowired
	private EngageService engageService;

	@SuppressWarnings("unchecked")
	@RequestMapping("guest/engage/engage")
	public String engage(HttpServletRequest request,
			@RequestParam("bagId") Integer bagId,
			@RequestParam("currentIndex") Integer currentIndex) {
		HttpSession session = request.getSession();

		// 从请求域取出请求的map
		Map<String, String[]> param = request.getParameterMap();

		// 从Sessin域取出包裹list与map
		List<Bag> bagList = (List<Bag>) session
				.getAttribute(GlobalNames.BAGLIST);
		Map<Integer, Map<String, String[]>> allBagMap = (Map<Integer, Map<String, String[]>>) session
				.getAttribute(GlobalNames.ALL_BAG_MAP);
		// 合并答案：将param保存到allBagMap中
		// 后面提交的请求参数对以前保存的请求参数有影响，不能使用param本身
		DataProcessUtils.bandParam(allBagMap, bagId, param);

		// 检查当前请求是点击哪个提交按钮之后发送的
		if (param.containsKey("submit_prev")) {
			// 计算下一个包裹的索引值
			currentIndex -= 1;
			// 将下一个包裹保存到请求域中作为“当前包裹”
			request.setAttribute(GlobalNames.CURRENT_BAG,
					bagList.get(currentIndex));
			// 将下一个包裹的索引保存到请求域中
			request.setAttribute(GlobalNames.CURRENT_INDEX, currentIndex);
		} else if (param.containsKey("submit_next")) {
			// 计算下一个包裹的索引值
			currentIndex += 1;
			// 将下一个包裹保存到请求域中作为“当前包裹”
			request.setAttribute(GlobalNames.CURRENT_BAG,
					bagList.get(currentIndex));
			// 将下一个包裹的索引保存到请求域中
			request.setAttribute(GlobalNames.CURRENT_INDEX, currentIndex);

		} else if (param.containsKey("submit_done")) {
			
			//解析并保存答案数据
			Survey survey = (Survey) session
					.getAttribute(GlobalNames.CURRENT_SURVEY);
			engageService.parseAndSaveAnswer(allBagMap,survey.getSurveyId());
			
			session.removeAttribute(GlobalNames.BAGLIST);
			session.removeAttribute(GlobalNames.BAGLIST_SIZE);
			session.removeAttribute(GlobalNames.CURRENT_SURVEY);
			session.removeAttribute(GlobalNames.ALL_BAG_MAP);
			
			return "redirect:/guest/success?msg=engageComplete";
			
		} else if (param.containsKey("submit_quit")) {
			session.removeAttribute(GlobalNames.BAGLIST);
			session.removeAttribute(GlobalNames.BAGLIST_SIZE);
			session.removeAttribute(GlobalNames.CURRENT_SURVEY);
			session.removeAttribute(GlobalNames.ALL_BAG_MAP);
			
			return "redirect:/index.jsp";
		}

		return "guest/engage_engage";
	}

	@RequestMapping("guest/engage/entry/{surveyId}")
	public String entrySurvey(@PathVariable("surveyId") Integer surveyId,
			HttpServletRequest request) {
		HttpSession session = request.getSession();

		// [1]根据surveyId查询Survey对象
		Survey survey = engageService.getSurveyById(surveyId);

		// [2]将Survey对象保存到Session域中
		session.setAttribute(GlobalNames.CURRENT_SURVEY, survey);

		// [3]将Set<Bag>转换为List<Bag>
		// 关注：bagList是否能够保持包裹本身根据bagOrder进行排序后的顺序
		List<Bag> bagList = new ArrayList<Bag>(survey.getBagSet());
		for (Bag bag : bagList) {
			System.out.println(bag);
			System.out.println(bag.getQuestionSet());
		}

		// [4]将List<Bag>保存到Session域中
		session.setAttribute(GlobalNames.BAGLIST, bagList);

		// [5]将List<Bag>集合的长度保存到Session域中
		session.setAttribute(GlobalNames.BAGLIST_SIZE, bagList.size());

		// [6]将第一个包裹保存到请求域中
		request.setAttribute(GlobalNames.CURRENT_BAG, bagList.get(0));

		// [7]将当前要显示的包裹的索引index保存到请求域
		request.setAttribute(GlobalNames.CURRENT_INDEX, 0);

		// [8]创建Map<bagId,param> allBagMap(用于保存答案等信息)
		Map<Integer, Map<String, String[]>> allBagMap = new HashMap<Integer, Map<String, String[]>>();

		// [9]将allBagMap保存到Session域中
		session.setAttribute(GlobalNames.ALL_BAG_MAP, allBagMap);

		return "guest/engage_engage";
	}

	@RequestMapping("guest/engage/showAllAvailableSurvey")
	public String showAllAvailableSurvey(
			@RequestParam(value = "pageNo", required = false) String pageNoStr,
			HttpServletRequest request) {

		Page<Survey> page = engageService.getLimitedAllAvailablePage(pageNoStr,
				Page.PAGE_SIZE_MINI);
		request.setAttribute(GlobalNames.SURVEY_PAGE, page);
		return "guest/engage_list";
	}
}
