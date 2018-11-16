/**
 * 
 */
package com.jeeplus.modules.bus.service;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.jeeplus.common.cache.GuavaCache;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.constant.Constants;
import com.jeeplus.modules.bus.dao.CustomerDao;
import com.jeeplus.modules.bus.dao.CustomerDetailDao;
import com.jeeplus.modules.bus.entity.Customer;
import com.jeeplus.modules.bus.entity.CustomerDetail;
import com.jeeplus.modules.bus.enums.CustomerEnum;
import com.jeeplus.modules.bus.enums.ResultEnum;
import com.jeeplus.modules.bus.enums.SexEnum;
import com.jeeplus.modules.bus.exception.BookException;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月19日
 * @version V1.0
 *
 */
@Service
@Transactional(readOnly = true)
public class WechatService {
	private Logger logger = LoggerFactory.getLogger(WechatService.class);
	@Autowired
	private WxMpService wxMpService;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerDetailDao customerDetailDao;
	
    /**
     * 绑定微信用户到本地用户
     * @param token
     */
    @Transactional(readOnly = false)
	public void bindingLocalUser(WxMpOAuth2AccessToken token) {
    	Customer customer = null;
        int count = customerDao.isExitsCustomerByOpenidWithOpenidType(token.getOpenId(), CustomerEnum.WECHAT_MP.getCode());
        if (count > 0) {
    		// 查询 
        	customer = customerDao.selectByOpenidWithOpenidType(token.getOpenId(), CustomerEnum.WECHAT_MP.getCode());
        } else {
        	// 绑定
        	customer = addCustomer(token);
        	logger.info("绑定微信用户成功!");
        }
    	if (customer != null && customer.getId() > 0) {
    		// 缓存用户
    		cacheCustomer(token, customer);
    		logger.info("缓存微信用户成功!");
    	}

	}
    
    private void cacheCustomer(WxMpOAuth2AccessToken token, Customer customer) {
    	Map<String, String> map = Maps.newConcurrentMap();
    	map.put(Constants.Customer.CUSTOMER_ID, String.valueOf(customer.getId()));
    	
        map.put(Constants.Customer.OPENID, token.getOpenId());
        map.put(Constants.Customer.OPENID_TYPE, String.valueOf(customer.getOpenidType()));
        map.put(Constants.Customer.NICK_NAME, customer.getNickname());
        map.put(Constants.Customer.HEAD_IMGURL, customer.getHeadimgurl());
        map.put(Constants.Customer.SEX, String.valueOf((customer.getSex())));
        GuavaCache.setKey(Constants.Customer.WECHAT_TOKEN + token.getOpenId(), map);
    }
    
    
    private Customer addCustomer(WxMpOAuth2AccessToken token) {
    	Customer customer = new Customer();
    	WxMpUser userInfo = null;
        try {
			userInfo = wxMpService.oauth2getUserInfo(token, null);
		} catch (WxErrorException e) {
			logger.error("【微信网页授权】获取信息失败{}" , e);
            throw new BookException(ResultEnum.WECHAT_MP_ERROR.getCode() , e.getError().getErrorMsg());
		}
        if (userInfo != null) {
    		try {
	        	customer.setOpenid(userInfo.getOpenId());
	        	customer.setOpenidType(CustomerEnum.WECHAT_OP.getCode());
	        	customer.setUnionid(userInfo.getUnionId());
	    		customer.setNickname(userInfo.getNickname());
	    		customer.setHeadimgurl(userInfo.getHeadImgUrl());
	    		customer.setSex(SexEnum.descOf(userInfo.getSex()));
	    		customer.setCreateTime(new Date());
	    		customer.setUpdateTime(new Date());
	    		customer.setLastLoginTime(new Date());
	    		customer.setIsDel(Integer.parseInt(Global.NO));
	    		
	    		customerDao.insertSelective(customer);
	    		CustomerDetail customerDetail = new CustomerDetail();
	    		customerDetail.setId(customer.getId());
	    		customerDetail.setProvince(userInfo.getProvince());
	    		customerDetail.setCity(userInfo.getCity());
	    		customerDetail.setCountry(userInfo.getCountry());
	    		customerDetail.setPrivilege(userInfo.getProvince());
	    		customerDetailDao.insertSelective(customerDetail);
    		} catch (Exception e) {
    			logger.error("获取微信用户信息失败", e);
    		}
        }
        return customer;
    }

    
}
