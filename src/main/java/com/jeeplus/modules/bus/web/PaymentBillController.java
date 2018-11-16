/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.utils.CookieUtil;
import com.jeeplus.common.utils.JsonUtil;
import com.jeeplus.common.utils.RequestUtils;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.dto.PaymentDto;
import com.jeeplus.modules.bus.entity.OfficeInterfaceConfig;
import com.jeeplus.modules.bus.entity.PaymentBill;
import com.jeeplus.modules.bus.entity.RechargeRule;
import com.jeeplus.modules.bus.service.OfficeCoinConfigService;
import com.jeeplus.modules.bus.service.OfficeInterfaceConfigService;
import com.jeeplus.modules.bus.service.PaymentBillService;
import com.jeeplus.modules.bus.service.RechargeRuleService;
import com.jeeplus.modules.bus.utils.CurrentCustomerUtil;
import com.jeeplus.modules.bus.utils.HttpClientUtils;
import com.jeeplus.modules.bus.utils.PaymentUtils;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.OriginUtil;

/**
 * 支付单Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/service/interface/paymentBill")
public class PaymentBillController extends BaseController {

	@Autowired
	private PaymentBillService paymentBillService;
	@Autowired
	private OfficeInterfaceConfigService officeInterfaceConfigService;
	@Autowired
	private RechargeRuleService rechargeRuleService;
	@Autowired
	private OfficeCoinConfigService officeCoinConfigService;

	@RequestMapping(value = "hasFirstRecharge", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String hasFirstRecharge(@RequestParam("userId")String userId, HttpServletRequest request){
		userId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(userId)) {
			return ServiceUtil.doResponse(RespCodeEnum.U6, true);
		}
		
		String result = null;
		Map<String, Object> data = Maps.newHashMap();
		boolean hasFirstRecharge = false;
		
		PaymentBill paymentBill = paymentBillService.getFirstPaymentBillIfNoSuc(userId);
		if(paymentBill != null && paymentBill.getCreateDate() != null){
			Long countDown = System.currentTimeMillis() - paymentBill.getCreateDate().getTime();
			if(countDown < 24*60*60*1000){
				hasFirstRecharge = true;
				data.put("countDown", countDown);
			}
		}
		data.put("hasFirstRecharge", hasFirstRecharge);
		
		result = ServiceUtil.getMessage(RespCodeEnum.U0, data);
		return result;
	}
	
	/**
	 * 调用小说站的支付
	 * @throws Exception 
	 */
	@RequestMapping(value = "recharge", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String recharge(PaymentDto paymentDto, HttpServletRequest request) throws Exception {
		String origin = CookieUtil.getORIGIN(request);
		if (StringUtils.isBlank(origin) || "null".equals(origin)) {
			return ServiceUtil.getMessage(RespCodeEnum.U4, "来源未知");
		}
//		String officeIdOf = OfficeEnum.officeIdOf(origin);
		String officeIdByOrigin = OriginUtil.getOfficeIdByOrigin(request);
		if (StringUtils.isBlank(officeIdByOrigin)) {
			return ServiceUtil.getMessage(RespCodeEnum.U4, "来源没有对应officeid");
		}
		paymentDto.setPayOrigin(officeIdByOrigin); 
		
		String currentCustomerId = CurrentCustomerUtil.getCurrentCustomerId(request);
		if (StringUtils.isBlank(currentCustomerId)) {
			return ServiceUtil.getMessage(RespCodeEnum.U6, "未登录");
		}
		paymentDto.setUserId(currentCustomerId);

		String result = null;
		try {
			OfficeInterfaceConfig config = officeInterfaceConfigService.getOfficeInterfaceConfigByOffice(paymentDto.getOfficeId());
			if(config.getEnable().equals(Global.NO)){
				//未开启
				result = ServiceUtil.getMessage(RespCodeEnum.U4, "小说站支付服务未开启，无法充值");
			}else{
				RechargeRule rule = null;
				if(paymentDto.getIsFirstRecharge() == null || !paymentDto.getIsFirstRecharge().equals(Global.YES)){
					rule = rechargeRuleService.get(paymentDto.getRechargeRuleId());
				}else{
					//如果拥有首次充值机会 可以充值10元的
					rule = new RechargeRule();
					rule.setPrice(new BigDecimal(10));
					rule.setOffice(new Office(paymentDto.getOfficeId()));
					rule.setRstCoin(officeCoinConfigService.getOfficeCoinConfigByOffice(paymentDto.getOfficeId()).getCoinRate().multiply(rule.getPrice()));
					rule.setRstTicket(BigDecimal.ZERO);
				}
				
				if(rule.getOffice().getId().equals(paymentDto.getOfficeId())) {
					//如果选择的充值规则与对应的officeid一样才可以充值
					Map<String, Object> param = Maps.newHashMap();
					param.put("userId", paymentDto.getUserId());
					param.put("price", PaymentUtils.genWeixinTotalFee(rule.getPrice()));
//					param.put("price", new BigDecimal(1)); // 1分钱 
					param.put("userIp", RequestUtils.getIpAddr(request));
					param.put("paychannel", paymentDto.getPaychannel());
					param.put("returnUrl", paymentDto.getReturnUrl());
					
					logger.info("param: {}, rule.setOffice: {}, rule.getRstCoin: {}" , JsonUtil.toPrettyJson(param), rule.getOffice(), rule.getRstCoin());
					HttpClientUtils httpClientUtils = new HttpClientUtils();
					String payParam = httpClientUtils.post(config.getPayInterfaceUrl(), JsonMapper.toJsonString(param), "utf-8");
					
					logger.info("调用"+config.getOffice().getName()+"支付接口返回的信息:" + JsonUtil.toPrettyJson(parseAddOrUpdateParam(payParam)));
					//将支付规则放在paymentDto中，创建一个订单
					paymentDto.setRule(rule);
					String orderNo = paymentBillService.getOrderCodeFromRstJson(RequestUtils.getJson(payParam));
					if(StringUtils.isNotBlank(orderNo)) {
						paymentBillService.insertNewPaymentBill(paymentDto, orderNo);
						logger.info("根据支付订单id 创建订单成功");
						result = ServiceUtil.getMessage(RespCodeEnum.U0, (Object)payParam);
					}else {
						logger.info(RespCodeEnum.U5.getResText());
						result = ServiceUtil.getMessage(RespCodeEnum.U5, "订单号未找到");
					}
				}else {
					result = ServiceUtil.getMessage(RespCodeEnum.U4, "充值规则不属于一个充值机构");
				}
			}
		}catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		
		return result;
	}
	
	/**
	 * 小说站支付的回调
	 * @throws Exception 
	 */
	@RequestMapping(value = "notify", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String notify(HttpServletRequest request) throws Exception {
		String para = request.getParameter("para");
		if(StringUtils.isBlank(para)){
			para = RequestUtils.getRequestPayload(request);
		}
		logger.info("支付通知参数:"+para);
		
		String result = null;
		try {
			JSONObject obj = parseAddOrUpdateParam(para);
			//校验参数
			if(!obj.containsKey("orderCode") || !obj.containsKey("sign") 
					|| !obj.containsKey("timestamp") || !obj.containsKey("officeId")){
				throw new ServiceException(RespCodeEnum.U4, null);
			}
			
			String officeId = obj.getString("officeId");
			OfficeInterfaceConfig config = officeInterfaceConfigService.getOfficeInterfaceConfigByOffice(officeId);
			String key = config.getSecretKey();
			String orderCode = obj.getString("orderCode");
			String timestamp = obj.getString("timestamp");
			String resCode = obj.getString("resCode");
			String sign = obj.getString("sign");
			//验签
			if(sign.equals(PaymentUtils.genSign(timestamp, orderCode, resCode, key))){
				if(resCode.equals(RespCodeEnum.U0.getResCode())){
					//订单支付成功
					paymentBillService.paySuccess(orderCode, officeId);
				}else {
					paymentBillService.payFail(orderCode, officeId, obj.getString("resMsg"));
				}
				result = ServiceUtil.doResponse(RespCodeEnum.U0, true);
			}else{
				result = ServiceUtil.getMessage(RespCodeEnum.U3, "验签失败");
			}
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		return result;
	}
	
	/**
	 * 查询订单状态
	 * @return
	 */
	@RequestMapping(value = "queryPaymentBillStatus", method=RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
	public String queryPaymentBillStatus(@RequestParam("orderCode")String orderCode, @RequestParam("officeId")String officeId, HttpServletRequest request){
		String result = null;
		try {
			PaymentBill paymentBill = paymentBillService.getPaymentBill(new PaymentBill(orderCode, officeId));
			if(paymentBill != null && paymentBill.getIssuccess().equals(Global.YES)){
				result = ServiceUtil.getMessage(RespCodeEnum.U0, "支付成功");
			}else {
				//如果支付单中的错误信息不为空 返回失败
				if(StringUtils.isNoneBlank(paymentBill.getErrorMsg())){
					result = ServiceUtil.getMessage(RespCodeEnum.U4, "支付失败");
				}else {
					//如果没有返回结果 调用查询接口
					OfficeInterfaceConfig config = officeInterfaceConfigService.getOfficeInterfaceConfigByOffice(officeId);
					
					//组织调用参数
					Map<String, String> param = Maps.newHashMap();
					param.put("orderCode", orderCode);
					param.put("paychannel", paymentBill.getPayChannel()); 
					
					HttpClientUtils httpClientUtils = new HttpClientUtils();
					logger.info("支付查询接口：{}, 参数: {}"+config.getPayQueryUrl(), JsonMapper.toJsonString(param));
					String para = httpClientUtils.post(config.getPayQueryUrl(), JsonMapper.toJsonString(param), "utf-8");
					logger.info("支付查询接口返回结果："+para);
					
					JSONObject obj = parseAddOrUpdateParam(para);
					//校验参数
					if(!obj.containsKey("orderCode") || !obj.containsKey("sign") 
							|| !obj.containsKey("timestamp") || !obj.containsKey("officeId")){
						throw new ServiceException(RespCodeEnum.U4, null);
					}
					
					//获取参数并验证签名
					String key = config.getSecretKey();
					String rstOrderCode = obj.getString("orderCode");
					String timestamp = obj.getString("timestamp");
					String resCode = obj.getString("resCode");
					String sign = obj.getString("sign");
					//验签
					if(sign.equals(PaymentUtils.genSign(timestamp, rstOrderCode, resCode, key))){
						if(resCode.equals(RespCodeEnum.U0.getResCode())){
							//订单支付成功
							paymentBillService.paySuccess(orderCode, officeId);
							result = ServiceUtil.getMessage(RespCodeEnum.U0, "支付成功");
						}else {
							paymentBillService.payFail(orderCode, officeId, obj.getString("resMsg"));
							result = ServiceUtil.getMessage(RespCodeEnum.U4, "支付失败");
						}
					}else{
						result = ServiceUtil.getMessage(RespCodeEnum.U3, "验签失败");
					}
				}
			}
		} catch (ServiceException e) {
			logger.error(e.getLocalMsg(), e);
			result = ServiceUtil.doResponse(e.getResEnum(), true);
		} catch (Exception e) {
			logger.error(RespCodeEnum.U1.getResText(), e);
			result = ServiceUtil.doResponse(RespCodeEnum.U1, true);
		}
		logger.info("返回结果:" + result);
		return result;
	}
	
	public JSONObject parseAddOrUpdateParam(String para){
		para = RequestUtils.getJson(para);
		JSONObject jsonObj = JSONObject.parseObject(para);
		if(jsonObj.containsKey("para")){
			jsonObj = jsonObj.getJSONObject("para");
		}
		
		return jsonObj;
	}
}