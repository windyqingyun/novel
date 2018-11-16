/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户章节购买记录Entity
 * @author zhangsc
 * @version 2017-11-03
 */
public class UserBuychapterHistory extends DataEntity<UserBuychapterHistory> {
	
	private static final long serialVersionUID = 1L;
	private String bookId;						// 书籍id
	private Integer chapter;					// 购买章节
	private BigDecimal originalprice;			// 原价（单位：阅读币）
	private BigDecimal discount;				// 折扣
	private BigDecimal payCoin;					// 支付的阅读币
	private BigDecimal payTicket;				// 支付的阅读券
	private String bulkbuychapterHistoryId;		// 多章购买的记录id
	private String fodderId;		//素材id
	private String payOrigin;		// 从哪个流量端来的支付
	private String officeId;		// 支付给哪个机构

	
	public UserBuychapterHistory() {
		super();
	}

	public UserBuychapterHistory(String id){
		super(id);
	}

	@Length(min=0, max=64, message="书籍id长度必须介于 0 和 64 之间")
	@ExcelField(title="书籍id", align=2, sort=1)
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
	@ExcelField(title="购买章节", align=2, sort=2)
	public Integer getChapter() {
		return chapter;
	}

	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
	
	@ExcelField(title="原价（单位：阅读币）", align=2, sort=3)
	public BigDecimal getOriginalprice() {
		return originalprice;
	}

	public void setOriginalprice(BigDecimal originalprice) {
		this.originalprice = originalprice;
	}
	
	@ExcelField(title="折扣", align=2, sort=4)
	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
	@ExcelField(title="支付的阅读币", align=2, sort=5)
	public BigDecimal getPayCoin() {
		return payCoin;
	}

	public void setPayCoin(BigDecimal payCoin) {
		this.payCoin = payCoin;
	}
	
	@ExcelField(title="支付的阅读券", align=2, sort=6)
	public BigDecimal getPayTicket() {
		return payTicket;
	}

	public void setPayTicket(BigDecimal payTicket) {
		this.payTicket = payTicket;
	}
	
	@Length(min=0, max=64, message="多章购买的记录id长度必须介于 0 和 64 之间")
	@ExcelField(title="多章购买的记录id", align=2, sort=7)
	public String getBulkbuychapterHistoryId() {
		return bulkbuychapterHistoryId;
	}

	public void setBulkbuychapterHistoryId(String bulkbuychapterHistoryId) {
		this.bulkbuychapterHistoryId = bulkbuychapterHistoryId;
	}

	public String getFodderId() {
		return fodderId;
	}

	public void setFodderId(String fodderId) {
		this.fodderId = fodderId;
	}

	public String getPayOrigin() {
		return payOrigin;
	}

	public void setPayOrigin(String payOrigin) {
		this.payOrigin = payOrigin;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

}