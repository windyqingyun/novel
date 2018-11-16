package com.jeeplus.common.enums;
public enum RespCodeEnum implements IRespValueEnum{
	/** 操作成功 */
    U0("0000", "操作成功"),
    /** 系统错误 */
    U1("0001","系统错误"),
    /** 解密异常 */
    U2("0002","解密异常"),
    /** 加密异常 */
    U3("0003","加密异常"),
    /** 参数校验异常 */
    U4("0004","参数校验异常"),
    /** 信息获取失败 */
	U5("0005","信息获取失败"),
    /** 未登录 */
    U6("0006","未登录");
    private String resCode = null;
    private String resText = null;
    private String friendlyText = null;

	/**
	 * 构造函数 .
	 * 
	 * @param respCode响应码
	 * @param respMsg响应描述
	 */
	RespCodeEnum(String resCode, String resText) {
		this.resCode = resCode;
		this.resText = resText;
		this.friendlyText = resText;
	}

	/**
	 * 构造函数 .
	 * 
	 * @param respCode响应码
	 * @param respMsg响应描述
	 * @param friendlyMsg友好提示
	 */
	RespCodeEnum(String resCode, String resText, String friendlyText) {
		this.resCode = resCode;
		this.resText = resText;
		this.friendlyText = friendlyText;
	}

	/**
	 * 获取响应码 .
	 * 
	 * @return
	 */
	public String getResCode() {
		return resCode;
	}

	/**
	 * 获取响应描述 .
	 * 
	 * @return
	 */
	public String getResText() {
		return resText;
	}
	
	/***
	 * @Description 获取响应描述 ：该方法只在状态码等于0005的时候使用,故不在对应的接口方法中添加，以免造成调用混乱
	 * @author ygq
	 * @Date 2015-7-23 下午06:00:34
	 * @param str
	 * @return
	 */
	public String getResText(String str) {
		return resText + str;
	}

	/**
	 * 获取友好提示 .
	 * 
	 * @return
	 */
	public String getFriendlyText() {
		return friendlyText;
	}

	/**
	 * 转化为指定格式的字符串 .
	 */
	public String toString() {
		return this.getResCode() + "-" + this.getResText();
	}
}
