package com.atguigu.survey.tag;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.atguigu.survey.utils.GlobalNames;

public class RedisplayTag extends SimpleTagSupport {

	/**
	 * 回显的类型：0-单选，1-多选，2-简答
	 */
	private int type;
	private Integer bagId;
	private String quesId;
	/**
	 * 根据传入的value值对比是否要回显checked（简答题除外）
	 */
	private String value;

	@SuppressWarnings({ "unchecked" })
	@Override
	public void doTag() throws JspException, IOException {
		PageContext page = (PageContext) getJspContext();

		// out的作用：是在页面上显示数据
		JspWriter out = page.getOut();
		// out.print("你好 nb");

		// Session的作用是读取之前保存到Session域中的数据
		HttpSession session = page.getSession();

		// 从Session域中将allBagMap取出来
		Map<Integer, Map<String, String[]>> allBagMap = (Map<Integer, Map<String, String[]>>) session
				.getAttribute(GlobalNames.ALL_BAG_MAP);

		// 根据bagId从allBagMap获取param
		Map<String, String[]> param = allBagMap.get(bagId);
		if (param == null || param.size() == 0) {
			return;
		}

		// 根据请求参数name属性值从param中获取对应的value值
		String[] values = param.get(quesId);
		if (values == null || values.length == 0) {
			return;
		}

		// 根据题型进行不同方式的回显
		if (type == 0 || type == 1) {
			// 将字符串数组转换为List集合，原因是调用集合对象的contains()方法更容易检查是否存在
			List<String> list = Arrays.asList(values);
			if (list.contains(value)) {
				out.print("checked='checked'");
			}
		}

		if (type == 2) {
			// 文本框提交的数据，数组中肯定只有一个值，所以直接使用下标0访问
			out.print(values[0]);
		}

	}

	public void setType(int type) {
		this.type = type;
	}

	public void setBagId(Integer bagId) {
		this.bagId = bagId;
	}

	public void setQuesId(String quesId) {
		this.quesId = quesId;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
