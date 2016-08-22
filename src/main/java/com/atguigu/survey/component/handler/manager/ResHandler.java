package com.atguigu.survey.component.handler.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.ResService;
import com.atguigu.survey.entity.manager.Res;
import com.atguigu.survey.utils.GlobalNames;

@Controller
@RequestMapping("/manager/res")
public class ResHandler {

	@Autowired
	private ResService resService;

	@RequestMapping("/updateResStatus")
	public void updateResStatus(Res res, HttpServletResponse response)
			throws IOException {

		boolean publicRes = resService.updateResStatus(res);

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		if (publicRes) {
			writer.write("公共资源");
		} else {
			writer.write("受保护资源");
		}
	}

	@RequestMapping("/updateResName")
	public void updateResName(Res res, HttpServletResponse response)
			throws IOException {

		resService.updateResName(res);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("操作成功");

	}

	@RequestMapping("/delRes")
	public String delRes(
			@RequestParam(value = "resIdList", required = false) List<Integer> resIdList) {
		if (resIdList != null && resIdList.size() != 0) {
			resService.batchDelete(resIdList);
		}

		return "redirect:/manager/res/showList";
	}

	@RequestMapping("/showList")
	public String showList(Map<String, Object> map) {

		List<Res> resList = resService.getResList();
		map.put(GlobalNames.RESLIST, resList);

		return "manager/res_list";
	}

}
