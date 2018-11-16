package com.jeeplus.common.exception;

import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceUtil;

public class EncodeException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * EncodeException
	 * 
	 * @param message
	 */
	public EncodeException(String message) {

		super(message);
	}

	/**
	 * EncodeException失败
	 */
	public EncodeException() {
		this(ServiceUtil.doResponse(RespCodeEnum.U3, true));
	}

}
