/**
 * 
 */
package com.jeeplus.modules.bus.exception;

import com.jeeplus.modules.bus.enums.ResultEnum;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月18日
 * @version V1.0
 *
 */
public class BookException extends RuntimeException {
	
	private Integer code;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7285486274441306466L;
	
	public BookException(ResultEnum resultEnum) {
		super(resultEnum.getMsg());
		this.code = code;
	}
	
	public BookException(Integer code, String msg) {
		super(msg);
		this.code = code;
	}

	/**
	 * 系统自定义状态码
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	
	
}
