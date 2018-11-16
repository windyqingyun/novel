/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.jeeplus.common.utils;
import com.jeeplus.common.enums.RespCodeEnum;
/**
 * Service层公用的Exception, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * @author ThinkGem
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private RespCodeEnum resEnum;
	private String localMsg;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数 .
	 * 
	 * @param resEnum 响应枚举对象
	 * @param throwable 异常
	 */
	public ServiceException(RespCodeEnum resEnum, Throwable throwable) {
		super(throwable);
		this.resEnum = resEnum;
		this.localMsg = "出错描述:" + resEnum.toString();
	}

	public String getLocalMsg() {
		return localMsg;
	}

	public RespCodeEnum getResEnum() {
		return resEnum;
	}
}
