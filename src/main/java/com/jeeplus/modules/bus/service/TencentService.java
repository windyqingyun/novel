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
import com.jeeplus.modules.bus.enums.CustomerEnum;
import com.jeeplus.modules.bus.enums.SexEnum;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月21日
 * @version V1.0
 *
 */
@Service
@Transactional(readOnly = true)
public class TencentService {
	private Logger logger = LoggerFactory.getLogger(TencentService.class);

	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private CustomerDetailDao customerDetailDao;

	/**
	 * 绑定腾讯用户到本地用户
	 */
	@Transactional(readOnly = false)
	public void bindingLocalUser(String openID, UserInfo userInfo, String origin) {
		int count = customerDao.isExitsCustomerByOpenidWithOpenidType(openID, CustomerEnum.TENCENT.getCode());
		Customer customer = null;
		if (count > 0) {
			// 查询
			customer = customerDao.selectByOpenidWithOpenidType(openID, CustomerEnum.TENCENT.getCode());
		} else {
			// 绑定
			customer = addCustomer(openID, userInfo, customer, origin);
			logger.info("绑定腾讯用户成功!");
		}
		if (customer != null && customer.getId() > 0) {
			// 缓存用户
			cacheCustomer(openID, customer);
			logger.info("缓存腾讯用户成功!");
		}

	}

	private void cacheCustomer(String openID, Customer customer) {
		Map<String, String> map = Maps.newConcurrentMap();
		map.put(Constants.Customer.CUSTOMER_ID, String.valueOf(customer.getId()));

		map.put(Constants.Customer.OPENID, openID);
		map.put(Constants.Customer.OPENID_TYPE, String.valueOf(customer.getOpenidType()));
		map.put(Constants.Customer.NICK_NAME, customer.getNickname());
		map.put(Constants.Customer.HEAD_IMGURL, customer.getHeadimgurl());
		map.put(Constants.Customer.SEX, String.valueOf((customer.getSex())));
		GuavaCache.setKey(Constants.Customer.TENCENT_TOKEN + openID, map);
	}

	private Customer addCustomer(String openID, UserInfo userInfo, Customer customer, String origin) {
		UserInfoBean userInfoBean;
		try {
			userInfoBean = userInfo.getUserInfo();
			if (userInfoBean != null) {
				customer = new Customer();
				customer.setOpenid(openID);
				customer.setOpenidType(CustomerEnum.TENCENT.getCode());
				customer.setNickname(userInfoBean.getNickname());
				customer.setHeadimgurl(userInfoBean.getAvatar().getAvatarURL50());
				customer.setSex(SexEnum.descOf(userInfoBean.getGender()));
				customer.setOrigin(origin);	//用户注册来源, 并不是每个请求的来源  切记
				customer.setCreateTime(new Date());
				customer.setUpdateTime(new Date());
				customer.setLastLoginTime(new Date());
				customer.setIsDel(Integer.parseInt(Global.NO));
				customerDao.insertSelective(customer);
			}
		} catch (QQConnectException e) {
			logger.error("获取腾讯用户信息失败", e);
		}
		return customer;
	}

}
