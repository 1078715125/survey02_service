package com.atguigu.survey.component.handler.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.AuthService;
import com.atguigu.survey.component.service.ResService;
import com.atguigu.survey.entity.manager.Authorize;
import com.atguigu.survey.entity.manager.Res;
import com.atguigu.survey.utils.GlobalNames;

@Controller
@RequestMapping("/manager/auth")
public class AuthHandler {

	@Autowired
	private AuthService authService;
	@Autowired
	private ResService resService;

	@RequestMapping("/dispatcher")
	public String dispatcher(
			@RequestParam(value = "resIdList", required = false) List<Integer> resIdList,
			@RequestParam("authId") Integer authId) {
		
		authService.updateRelationship(authId,resIdList);
		return "redirect:/manager/auth/showList";
	}

	@RequestMapping("/toDispatcher/{authId}")
	public String toDispatcher(@PathVariable("authId") Integer authId,
			Map<String, Object> map) {

		List<Integer> currentResIdList = authService
				.getCurrentResIdList(authId);
		List<Res> resList = resService.getResList();
		map.put("currentResIdList", currentResIdList);
		map.put("resList", resList);
		map.put("authId", authId);
		return "manager/auth_dispatcher_res";
	}

	@RequestMapping("/delAuth")
	public String delAuth(@RequestParam("authIdList") List<Integer> authIdList) {

		authService.batchDelete(authIdList);
		return "redirect:/manager/auth/showList";
	}

	@RequestMapping("/updateAuthName")
	public void updateAuthName(HttpServletResponse response, Authorize auth)
			throws IOException {
		authService.updateAuthName(auth);

		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("操作成功");
	}

	@RequestMapping("/showList")
	public String showList(Map<String, Object> map) {

		List<Authorize> authList = authService.getAllAuthList();
		map.put(GlobalNames.AUTH_LIST, authList);
		return "manager/auth_list";
	}

	@RequestMapping("/saveAuth")
	public String saveAuth(Authorize auth) {

		authService.saveEntity(auth);

		return "redirect:/manager/auth/showList";
	}
}
