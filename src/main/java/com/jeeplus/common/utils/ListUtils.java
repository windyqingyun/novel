package com.jeeplus.common.utils;

import java.util.List;

/**
 * list工具类
 * @author zhangsc
 * @version 2017年11月13日
 */
public class ListUtils {
	
	public static boolean isEmpty(List list) {
		return list == null || list.isEmpty();
	}
	
	public static boolean isNotEmpty(List list) {
		return !isEmpty(list);
	}
}
