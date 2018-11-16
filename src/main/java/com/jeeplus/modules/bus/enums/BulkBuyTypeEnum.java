package com.jeeplus.modules.bus.enums;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

/**
 * 多章购买类型枚举
 * @author zhangsc
 * @version 2017年11月22日
 */
public enum BulkBuyTypeEnum {
	/** 后20章 **/
	FORM_END_20("01", "后20章", 20, 2, "0.9"),
	/** 后50章 **/
	FORM_END_50("02", "后50章", 50, 2, "0.8"),
	/** 后100章 **/
	FORM_END_100("03", "后台100章", 100, 2, "0.7"),
	/** 后200章 **/
	FORM_END_200("04", "后200章", 200, 2, "0.6");
	
	private String code;
	private String desc;
	private Integer size;   //购买长度
	private Integer type; //(0:从第一章开始购买  1:按照正常顺序购买  2:从倒数的开始购买)
	private BigDecimal discount;  //折扣
	

	private BulkBuyTypeEnum(String code, String desc, Integer size,
			Integer type, String discount) {
		this.code = code;
		this.desc = desc;
		this.size = size;
		this.type = type;
		this.discount = new BigDecimal(discount);
	}
	
	public static BulkBuyTypeEnum getBulkBuyTypeEnumByCode(String code){
		for (BulkBuyTypeEnum e : values()) {
			if(e.getCode().equals(code)){
				return e;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getSize() {
		return size;
	}

	public Integer getType() {
		return type;
	}

	public BigDecimal getDiscount() {
		return discount;
	}
	
	public JSONObject convetToJson(){
		JSONObject obj = new JSONObject();
		obj.put("discount", this.getDiscount().multiply(BigDecimal.TEN));
		obj.put("code", this.getCode());
		obj.put("des", this.getDesc());
		
		return obj;
	}
}
