package com.jeeplus.common.log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.RequestUtils;
import com.jeeplus.common.utils.StringUtil;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.Customer;
import com.jeeplus.modules.bus.history.entity.UserClickLog;
import com.jeeplus.modules.bus.history.service.UserClickLogService;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.sys.utils.OriginUtil;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

@Aspect
@Component
public class SystemLogAspect {

	private Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

	private static String[] types = { "java.lang.String", "java.lang.Long", "java.lang.Integer", "java.lang.Boolean",
			"java.lang.Double", "java.lang.Float", "java.lang.Short", "java.lang.Byte", "java.lang.Char", "int",
			"double", "long", "short", "byte", "boolean", "char", "float" };

	private static final Map<String, Integer> map = Maps.newConcurrentMap();

	@Value("${book.viewcount.level}")
	private Integer level = 20;

	@Autowired
	private UserClickLogService userClickLogService;
	@Autowired
	private BookService bookService;

	@Pointcut("@annotation(com.jeeplus.common.log.ControllerLog)")
	public void controllerAspect() {
	}

	/**
	 * 前置通知 用于拦截Controller层记录用户的操作
	 *
	 * @param joinPoint
	 *            切点
	 */
	@AfterReturning(pointcut = "controllerAspect()")
	public void doBefore(JoinPoint joinPoint) {
		handleLog(joinPoint, null);
	}

	// @AfterReturning(pointcut="controllerAspect()",argNames =
	// "joinPoint,retVal",returning = "retVal")
	@AfterThrowing(value = "controllerAspect()", throwing = "e")
	public void doAfter(JoinPoint joinPoint, Exception e) {
		handleLog(joinPoint, e);
	}

	private void handleLog(JoinPoint joinPoint, Exception e) {
		try {
			// 获得注解
			ControllerLog controllerLog = giveController(joinPoint);
			if (controllerLog == null) {
				return;
			}
			// 获取当前的用户
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			Customer currentCustomer = CurrentCustomerUtil.getCurrentCustomer(request);

			// *========数据库日志=========*//
			UserLogModel userlogModel = new UserLogModel();
			String ip = RequestUtils.getIpAddr(request); // 请求的IP
			userlogModel.setIp(ip);
			userlogModel.setCreateTime(new Date());
			userlogModel.setUri(request.getRequestURI());
			userlogModel.setRequest(request);
			if (currentCustomer != null) {
				userlogModel.setUserId(Integer.toString(currentCustomer.getId()));
				userlogModel.setUserName(currentCustomer.getOpenid());
			}else {
				userlogModel.setUserId("0"); //游客
			}

			if (e != null)
				userlogModel.setErrorMessage(e.getMessage());

			// 处理设置注解上的参数
			getControllerMethodDescription(controllerLog, userlogModel, request);

			// 以下为反射
			String classType = joinPoint.getTarget().getClass().getName();
			Class<?> clazz = Class.forName(classType);
			String clazzName = clazz.getName();
			// String clazzSimpleName = clazz.getSimpleName();
			String methodName = joinPoint.getSignature().getName();

			// String[] paramNames = getFieldsName(this.getClass(), clazzName,
			// methodName);
			String[] paramterNameArr = getParamterName(clazzName, methodName);

			// String logContent = writeLogInfo(paramterNameArr, joinPoint);
			Map<String, Object> paramsMap = getParamsMap(paramterNameArr, joinPoint);
			userlogModel.setParamsMap(paramsMap);

			// Logger logger = LoggerFactory.getLogger(clazzName);
			// logger.info("clazzName: " + clazzName + ", methodName:" +
			// methodName + ", param:" + logContent);

			// 记录书籍点击log
			logClick(controllerLog, joinPoint, userlogModel);

		} catch (Exception exp) {
			// 记录本地异常日志
			logger.error("==前置通知异常==");
			logger.error("异常信息:{}", exp.getMessage());
			exp.printStackTrace();
		}
	}

