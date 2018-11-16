/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.common.service;

import com.jeeplus.common.enums.RespCodeEnum;

/**
 * Service层公用的Exception, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * @author jeeplus
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
