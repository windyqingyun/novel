package com.jeeplus.modules.bus.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.jeeplus.modules.bus.enums.BulkBuyTypeEnum;

/**
 * 多章购买类型的dto
 * @author zhangsc
 * @version 2017年12月4日
 */
public class BulkBuyTypeDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer size;   //购买长度
	private Integer type; //(0:从第一章开始购买  1:按照正常顺序购买  2:从倒数的开始购买)
	private BigDecimal discount;  //折扣
	
	public BulkBuyTypeDto(){
		super();
	}
	
	public BulkBuyTypeDto(BulkBuyTypeEnum buyTypeEnum){
		this.size = buyTypeEnum.getSize();
		this.type = buyTypeEnum.getType();
		this.discount = buyTypeEnum.getDiscount();
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
}
