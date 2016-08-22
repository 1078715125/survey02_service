package com.atguigu.survey.component.handler.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.AuthService;
import com.atguigu.survey.component.service.RoleService;
import com.atguigu.survey.entity.manager.Authorize;
import com.atguigu.survey.entity.manager.Role;
import com.atguigu.survey.utils.GlobalNames;

@Controller
@RequestMapping("/manager/role")
public class RoleHandler {

	@Autowired
	private RoleService roleService;

	@Autowired
	private AuthService authService;

	@RequestMapping("/dispatcher")
	public String dispatcher(
			@RequestParam(value = "authIdList", required = false) List<Integer> authIdList,
			@RequestParam("roleId") Integer roleId) {

		roleService.updateRelationship(roleId,authIdList);
		return "redirect:/manager/role/showList";
	}

	@RequestMapping("/toDispatcher/{roleId}")
	public String toDispatcher(@PathVariable("roleId") Integer roleId,
			Map<String, Object> map) {

		List<Integer> currentAuthIdList = roleService
				.getCurrentAuthIdList(roleId);
		List<Authorize> authList = authService.getAllAuthList();

		map.put("currentAuthIdList", currentAuthIdList);
		map.put("authList", authList);
		map.put("roleId", roleId);

		return "manager/role_dispatcher_auth";
	}

	@RequestMapping("/updateRoleName")
	public void updateRoleName(HttpServletResponse response, Role role)
			throws IOException {

		roleService.updateRoleName(role);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("操作成功");

	}

	@RequestMapping("/delRole")
	public String delRole(
			@RequestParam("roleIdList") List<Integer> roleIdList) {

		roleService.batchDelete(roleIdList);
		return "redirect:/manager/role/showList";
	}

	@RequestMapping("/showList")
	public String showList(Map<String, Object> map) {

		List<Role> roleList = roleService.getAllRoleList();
		map.put(GlobalNames.ROLE_LIST, roleList);
		return "manager/role_list";
	}

	@RequestMapping("/saveRole")
	public String saveRole(Role role) {

		roleService.saveEntity(role);
		return "redirect:/manager/role/showList";
	}

}
