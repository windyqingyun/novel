package com.jeeplus.common.exception;

import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.ServiceUtil;
public class DecodeException extends Exception {
	private static final long serialVersionUID = 1L;
/**
 * DecodeException  解密失败
 * @param message
 */
	public DecodeException(String message){
		
		super(message);
	}
	/**
	 * DecodeException 解密失败
	 */
	public DecodeException() {
		this(ServiceUtil.doResponse(RespCodeEnum.U2, true));
	}
	
}
