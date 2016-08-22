package com.atguigu.survey.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.survey.component.service.LogService;
import com.atguigu.survey.entity.guest.User;
import com.atguigu.survey.entity.manager.Admin;
import com.atguigu.survey.entity.manager.Log;
import com.atguigu.survey.router.RoutingToken;
import com.atguigu.survey.utils.DataProcessUtils;
import com.atguigu.survey.utils.GlobalNames;

/**
 * 日志导航仪
 * 
 * 
 * @author GYX09
 * @data 2016年7月5日下午12:11:02
 */
public class LogRecordor {

	@Autowired
	private LogService logService;

	/**
	 * 通过环绕通知记录日志
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	public Object doRecordLog(ProceedingJoinPoint joinPoint) throws Throwable {

		Object[] args = joinPoint.getArgs();
		Object result;
		String params = DataProcessUtils.convertArrToString(args);

		String methodName = null;
		String typeName = null;
		String returnValue = null;
		String exceptionType = null, exceptionMsg = null;

		try {
			// 获取目标方法的签名
			Signature signature = joinPoint.getSignature();
			// 从签名中读取方法名
			methodName = signature.getName();
			// 从签名中读取方法所在类型的信息
			typeName = signature.getDeclaringTypeName();
			// 调用目标方法
			result = joinPoint.proceed(args);
			// 将result(不为null)的字符串形式赋值给returnValue
			if (result != null) {
				returnValue = result.toString();
			}

		} catch (Throwable e) {
			Throwable ex = e;
			// 判断进一步的原因是否存在
			for (;;) {
				if (e != null) {
					exceptionType = e.getClass().getName();
					exceptionMsg = e.getMessage();
					e = e.getCause();
					continue;
				}
				break;
			}
			// 继续将原异常抛出给框架处理
			throw ex;

		} finally {
			saveLog(params, methodName, typeName, returnValue, exceptionType,
					exceptionMsg);
		}
		return result;// 将执行结果返回
	}

	private void saveLog(String params, String methodName, String typeName,
			String returnValue, String exceptionType, String exceptionMsg) {
		// 获取当前线程上绑定的request对象
		HttpServletRequest request = RequestBinder.getRequest();
		HttpSession session = request.getSession();
		// 获取当前操作的登录用户
		Admin admin = (Admin) session.getAttribute(GlobalNames.LOGIN_ADMIN);
		User user = (User) session.getAttribute(GlobalNames.LOGIN_USER);

		String adminOperator = (admin == null) ? "未登录" : admin.getAdminName();
		String userOperator = (user == null) ? "未登录" : user.getUserName();

		String operator = "admin:" + adminOperator + " ,user:" + userOperator;
		// 捕获当前系统时间
		String operateTime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
				.format(new Date());
		// 判断返回值情况
		returnValue = (returnValue == null) ? "无返回值" : returnValue;
		Log log = new Log(null, operator, operateTime, methodName, typeName,
				params, returnValue, exceptionType, exceptionMsg);

		RoutingToken.setToken(RoutingToken.DATASOURCE_LOG);// 分布式保存日志
		logService.saveLog(log);
	}

}
