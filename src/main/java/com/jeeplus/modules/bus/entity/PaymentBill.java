/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

/**
 * 支付单Entity
 * @author zhangsc
 * @version 2017-11-03
 */
public class PaymentBill extends DataEntity<PaymentBill> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private BigDecimal price;		// 支付金额(人民币)
	private String payChannel;		// 支付渠道(0：微信支付，1：支付宝支付)
	private String payOrigin;		// 支付来源 officeid
	private Office office;		// 支付给哪个机构
	private BigDecimal resultCoin;		// 充值返回的阅读币
	private BigDecimal resultTicket;		// 充值返回的阅读券
	private Date payDate;		// 支付时间
	private Date successDate;		// 支付成功时间
	private String issuccess;		// 是否支付成功, 0:失败  1:成功
	private String errorMsg;		// 异常信息
	private String orderCode;       //订单编号
	private String bookId;         //小说编号
	
	//以下为自定义字段
	private Date beginPayDate; //开始支付时间
	private Date endPayDate;  //结束支付时间
	
	public PaymentBill() {
		super();
	}

	public PaymentBill(String id){
		super(id);
	}
	
	public PaymentBill(String orderCode, String officeId){
		super();
		this.orderCode = orderCode;
		this.office = new Office(officeId);
	}

	@NotNull(message="用户id不能为空")
	@ExcelField(title="用户id", fieldType=User.class, value="user.name", align=2, sort=1)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@NotNull(message="支付金额(人民币)不能为空")
	@ExcelField(title="支付金额(人民币)", align=2, sort=2)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	/**
	 * @return the payOrigin
	 */
	public String getPayOrigin() {
		return payOrigin;
	}

	/**
	 * @param payOrigin the payOrigin to set
	 */
	public void setPayOrigin(String payOrigin) {
		this.payOrigin = payOrigin;
	}

	@Length(min=1, max=10, message="支付渠道(0：微信，1：支付宝)长度必须介于 1 和 10 之间")
	@ExcelField(title="支付渠道(0：微信，1：支付宝)", dictType="pay_channel", align=2, sort=3)
	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	
	@NotNull(message="支付给哪个机构不能为空")
	@ExcelField(title="支付给哪个机构", fieldType=Office.class, value="office.name", align=2, sort=4)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="充值返回的阅读币", align=2, sort=5)
	public BigDecimal getResultCoin() {
		return resultCoin;
	}

	public void setResultCoin(BigDecimal resultCoin) {
		this.resultCoin = resultCoin;
	}
	
	@ExcelField(title="充值返回的阅读券", align=2, sort=6)
	public BigDecimal getResultTicket() {
		return resultTicket;
	}

	public void setResultTicket(BigDecimal resultTicket) {
		this.resultTicket = resultTicket;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="支付时间", align=2, sort=7)
	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="支付成功时间", align=2, sort=8)
	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}
	
	@Length(min=1, max=2, message="是否支付成功, 0:否  1:成功长度必须介于 1 和 2 之间")
	@ExcelField(title="是否支付成功, 0:否  1:成功", dictType="yes_no", align=2, sort=9)
	public String getIssuccess() {
		return issuccess;
	}

	public void setIssuccess(String issuccess) {
		this.issuccess = issuccess;
	}
	
	@Length(min=0, max=150, message="异常信息长度必须介于 0 和 150 之间")
	@ExcelField(title="异常信息", align=2, sort=10)
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getBeginPayDate() {
		return beginPayDate;
	}

	public void setBeginPayDate(Date beginPayDate) {
		this.beginPayDate = beginPayDate;
	}

	public Date getEndPayDate() {
		return endPayDate;
	}

	public void setEndPayDate(Date endPayDate) {
		this.endPayDate = endPayDate;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
}