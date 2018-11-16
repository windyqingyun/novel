package com.jeeplus.modules.bus.utils;

import java.math.BigDecimal;

import com.jeeplus.common.security.Digests;
import com.jeeplus.common.utils.Base64;

public class PaymentUtils {
	/**
	 * 生成以分为单位的金额
	 * @param price
	 * @return
	 */
	public static Integer genWeixinTotalFee(BigDecimal price){
		price = price.multiply(new BigDecimal(100));
		
		return price.intValue();
	}
	
	public static String genSign(String timestamp, String orderCode, String resCode, String key){
		return Base64.encode(Digests.md5((timestamp+orderCode+resCode+key).getBytes()));
	}
}
