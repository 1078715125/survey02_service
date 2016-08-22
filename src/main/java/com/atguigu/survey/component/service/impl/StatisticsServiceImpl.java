package com.atguigu.survey.component.service.impl;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.survey.base.impl.BaseServiceImpl;
import com.atguigu.survey.component.dao.AnswerDao;
import com.atguigu.survey.component.dao.QuestionDao;
import com.atguigu.survey.component.dao.SurveyDao;
import com.atguigu.survey.component.service.StatisticsService;
import com.atguigu.survey.entity.guest.Answer;
import com.atguigu.survey.entity.guest.Bag;
import com.atguigu.survey.entity.guest.Question;
import com.atguigu.survey.entity.guest.Survey;
import com.atguigu.survey.model.Page;

@Service
public class StatisticsServiceImpl extends BaseServiceImpl<Survey> implements
		StatisticsService {

	@Autowired
	private SurveyDao surveyDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private AnswerDao answerDao;

	public Page<Survey> getLimitedAllAvailablePage(String pageNoStr,
			int pageSize) {
		int totalRecordNo = surveyDao.getAvailableTotalRecordNo();

		Page<Survey> page = new Page<Survey>(pageNoStr, totalRecordNo, pageSize);
		int pageNo = page.getPageNo();// 修正的页码

		List<Survey> list = surveyDao.getLimitedAvailableList(pageNo, pageSize);
		page.setList(list);

		return page;
	}

	public List<String> getTextListByQuestionId(Integer questionId) {

		return answerDao.getTextListByQuestionId(questionId);
	}

	public Question getQuestion(Integer questionId) {

		return questionDao.getEntity(questionId);
	}

	/**
	 * 根据questionId生成统计图
	 */
	public JFreeChart getChart(Integer questionId) {

		// 根据questionId查询Question对象
		Question question = questionDao.getEntity(questionId);
		String questionName = question.getQuestionName();

		// 遍历option数组，生成Dataset对象
		DefaultPieDataset dataset = new DefaultPieDataset();

		String[] option = question.getOptionsArr();
		for (int i = 0; i < option.length; i++) {
			// 选项的显示名称
			String labelName = option[i];
			// 选项的索引
			int index = i;

			// 当前选项被选中的次数
			int count = answerDao.getOptionEngagedCount(questionId, index);

			dataset.setValue(labelName, count);

		}

		// 查询当前问题被参与的次数
		int questionCount = answerDao.getQuestionEngagedCount(questionId);

		// 创建JFreeChart对象，并进行必要的设置
		// 组装title值
		String title = questionName + " " + questionCount + "次参与";

		// 创建JFreeChart对象
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset);

		// 修饰
		chart.getTitle().setFont(new Font("宋体", Font.PLAIN, 20));
		chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 15));

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setForegroundAlpha(0.6f);// 透明度
		plot.setLabelFont(new Font("宋体", Font.PLAIN, 15));
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0},{1}/{3},{2}"));

		return chart;
	}

	/**
	 * 根据questionId生成excel文件的HSSFWorkbook对象
	 */
	public HSSFWorkbook getWorkBook(Integer surveyId) {

		// 准备数据
		Survey survey = surveyDao.getEntity(surveyId);
		String surveyName = survey.getSurveyName();

		// 将Survey对象中级联的所有Question对象封装到List集合中
		List<Question> questionList = new ArrayList<Question>();

		Set<Bag> bagSet = survey.getBagSet();
		for (Bag bag : bagSet) {
			Set<Question> questionSet = bag.getQuestionSet();
			// 将questionSet整体一次性存入questionList
			questionList.addAll(questionSet);
		}

		// 根据surveyId查询List<Answer>
		List<Answer> answerList = answerDao.getAnswerBySurveyId(surveyId);

		// 根据surveyId查询当前调查被参与的次数
		int surveyCount = answerDao.getSurveyEngagedCount(surveyId);

		// 转换数据
		Map<String, Map<Integer, String>> bigMap = convertMap(answerList);

		// 创建HSSFWorkbook对象，并封装数据
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 创建Sheet
		String sheetName = surveyName + " " + surveyCount + "次参与";
		HSSFSheet sheet = workbook.createSheet(sheetName);

		// 创建第一行：问题标题
		HSSFRow hearder = sheet.createRow(0);
		for (int i = 0; i < questionList.size(); i++) {
			HSSFCell cell = hearder.createCell(i);
			Question question = questionList.get(i);

			cell.setCellValue(question.getQuestionName());
		}

		// 创建数据行：遍历bigMap
		if (surveyCount != 0) {
			Set<String> uuidSet = bigMap.keySet();
			List<String> uuidlist = new ArrayList<String>(uuidSet);

			for (int i = 0; i < uuidlist.size(); i++) {
				String uuid = uuidlist.get(i);
				Map<Integer, String> smallMap = bigMap.get(uuid);

				// 索引0已经被标题占用
				HSSFRow dataRow = sheet.createRow(i + 1);

				// 遍历List<Question>生成单元格,与header对应
				for (int j = 0; j < questionList.size(); j++) {
					Question question = questionList.get(j);
					String content = smallMap.get(question.getQuestionId());

					// 将content格式化
					content = convertOption(question, content);

					HSSFCell cell = dataRow.createCell(j);
					cell.setCellValue(content);
				}

			}
		}

		// 设置自动列宽
		for (int j = 0; j < questionList.size(); j++) {
			sheet.autoSizeColumn(j);
		}

		return workbook;
	}

	public String convertOption(Question question, String content) {
		if (content == null || content.length() == 0) {
			return "null";
		}

		if (question.getQuestionType() == 2) {// 简答题答题结果不用格式化
			return content;
		}

		StringBuilder sb = new StringBuilder();
		String[] strs = content.split(",");
		String[] optionsArr = question.getOptionsArr();
		for (String str : strs) {
			int index;
			try {
				index = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				continue;
			}
			sb.append(optionsArr[index] + ",");// 可能会角标越界
		}

		return sb.substring(0, sb.lastIndexOf(","));
	}

	/**
	 * 转化成--Map&lt;uuid,Map&lt;questionId,content&gt;&gt;
	 * 
	 * @param answerList
	 * @return
	 */
	public Map<String, Map<Integer, String>> convertMap(List<Answer> answerList) {

		Map<String, Map<Integer, String>> bigMap = new HashMap<String, Map<Integer, String>>();
		for (Answer answer : answerList) {
			String uuid = answer.getUuid();

			// 保证一个uuid共用一个smallMap
			Map<Integer, String> smallMap = bigMap.get(uuid);
			if (smallMap == null) {
				smallMap = new HashMap<Integer, String>();
				bigMap.put(uuid, smallMap);
			}

			smallMap.put(answer.getQuestionId(), answer.getContent());

		}

		return bigMap;
	}
}
