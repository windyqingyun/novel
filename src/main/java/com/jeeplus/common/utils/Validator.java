package com.jeeplus.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	
	/**
	 * @MethodName：validate
	 * @Function:
	 *		验证器的门面方法
	 * @HowToImplement
	 * @HowToUse:
	 * @Notice:
	 * @Date:   2015-2-4 下午6:16:36
	 * @author  lining
	 * @param key
	 * @param value
	 * @return
	 * @throws Throwable
	 */
	public static boolean validate(String key, Object value) throws Throwable{
		//状态标志，默认失败
		boolean flag = false;
		if(null == key || null == value){
			throw new RuntimeException("校验键值参数为null");
		}else{
			if("empNo".equalsIgnoreCase(key)){
				flag = validateEmpNo(value);
			}else if("period".equalsIgnoreCase(key)){
				flag = validatePeriod(value);
			}else if("secretKeyId".equalsIgnoreCase(key)){
				flag = validateSecretKeyId(value);
			}else if("year".equalsIgnoreCase(key)){
				flag = validateYear(value);
			}else if("beginTime".equalsIgnoreCase(key) || "endTime".equalsIgnoreCase(key)){
				flag = validateDateTime(value);
			}else if("orgunitLongNum".equalsIgnoreCase(key)){
				flag = validateOrgLongNum(value);
			}else {
				throw new RuntimeException("非MIS系统定义的查询参数");
			}
		}
		return flag;
	}

	/**
	 * @MethodName：validateOrgLongNum
	 * @Function:
	 *		校验组织长编码业务逻辑
	 * @HowToImplement
	 * @HowToUse:
	 * @Notice:
	 * @Date:   2015-3-24 上午10:07:17
	 * @author  lining
	 * @param value
	 * @return
	 */
	private static boolean validateOrgLongNum(Object value){
		return true;
	}
	/**
	 * @MethodName：validateIdNo
	 * @Function:
	 *		校验18位身份证号
	 * @HowToImplement
	 * 		利用正则表达式
	 * @HowToUse:
	 * @Notice:
	 * @Date:   2015-3-29 下午5:24:32
	 * @author  lining
	 * @param idNo
	 * @return
	 */
	public static boolean validateIdNo(String idNo){
		Pattern pattern = Pattern.compile(RegexConst.IDNO_REGEX);
		Matcher matcher = pattern.matcher((CharSequence) idNo);
		return matcher.matches();
	}
	/**
	 * @MethodName：validateDateTime
	 * @Function:
	 *		校验日期时间数据是否符合规则
	 *   日期时间的匹配模式(可以识别闰年) 例如2015-03-23 13:30:49 或 2015-3-23 13:30:49
	 * @HowToImplement
	 * @HowToUse:
	 * @Notice:
	 * @Date:   2015-3-24 上午10:03:20
	 * @author  lining
	 * @param value
	 * @return
	 */
	public static boolean validateDateTime(Object value){
		Pattern pattern = Pattern.compile(RegexConst.DATETIME_REGEX);
		Matcher matcher = pattern.matcher((CharSequence) value);
		return matcher.matches();
	}
	/**
	 * @MethodName：validateYear
	 * @Function:
	 *		年份参数的业务逻辑验证
	 * @HowToImplement
	 * @HowToUse:
	 * @Notice:
	 * @Date:   2015-2-6 下午2:42:30
	 * @author  lining
	 * @param value
	 * @return
	 */
	public static boolean validateYear(Object value) {
		Pattern pattern = Pattern.compile(RegexConst.YEAR_REGEX);
		Matcher matcher = pattern.matcher((CharSequence) value);
		return matcher.matches();
	}

	/**
	 * @MethodName：validateSecretKeyId
	 * @Function:
	 *		验证期间id的合法性
	 * @HowToImplement
	 * @HowToUse:
	 * @Notice:
	 * @Date:   2015-2-5 下午4:12:01
	 * @author  lining
	 * @param value
	 * @return
	 */
	private static boolean validateSecretKeyId(Object value) {
		return true;
	}

	/**
	 * @MethodName：validatePeriod
	 * @Function:
	 *		校验EAS12期间参数
	 * @HowToImplement
	 * @HowToUse:
	 * @Notice:
	 * @Date:   2015-2-4 下午6:12:02
	 * @author  lining
	 * @param value
	 * @return
	 */
	public static boolean validatePeriod(Object value) {
		Pattern pattern = Pattern.compile(RegexConst.PERIOD_REGEX);
		Matcher matcher = pattern.matcher((CharSequence) value);
		return matcher.matches();
	}

	/**
	 * @MethodName：validateEmpNo
	 * @Function:
	 *		校验EAS12位员工工号
	 * @HowToImplement
	 * @HowToUse:
	 * @Notice:
	 * @Date:   2015-2-4 下午6:11:19
	 * @author  lining
	 * @param value
	 * @return
	 */
	public static boolean validateEmpNo(Object value){
		Pattern pattern = Pattern.compile(RegexConst.EMPNO_REGEX);
		Matcher matcher = pattern.matcher((CharSequence) value);
		return matcher.matches();
	}
	/**
	 * @MethodName：validateScore
	 * @Function:
	 *		考试分数校验
	 * @HowToImplement
	 * @HowToUse:
	 * @Notice:
	 * @Date:   
	 * @author  mayongkang
	 * @param value
	 * @return
	 */
	public static boolean validateScore(Object value) {
		Pattern pattern = Pattern.compile(RegexConst.SCORE_REGEX);
		Matcher matcher = pattern.matcher((CharSequence) value);
		return matcher.matches();
	}
	/**
	 * 
	 * @MethodName：validatePositiveInteger
	 * @Function:
	 *		是否是正整数
	 * @Date:   2015-4-2 下午3:27:22
	 * @author  haibao
	 * @param value
	 * @return
	 */
	public static boolean validatePositiveInteger(Object value) {
		Pattern pattern = Pattern.compile(RegexConst.POSITIVE_INTEGER_REGEX);
		Matcher matcher = pattern.matcher((CharSequence) value);
		return matcher.matches();
	}
	
}
