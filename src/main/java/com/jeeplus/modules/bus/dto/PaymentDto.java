package com.jeeplus.modules.bus.dto;

import java.io.Serializable;

import com.jeeplus.modules.bus.entity.RechargeRule;


/**
 * 支付实体，用于调用支付
 * @author zhangsc
 * @version 2017年11月22日
 */
public class PaymentDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private String userId;      //用户id
	private String rechargeRuleId;   //充值规则
	private String officeId;   //机构id
	private String paychannel;     //支付方式(01 微信支付 02 支付宝支付)
	private String payOrigin;		// 支付来源(01:万年历, 02:xxx)
	private String returnUrl;      //成功回调地址
	private String bookId;        //对应的小说
	private String isFirstRecharge;    //是否是首次充值
	
	
	private RechargeRule rule;     //充值规则
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRechargeRuleId() {
		return rechargeRuleId;
	}
	public void setRechargeRuleId(String rechargeRuleId) {
		this.rechargeRuleId = rechargeRuleId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getPaychannel() {
		return paychannel;
	}
	public void setPaychannel(String paychannel) {
		this.paychannel = paychannel;
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
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public RechargeRule getRule() {
		return rule;
	}
	public void setRule(RechargeRule rule) {
		this.rule = rule;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getIsFirstRecharge() {
		return isFirstRecharge;
	}
	public void setIsFirstRecharge(String isFirstRecharge) {
		this.isFirstRecharge = isFirstRecharge;
	}
	
}
