/**
 * 
 */
package com.jeeplus.modules.bus.enums;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月18日
 * @version V1.0
 *
 */
public enum ResultEnum {
	
	WECHAT_MP_ERROR(10, "微信公众号错误");
	
	private Integer code;
	private String msg;
	
	ResultEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
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

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
