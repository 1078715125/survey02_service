package com.atguigu.survey.component.handler.guest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.survey.component.service.SurveyService;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.ex.FileTooLargeExceptionForEdit;
import com.atguigu.survey.ex.FileTooLargeExceptionForSave;
import com.atguigu.survey.ex.FileTypeNotAllowedExceptionForEdit;
import com.atguigu.survey.ex.FileTypeNotAllowedExceptionForSave;
import com.atguigu.survey.ex.RemoveSurveyFailedException;
import com.atguigu.survey.model.Page;
import com.atguigu.survey.utils.DataProcessUtils;
import com.atguigu.survey.utils.GlobalNames;
import com.atguigu.survey.utils.GlobalValues;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Controller
public class SurveyHandler {

	@Autowired
	private SurveyService surveyService;

	@RequestMapping("guest/survey/complete/{surveyId}")
	public String complete(@PathVariable("surveyId") Integer surveyId,
			Map<String, Object> map) {

		surveyService.updateSurveyComplete(surveyId);
		map.put("msg", "surveyComplete");
		return "redirect:/guest/success";
	}

	@RequestMapping("guest/survey/toDesignSurvey/{surveyId}")
	public String toDesignSurvey(@PathVariable("surveyId") Integer surveyId,
			Map<String, Object> map) {

		Survey survey = surveyService.getEntity(surveyId);
		map.put(GlobalNames.SURVEY_MODEL, survey);
		return "guest/survey_designUI";
	}

	@RequestMapping("guest/survey/updateSurvey")
	public String updateSurvey(
			@RequestParam("logoFile") MultipartFile logoFile,
			@RequestParam("pageNo") Integer pageNo, Survey survey,
			HttpServletRequest request) throws IOException {
		// 为survey处理上传的logo文件并绑定用户
		resoveLogoAndUser(logoFile, survey, request, "edit");
		surveyService.updateEntity(survey);

		return "redirect:/guest/survey/showMyUncompletedSurvey?pageNo="
				+ pageNo;
	}

	@RequestMapping("guest/survey/toUpdateMyUncompletedSurvey/{surveyId}/{pageNo}")
	public String toUpdateMyUncompletedSurvey(
			@PathVariable("surveyId") Integer surveyId,
			@PathVariable("pageNo") Integer pageNo, Map<String, Object> map) {
		Survey survey = surveyService.getEntity(surveyId);
		map.put(GlobalNames.SURVEY_MODEL, survey);
		map.put("pageNo", pageNo);
		return "guest/survey_editUI";
	}

	@RequestMapping("/guest/survey/toAddUI")
	public String toAddUI(
			@ModelAttribute(GlobalNames.SURVEY_MODEL) Survey survey) {

		survey.setSurveyName("我的调查");

		return "guest/survey_addUI";
	}

	@RequestMapping("/guest/survey/saveSurvey")
	public String saveSurvey(@RequestParam("logoFile") MultipartFile logoFile,
			Survey survey, HttpServletRequest request) throws IOException {
		// 为survey处理上传的logo文件并绑定用户
		resoveLogoAndUser(logoFile, survey, request, "save");
		surveyService.saveEntity(survey);

		return "redirect:/guest/survey/showMyUncompletedSurvey";
	}

	@RequestMapping("guest/survey/showMyUncompletedSurvey")
	public String showMyUncompletedSurvey(HttpServletRequest request,
			@RequestParam(value = "pageNo", required = false) String pageNoStr) {
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute(GlobalNames.LOGIN_USER);

		Page<Survey> page = surveyService.getLimitedUncompletedPage(pageNoStr,
				Page.PAGE_SIZE_MINI, loginUser.getUserId());

		request.setAttribute(GlobalNames.SURVEY_PAGE, page);
		return "guest/survey_uncompleted";
	}

	@RequestMapping("/guest/survey/removeMyUncompletedSurvey/{surveyId}")
	public void removeMyUncompletedSurvey(
			@PathVariable("surveyId") Integer SurveyId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		try {
			surveyService.removeEntityById(SurveyId);
		} catch (Exception e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			if (cause instanceof MySQLIntegrityConstraintViolationException) {
				throw new RemoveSurveyFailedException();
			}
		}

		// 从哪个页面过来再回到那里去
		String ref = request.getHeader("Referer");
		response.sendRedirect(ref);
	}

	private void resoveLogoAndUser(MultipartFile logoFile, Survey survey,
			HttpServletRequest request, String type) throws IOException {
		HttpSession session = request.getSession();
		// 将logo上传至surveyLogos目录下
		if (!logoFile.isEmpty()) {

			long size = logoFile.getSize();
			// 进行文件大小的验证
			if (size > 1024 * 500) {
				request.setAttribute(GlobalNames.SURVEY_MODEL, survey);
				if ("save".equals(type)) {
					throw new FileTooLargeExceptionForSave();
				} else if ("edit".equals(type)) {
					throw new FileTooLargeExceptionForEdit();
				}
			}

			String contentType = logoFile.getContentType();
			// 进行文件类型的验证
			if (!GlobalValues.ALLOWD_TYPES.contains(contentType)) {
				request.setAttribute(GlobalNames.SURVEY_MODEL, survey);
				if ("save".equals(type)) {
					throw new FileTypeNotAllowedExceptionForSave();
				} else if ("edit".equals(type)) {
					throw new FileTypeNotAllowedExceptionForEdit();
				}

			}

			// 指定位置：工程中/webapp/surveyLogos目录。需要将这个虚拟路径转换为真实路径
			String virtualPath = "/surveyLogos";
			String realPath = session.getServletContext().getRealPath(
					virtualPath);
			// 从logoFile对象中获取文件的输入流对象
			InputStream inputStream = logoFile.getInputStream();
			// 调用工具方法压缩图片并保存到surveyLogos目录下
			String logoPath = DataProcessUtils.resizeImages(inputStream,
					realPath);
			survey.setLogoPath(logoPath);
		}

		User loginUser = (User) session.getAttribute(GlobalNames.LOGIN_USER);
		survey.setUser(loginUser);
	}
}
