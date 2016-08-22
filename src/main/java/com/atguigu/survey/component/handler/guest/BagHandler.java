package com.atguigu.survey.component.handler.guest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.BagService;
import com.atguigu.survey.component.service.SurveyService;
import com.atguigu.survey.entity.guest.Bag;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.ex.BagOrderDuplicateException;
import com.atguigu.survey.ex.RemoveBagFailedException;
import com.atguigu.survey.model.Page;
import com.atguigu.survey.utils.DataProcessUtils;
import com.atguigu.survey.utils.GlobalNames;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Controller
public class BagHandler {

	@Autowired
	private BagService bagService;

	@Autowired
	private SurveyService surveyService;

	@RequestMapping("guest/bag/copyToThisSurvey/{bagId}/{surveyId}")
	public String copyToThisSurvey(@PathVariable("bagId") Integer bagId,
			@PathVariable("surveyId") Integer surveyId) {

		bagService.updateRelationshipBycopy(bagId, surveyId);
		return "redirect:/guest/survey/toDesignSurvey/" + surveyId;
	}

	@RequestMapping("guest/bag/moveToThisSurvey/{bagId}/{surveyId}")
	public String moveToThisSurvey(@PathVariable("bagId") Integer bagId,
			@PathVariable("surveyId") Integer surveyId) {

		bagService.updateRelationshipBymove(bagId, surveyId);
		return "redirect:/guest/survey/toDesignSurvey/" + surveyId;
	}

	@RequestMapping("/guest/bag/toTargetSurveyUI/{bagId}/{surveyId}")
	public String toTargetSurveyUI(@PathVariable("bagId") Integer bagId,
			@PathVariable("surveyId") Integer surveyId,
			@RequestParam(value = "pageNo", required = false) String pageNoStr,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(GlobalNames.LOGIN_USER);

		Page<Survey> page = surveyService.getLimitedUncompletedPage(pageNoStr,
				Page.PAGE_SIZE_MINI, user.getUserId());

		request.setAttribute(GlobalNames.SURVEY_PAGE, page);
		request.setAttribute("bagId", bagId);
		request.setAttribute("surveyId", surveyId);
		return "guest/bag_copyOrMoveUI";
	}

	@RequestMapping("/guest/bag/adjustOrder")
	public String adjustOrder(
			@RequestParam(value = "surveyId", required = false) Integer surveyId,
			@RequestParam(value = "bagIdList", required = false) List<Integer> bagIdList,
			@RequestParam(value = "bagOrderList", required = false) List<Integer> bagOrderList,
			HttpServletRequest request) {
		
		if (bagIdList == null || bagIdList.size() == 0) {
			return "redirect:/guest/survey/toDesignSurvey/" + surveyId;
		}
		// 检查bagOrderList中有无重复order数据，通过Set集合检查
		boolean right = DataProcessUtils.checkAdjustOrder(bagOrderList);
		if (!right) {
			List<Bag> bagList = bagService.getBagListBySurveyId(surveyId);
			request.setAttribute("bagList", bagList);
			request.setAttribute("surveyId", surveyId);
			throw new BagOrderDuplicateException();
		}

		bagService.batchAdjustOrder(bagIdList, bagOrderList);

		return "redirect:/guest/survey/toDesignSurvey/" + surveyId;
	}

	@RequestMapping("/guest/bag/toOrderAdjustUI/{surveyId}")
	public String toOrderAdjustUI(@PathVariable("surveyId") Integer surveyId,
			Map<String, Object> map) {

		List<Bag> bagList = bagService.getBagListBySurveyId(surveyId);
		map.put("bagList", bagList);
		map.put("surveyId", surveyId);
		return "guest/bag_orderAdjustUI";
	}

	@RequestMapping("/guest/bag/updateBag")
	public String updateBag(Bag bag) {

		bagService.updateBag(bag);
		return "redirect:/guest/survey/toDesignSurvey/"
				+ bag.getSurvey().getSurveyId();
	}

	@RequestMapping("/guest/bag/toEditUI/{bagId}")
	public String toEditUI(@PathVariable("bagId") Integer bagId,
			Map<String, Object> map) {

		Bag bag = bagService.getEntity(bagId);
		map.put(GlobalNames.BAG_MODEL, bag);
		return "guest/bag_editUI";
	}

	@RequestMapping("/guest/bag/removeBag/{bagId}")
	public String removeBag(@PathVariable("bagId") Integer bagId,
			HttpServletRequest request) {

		try {
			bagService.removeEntityById(bagId);
		} catch (Exception e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			if (cause instanceof MySQLIntegrityConstraintViolationException) {
				throw new RemoveBagFailedException();
			}
		}
		String ref = request.getHeader("Referer");
		return "redirect:" + ref;
	}

	@RequestMapping("/guest/bag/saveBag")
	public String saveBag(Bag bag) {

		bagService.saveBag(bag);
		return "redirect:/guest/survey/toDesignSurvey/"
				+ bag.getSurvey().getSurveyId();
	}

	@RequestMapping("/guest/bag/toAddUI/{surveyId}")
	public String toAddUI(@PathVariable("surveyId") Integer surveyId,
			Map<String, Object> map) {

		map.put("surveyId", surveyId);
		return "guest/bag_addUI";
	}

}
