package com.atguigu.survey.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class RedisplayCheckTag extends SimpleTagSupport {

	private Integer currentId;

	private List<Integer> list;

	@Override
	public void doTag() throws JspException, IOException {
		PageContext page = (PageContext) getJspContext();

		// out的作用：是在页面上显示数据
		JspWriter out = page.getOut();
		// out.print("你好 nb");
		if (list != null && list.size() != 0) {
			
			if (list.contains(currentId)) {
				out.write("checked='checked'");
			}
		}

		

	}

	public Integer getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

}
