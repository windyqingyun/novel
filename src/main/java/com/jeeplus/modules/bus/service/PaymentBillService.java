/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.ListUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dao.PaymentBillDao;
import com.jeeplus.modules.bus.dto.PaymentDto;
import com.jeeplus.modules.bus.entity.PaymentBill;
import com.jeeplus.modules.bus.entity.RechargeRule;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

/**
 * 支付单Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class PaymentBillService extends CrudService<PaymentBillDao, PaymentBill> {
	@Autowired
	private UserWalletService userWalletService;
	
	public PaymentBill get(String id) {
		return super.get(id);
	}
	
	public List<PaymentBill> findList(PaymentBill paymentBill) {
		return super.findList(paymentBill);
	}
	
	public Page<PaymentBill> findPage(Page<PaymentBill> page, PaymentBill paymentBill) {
		return super.findPage(page, paymentBill);
	}
	
	@Transactional(readOnly = false)
	public void save(PaymentBill paymentBill) {
		super.save(paymentBill);
	}
	
	@Transactional(readOnly = false)
	public void delete(PaymentBill paymentBill) {
		super.delete(paymentBill);
	}
	
	@Transactional(readOnly = false)
	public void insertNewPaymentBill(PaymentDto paymentDto, String orderCode){
		RechargeRule rule = paymentDto.getRule();  //支付规则
		User buyUser = new User(paymentDto.getUserId());
		
		Date now = new Date();
		PaymentBill paymentBill = new PaymentBill();
		paymentBill.setId(IdGen.uuid());
		paymentBill.setCreateDate(now);
		paymentBill.setPayDate(now);
		paymentBill.setPrice(rule.getPrice());
		paymentBill.setResultCoin(rule.getRstCoin());
		paymentBill.setResultTicket(rule.getRstTicket());
		paymentBill.setCreateBy(buyUser);
		paymentBill.setUser(buyUser);
		paymentBill.setOffice(new Office(paymentDto.getOfficeId()));
		paymentBill.setPayChannel(paymentDto.getPaychannel());
		paymentBill.setPayOrigin(paymentDto.getPayOrigin());
		paymentBill.setIssuccess(Global.NO);
		paymentBill.setOrderCode(orderCode);
		paymentBill.setBookId(paymentDto.getBookId());
		
		dao.insert(paymentBill);
	}
	
	/**
	 * 获取调用支付接口返回的支付单号
	 * @param param
	 * @return
	 */
	public String getOrderCodeFromRstJson(String param){
		try {
			JSONObject jsonObj = JSONObject.parseObject(param);
			String resCode = jsonObj.getString("resCode");
			if(resCode.equals(RespCodeEnum.U0.getResCode())){
				JSONObject dataObj = jsonObj.getJSONObject("data");
				return dataObj.getString("orderCode");
			}else if (resCode.equals(RespCodeEnum.U1.getResCode())) {
				logger.error("ip异常: ", param);
			}
		} catch (Exception e) {
			logger.error("小说站调用支付返回参数解析异常："+ param);
			logger.error("具体如下: ", e);
		}
		return null;
		
	}
	
	
	/**
	 * 支付成功
	 */
	@Transactional(readOnly = false)
	public void paySuccess(String orderCode, String officeId){
		Date now = new Date();
		PaymentBill paymentBill = getPaymentBill(new PaymentBill(orderCode, officeId));
		if(isPaySuccess(paymentBill)){
			return;   //如果支付已经完成 不做处理
		}
		
		if(paymentBill != null && StringUtils.isNotBlank(paymentBill.getId())){
			paymentBill.setIssuccess(Global.YES);
			paymentBill.setSuccessDate(now);
			paymentBill.setUpdateDate(now);
			dao.update(paymentBill);
		
			//添加用户余额
			userWalletService.addCoinAndTicket(userWalletService.getUserWalletByOfficeId(paymentBill.getUser().getId(), officeId).getId(),
					paymentBill.getResultCoin(), paymentBill.getResultTicket());
		}else{
			logger.error("没有查到相关的订单:"+orderCode+",officeId:"+officeId);
		}
	}
	
	public boolean isPaySuccess(PaymentBill paymentBill){
		return paymentBill.getIssuccess().equals(Global.YES);
	}
	
	/**
	 * 支付失败
	 */
	@Transactional(readOnly = false)
	public void payFail(String orderCode, String officeId, String errMsg){
		Date now = new Date();
		PaymentBill paymentBill = getPaymentBill(new PaymentBill(orderCode, officeId));
		if(paymentBill != null && StringUtils.isNotBlank(paymentBill.getId())){
			paymentBill.setErrorMsg(errMsg);
			paymentBill.setUpdateDate(now);
			dao.update(paymentBill);
		}else{
			logger.error("没有查到相关的订单:"+orderCode+",officeId:"+officeId);
		}
	}
	
	/**
	 * 如果没有充值成功的记录的话，返回第一条支付单
	 * @param userId
	 * @return
	 */
	public PaymentBill getFirstPaymentBillIfNoSuc(String userId){
		PaymentBill paymentBill = dao.getFirstPaymentBillIfNoSuc(userId);
		return paymentBill;
	}
	
	public PaymentBill getPaymentBill(PaymentBill paymentBill){
		List<PaymentBill> list = findList(paymentBill);
		if(ListUtils.isEmpty(list)){
			return null;
		}
		return list.get(0);
	}
}