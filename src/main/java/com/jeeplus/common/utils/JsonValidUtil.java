/**
 * @Filename:   JsonValidUtil.java 
 * @Copyright:  Copyright (c)2015
 * @Company:    CreditEase
 * @version:    mis_alpha
 * @since:  	JDK 1.6
 * @Create at:  2015 下午08:46:01
 * @Description:
 *   	处理Json的工具类
 * @Modification History:   
 * 	Date    		Author      	Version      	Description   
 * ----------------------------------------------------------------- 
 * 	2015-1-27 		lining    		1.0 Version		创建文件
 */
package com.jeeplus.common.utils;

import com.alibaba.fastjson.JSONObject;


/**
 * @ClassName: JsonValidUtil
 * @Description:
 * 		描述此类是对现实世界什么事物的抽象
 * @Date:     2015-1-27 下午08:46:01
 * @author    ygq
 * @version   mis_alpha
 * @since     JDK 1.6 
 */

public class JsonValidUtil {
	private static boolean IsJsonStart(String json) {
		if (!StringUtil.isNull(json)) {
			json = json.trim();
			if (json.length() > 1) {
				char s = json.charAt(0);
				char e = json.charAt(json.length() - 1);
				return (s == '{' && e == '}') || (s == '[' && e == ']');
			}
		}
		return false;
	}
//	public static void main(String[] args) {
//		String test = "[{\"d\":\"abc\"},{\"e\":\"abc\"}]";
//		System.out.println(IsJson(test));
//		Gson gson = new Gson();        
//		List map = gson.fromJson(test, List.class);
//		System.out.println(map);
//	}
	/***
	 * @MethodName：IsJson
	 * @Function:
	 *		判断一个字符串是否是json格式的字符串
	 * @Date:   2015-1-27 下午09:09:39
	 * @author  ygq
	 * @param json
	 * @return
	 */
	public static boolean isJson(String json) {
		int errIndex = 0;
		return isJson(json, errIndex);
	}
	
	/**
	 * 判断是否是json，如果是的话返回jsonObj对象，否则返回null
	 * @param json
	 * @return
	 */
	public static JSONObject isJsonAndRstJsonObj(String json) {
		try {
			JSONObject jsonObj = JSONObject.parseObject(json);
			return jsonObj;
		} catch (Exception e) {
			return null;
		}
	}
	
	static boolean isJson(String json, int errIndex) {
		errIndex = 0;
		if (IsJsonStart(json)) {
			CharState cs = new CharState();
			char c;
			for (int i = 0; i < json.length(); i++) {
				c = json.charAt(i);
				if(CharState.SetCharState(c, cs) && cs.childrenStart){//设置关键符号状态。
					String item = json.substring(i);
					int err = 0;
					int length = GetValueLength(item, true, err);
					cs.childrenStart = false;
					if (err > 0) {
						errIndex = i + err;
						return false;
					}
					i = i + length - 1;
				}
				if (cs.isError) {
					errIndex = i;
					return false;
				}
			}

			return !cs.arrayStart && !cs.jsonStart;
		}
		return false;
	}

	// / <summary>
	// / 获取值的长度（当Json值嵌套以"{"或"["开头时）
	// / </summary>
	private static int GetValueLength(String json, boolean breakOnErr,
			int errIndex) {
		errIndex = 0;
		int len = 0;
		if (!StringUtil.isNull(json)) {
			CharState cs = new CharState();
			char c;
			for (int i = 0; i < json.length(); i++) {
				c = json.charAt(i);
				 if (!CharState.SetCharState(c, cs)){//设置关键符号状态。
					if (!cs.jsonStart && !cs.arrayStart)// json结束，又不是数组，则退出。
					{
						break;
					}
				 }else if (cs.childrenStart){// 正常字符，值状态下。
					int length = GetValueLength(json.substring(i), breakOnErr,
							errIndex);// 递归子值，返回一个长度。。。
					cs.childrenStart = false;
					cs.valueStart = 0;
					// cs.state = 0;
					i = i + length - 1;
				}
				if (breakOnErr && cs.isError) {
					errIndex = i;
					return i;
				}
				if (!cs.jsonStart && !cs.arrayStart)// 记录当前结束位置。
				{
					len = i + 1;// 长度比索引+1
					break;
				}
			}
		}
		return len;
	}
	
}
