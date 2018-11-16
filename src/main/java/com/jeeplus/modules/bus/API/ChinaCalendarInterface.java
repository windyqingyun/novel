package com.jeeplus.modules.bus.API;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.entity.ProviderUser;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 中华万年历接口
 * @author Administrator
 *
 */
public class ChinaCalendarInterface extends ProviderInterface{
	
	private Office office;
	private String loginUrl = "zhwnl://login";
	private String userInfoUrl = "https://v2-client.suishenyun.cn/openapi/user/getOpenUser";  //获取用户信息的地址
	private String mchId = "2756930574"; //商户id
	private String appId = "laikanzac1895c3688";   //应用id
	private String key = "suishenyunlaikanyuedu2017081519e";              //签名秘钥
	
	
	public ChinaCalendarInterface(Office office){
		super();
		this.office = office;
	}
	
	@Override
	public ProviderUser parseToUser(String result, String originalId) {
		if(StringUtils.isBlank(result)){
			throw new ServiceException(RespCodeEnum.U5, new Throwable("获取中华万年历用户信息失败"));
		}
		JSONObject obj = JSONObject.parseObject(result);
		String status = obj.getString("status");
		if(!status.equals("1000")){
			throw new ServiceException(RespCodeEnum.U4, new Throwable("验证失败"+obj.getString("desc")));
		}
		
		JSONObject data = obj.getJSONObject("data");
		ProviderUser user = new ProviderUser();
		user.setOffice(office);
		user.setName(data.getString("nick"));
		user.setPhoto(data.getString("avatar"));
		user.setSex(data.getString("sex"));
		user.setOriginalId(originalId);
		
		return user;
	}

	/**
	 * 转换为调用用户信息的参数
	 * @throws Exception 
	 */
	@Override
	public Map formateInvorkGetUserInfoPara(String para) throws Exception {
		Map<String, String> map = Maps.newHashMap();
		map.put("appid", appId);
		map.put("mch_id", mchId);
		map.put("suid", JSONObject.parseObject(para).getString("userId"));
		
		//生成签名
		String sign = generateSignature(map, key);
		map.put("sign", sign);
		
		return map;
	}
	
	@Override
	public Office getOffice() {
		return office;
	}

	@Override
	public String getLoginUrl() {
		return loginUrl + "?mch_id ="+mchId+"&appid="+appId;
	}

	@Override
	public String getGetUserInfoUrl() {
		return userInfoUrl;
	}
	
	
	 /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    private String generateSignature(final Map<String, String> data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5(sb.toString()).toUpperCase();
    }
    
    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    private String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
    
    @Override
    public String getInvokMethod() {
    	return ProviderInterface.GET;
    }
}
