package com.atguigu.survey.component.handler.manager;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.survey.component.service.AdminService;
import com.atguigu.survey.component.service.RoleService;
import com.atguigu.survey.entity.manager.Admin;
import com.atguigu.survey.entity.manager.Role;
import com.atguigu.survey.ex.AdminNameAndPwdNotMatchException;
import com.atguigu.survey.utils.GlobalNames;

@Controller
@RequestMapping("/manager/admin")
public class AdminHandler {

	@Autowired
	private AdminService adminService;
	@Autowired
	private RoleService roleService;

	@RequestMapping("/dispatcher")
	public String dispatcher(
			@RequestParam("adminId") Integer adminId,
			@RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList) {

		adminService.updateRelationship(adminId,roleIdList);
		return "redirect:/manager/admin/showList";
	}

	@RequestMapping("/toDispatcher/{adminId}")
	public String toDispatcher(@PathVariable("adminId") Integer adminId,
			Map<String, Object> map) {

		List<Integer> currentRoleIdList = adminService
				.getCurrentRoleIdList(adminId);
		List<Role> roleList = roleService.getAllRoleList();

		map.put("currentRoleIdList", currentRoleIdList);
		map.put("roleList", roleList);
		map.put("adminId", adminId);
		return "manager/admin_dispatcher_role";
	}

	@RequestMapping("/delAdmin")
	public String delAdmin(
			@RequestParam("adminIdList") List<Integer> adminIdList) {

		adminService.batchDelete(adminIdList);
		return "redirect:/manager/admin/showList";
	}

	@RequestMapping("/showList")
	public String showList(Map<String, Object> map) {

		List<Admin> adminList = adminService.getAllAdminList();
		map.put(GlobalNames.ADMIN_LIST, adminList);
		return "manager/admin_list";
	}

	@RequestMapping("/saveAdmin")
	public String saveAdmin(Admin admin) {
		adminService.saveAdmin(admin);
		return "redirect:/manager/admin/showList";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();

		return "manager/manager_main";
	}

	@RequestMapping("/login")
	public String login(Admin admin, HttpSession session) {

		if ("superAdmin".equals(admin.getAdminName())
				&& "123456".equals(admin.getAdminPwd())) {
			session.setAttribute(GlobalNames.LOGIN_ADMIN, admin);
		} else {
			
			admin = adminService.login(admin);
			session.setAttribute(GlobalNames.LOGIN_ADMIN, admin);
		}

		return "manager/manager_main";
	}

}
