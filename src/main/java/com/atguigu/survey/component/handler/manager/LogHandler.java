package com.atguigu.survey.component.handler.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.LogService;
import com.atguigu.survey.entity.manager.Log;
import com.atguigu.survey.model.Page;
import com.atguigu.survey.router.RoutingToken;
import com.atguigu.survey.utils.DataProcessUtils;

@Controller
@RequestMapping("/manager/log")
public class LogHandler {

	@Autowired
	private LogService logService;

	@RequestMapping("/showList")
	public String showList(
			@RequestParam(value = "pageNo", required = false) String pageNoStr,
			@RequestParam(value = "tableList", required = false) List<String> tableList,
			Map<String, Object> map) {
		List<String> tableNameList = logService.getLogTableNameList();
		// 不显示下三个月的log表
		tableNameList.remove(DataProcessUtils.generateTableName(1));
		tableNameList.remove(DataProcessUtils.generateTableName(2));
		tableNameList.remove(DataProcessUtils.generateTableName(3));

		String currentLog = DataProcessUtils.generateTableName(0);// 当前月

		RoutingToken.setToken(RoutingToken.DATASOURCE_LOG);
		Page<Log> pageLog = logService.getLogListLimitedfromTableName(
				currentLog, pageNoStr);

		map.put("tableNameList", tableNameList);
		map.put("page", pageLog);
		
		if (tableList == null) {
			tableList = new ArrayList<String>();
			tableList.add(currentLog);
		}
		map.put("tableList", tableList);
		return "manager/log_list";
	}
}