	/**
	 * 记录书籍点击log
	 * 
	 * @param controllerLog
	 * @param joinPoint
	 * @param userlogModel
	 */
	private void logClick(ControllerLog controllerLog, JoinPoint joinPoint, UserLogModel userlogModel) {
		// 保存数据库
		if (controllerLog.action().equals(ActionConstants.CLICK_BOOK)) {
			// 实时记录点击数
			UserClickLog clickLog = new UserClickLog();
			String bookId = (String) userlogModel.getParamsMap().get("bookId");
			clickLog.setClickResource(bookId);
			clickLog.setOrigin(OriginUtil.getOfficeIdByOrigin(userlogModel.getRequest()));
			clickLog.setClickNum(1L);
			clickLog.setCreateTime(new Date());
			clickLog.setCreateBy(userlogModel.getUserId());
			clickLog.setIp(userlogModel.getIp());
			clickLog.setIp(Global.NO);
			userClickLogService.insertSelective(clickLog);
			logger.debug("来自 : {} , 用户id : {} , 点击了bookId : {}", clickLog.getOrigin(), clickLog.getCreateBy(),
					clickLog.getClickResource());

			/**
			 * null 3 	1次
			 * 3 	4	2次
			 * ...
			 * 11	12  10次
			 */
			Integer num = map.get(bookId);
			if (num == null) {
				map.put(bookId, 1);
			}else {
				map.put(bookId, ++num);
				if (num >= level) {
					// 更新
					Book book = bookService.get(bookId);
					if (book != null) {
						book.setCustomviewcount(book.getCustomviewcount() + level);
						bookService.updateCustomviewcount(book);
					}
					map.remove(bookId);
					logger.debug("10次点击, 更新customviewcount");
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println(map.get("df"));
		
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * 
	 * @param joinPoint
	 *            切点
	 * @return 方法描述
	 * @throws Exception
	 */
	public static void getControllerMethodDescription(ControllerLog controllerLog, UserLogModel userlogModel,
			HttpServletRequest request) throws Exception {
		// 设置action动作
		userlogModel.setAction(controllerLog.action());
		// 设置标题
		userlogModel.setTitle(controllerLog.title());
		// 设置channel
		userlogModel.setChannel(controllerLog.channel());
		// 是否需要保存request，参数和值
		if (controllerLog.isSaveRequestData()) {
			// 获取参数的信息，传入到数据库中。
			setRequestValue(userlogModel, request);
		}
	}

	/**
	 * 获取请求的参数，放到log中
	 * 
	 * @param userlogModel
	 * @param request
	 */
	@SuppressWarnings("rawtypes")
	private static void setRequestValue(UserLogModel userlogModel, HttpServletRequest request) {
		if (userlogModel == null)
			userlogModel = new UserLogModel();
		Map map = request.getParameterMap();
		String params = JSONObject.toJSONString(map);
		userlogModel.setRequestParam(params);
	}

	/**
	 * 是否存在注解，如果存在就记录日志
	 * 
	 * @param joinPoint
	 * @param controllerLog
	 * @return
	 * @throws Exception
	 */
	private static ControllerLog giveController(JoinPoint joinPoint) throws Exception {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		if (method != null) {
			return method.getAnnotation(ControllerLog.class);
		}
		return null;
	}

	/**
	 * 得到方法参数的名称
	 * 
	 * @param cls
	 * @param clazzName
	 * @param methodName
	 * @return
	 * @throws NotFoundException
	 */
	private static String[] getFieldsName(Class cls, String clazzName, String methodName) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		// ClassClassPath classPath = new ClassClassPath(this.getClass());
		ClassClassPath classPath = new ClassClassPath(cls);
		pool.insertClassPath(classPath);

		CtClass cc = pool.get(clazzName);
		CtMethod cm = cc.getDeclaredMethod(methodName);
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
		if (attr == null) {
			// exception
		}
		String[] paramNames = new String[cm.getParameterTypes().length];
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos); // paramNames即参数名
		}
		return paramNames;
	}

	private static String writeLogInfo(String[] paramNames, JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		StringBuilder sb = new StringBuilder();
		boolean clazzFlag = true;
		for (int k = 0; k < args.length; k++) {
			Object arg = args[k];
			sb.append(paramNames[k] + " ");
			// 获取对象类型
			String typeName = arg.getClass().getTypeName();

			for (String t : types) {
				if (t.equals(typeName)) {
					sb.append("=" + arg + "; ");
				}
			}
			if (clazzFlag) {
				sb.append(getFieldsValue(arg));
			}
		}
		return sb.toString();
	}

	/**
	 * 简单的封装 可能有bug 自行选择
	 * 
	 * @param paramNames
	 * @param joinPoint
	 * @return
	 */
	private static Map<String, Object> getParamsMap(String[] paramNames, JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		Map map = Maps.newConcurrentMap();
		for (int k = 0; k < args.length; k++) {
			Object arg = args[k];
			map.put(paramNames[k], arg);
		}
		return map;
	}

	/**
	 * 得到参数的值
	 * 
	 * @param obj
	 */
	public static String getFieldsValue(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		String typeName = obj.getClass().getTypeName();
		for (String t : types) {
			if (t.equals(typeName))
				return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("【");
		for (Field f : fields) {
			f.setAccessible(true);
			try {
				for (String str : types) {
					if (f.getType().getName().equals(str)) {
						sb.append(f.getName() + " = " + f.get(obj) + "; ");
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sb.append("】");
		return sb.toString();
	}

	/**
	 * 得到用户的登陆信息--这个还未实现，只是在网上抄的一段
	 * 
	 * @param joinPoint
	 * @throws Exception
	 */
	public void adminOptionContent(JoinPoint joinPoint) throws Exception {
		StringBuffer rs = new StringBuffer();
		String className = null;
		int index = 1;
		Object[] args = joinPoint.getArgs();

		for (Object info : args) {
			// 获取对象类型
			className = info.getClass().getName();
			className = className.substring(className.lastIndexOf(".") + 1);
			rs.append("[参数" + index + "，类型：" + className + "，值：");
			// 获取对象的所有方法
			Method[] methods = info.getClass().getDeclaredMethods();
			// 遍历方法，判断get方法
			for (Method method : methods) {
				String methodName = method.getName();
				System.out.println(methodName);
				// 判断是不是get方法
				if (methodName.indexOf("get") == -1) {// 不是get方法
					continue;// 不处理
				}
				Object rsValue = null;
				try {
					// 调用get方法，获取返回值
					rsValue = method.invoke(info);
					if (rsValue == null) {// 没有返回值
						continue;
					}
				} catch (Exception e) {
					continue;
				}
				// 将值加入内容中
				rs.append("(" + methodName + " : " + rsValue + ")");
			}
			rs.append("]");
			index++;
		}
		System.out.println(rs.toString());
	}

	private String[] getParamterName(String clazzName, String methodName) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		ClassClassPath classPath = new ClassClassPath(this.getClass());
		pool.insertClassPath(classPath);

		CtClass cc = pool.get(clazzName);
		CtMethod cm = cc.getDeclaredMethod(methodName);
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
		if (attr == null) {
			// exception
		}
		String[] paramNames = new String[cm.getParameterTypes().length];
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++)
			paramNames[i] = attr.variableName(i + pos);
		// paramNames即参数名
		// for (int i = 0; i < paramNames.length; i++) {
		// System.out.println(paramNames[i]);
		// }
		return paramNames;
	}

}