package com.jeeplus.modules.bus.API;

import java.util.Map;

import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.modules.bus.entity.ProviderUser;
import com.jeeplus.modules.bus.utils.HttpClientUtils;
import com.jeeplus.modules.sys.entity.Office;

public abstract class ProviderInterface {
	public static final String GET = "get";
	public static final String POST = "post";
	public static final String UTF8_ENCODING = "UTF-8";
	
	public abstract Office getOffice();

	public abstract String getLoginUrl();

	public abstract String getGetUserInfoUrl();

	/**
	 * 默认是post方法
	 * @return
	 */
	public String getInvokMethod() {
		return POST;
	}
	
	/**
	 * 默认是utf-8编码
	 * @return
	 */
	public String getEncoding() {
		return UTF8_ENCODING;
	}

	/**
	 * 将返回值转换为用户信息
	 * @param result
	 * @return
	 */
	public abstract ProviderUser parseToUser(String result, String originalId) throws Exception;
	
	//调用获取用户信息的方法
	public ProviderUser getUserInfo(String para, String originalId) throws Exception{
		HttpClientUtils clientUtils = new HttpClientUtils();
		
		String result = null;
		Map map = formateInvorkGetUserInfoPara(para);
		if(getInvokMethod().equals(POST)){
			result = clientUtils.post(getGetUserInfoUrl(), JsonMapper.toJsonString(map), getEncoding());
		}else{
			StringBuffer url = new StringBuffer(getGetUserInfoUrl());
			if(map.size() > 0){
				int i = 0;
				for (Object key : map.keySet()) {
					if(i == 0){
						url.append("?"+key.toString()+"="+map.get(key).toString());
					}else{
						url.append("&"+key.toString()+"="+map.get(key).toString());
					}
					i++;
				}
			}
			
			result = clientUtils.get(url.toString(), getEncoding());
		}
	
		return parseToUser(result, originalId);
	}
	
	/**
	 * 格式化调用获取用户信息接口的参数
	 * @param para
	 * @return
	 */
	public abstract Map formateInvorkGetUserInfoPara(String para) throws Exception;
}
