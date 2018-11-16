package test;

import java.util.Map;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.utils.JsonUtil;
import com.jeeplus.common.utils.ServiceException;
import com.jeeplus.modules.bus.API.ChinaCalendarInterface;
import com.jeeplus.modules.bus.API.ProviderInterface;
import com.jeeplus.modules.bus.entity.ProviderUser;
import com.jeeplus.modules.bus.utils.HttpClientUtils;
import com.jeeplus.modules.bus.utils.PaymentUtils;
import com.jeeplus.modules.bus.web.ProviderInterfaceController;
import com.jeeplus.modules.sys.entity.Office;

public class test01 {
	
	public static void main(String[] args) {
		Map<String, String> param = Maps.newHashMap();
		param.put("resCode", "0000");
		param.put("orderCode", "3498441");
		param.put("paychannel", "02"); 
		
//		String prettyJson = JsonUtil.toPrettyJson(param);
//		Object parseJson = JsonUtil.parseJson(prettyJson);
//		JSONObject jsonObj = JSONObject.parseObject(prettyJson);
		
		String prettyJson2 = JsonUtil.toJson(param);
		String prettyJson22 = JsonUtil.toJson(param);
		Object parseJson4 = JsonUtil.parseJson(prettyJson2);
//		JSONObject jsonObj4 = JSONObject.parseObject(prettyJson2);
		
		String str = "{\"data\":{\"payUrl\":\"https:\\/\\/statecheck.swiftpass.cn\\/pay\\/wappay?token_id\u003d2c211d0ac3c287197d14b092cbf348164\u0026service\u003dpay.weixin.wappayv2\",\"thirdParty\":\"\\u5a01\\u5bcc\\u901a\",\"reqMethod\":\"GET\",\"payData\":null,\"orderCode\":\"3677\"},\"resMsg\":\"\",\"resCode\":\"0000\"}";
		String str2 ="{\"data\":{\"payUrl\":\"https:\\/\\/statecheck.swiftpass.cn\\/pay\\/wappay?token_id\u003d27382a686898f1e912741c004dab2ede4\u0026service\u003dpay.weixin.wappayv2\",\"thirdParty\":\"\\u5a01\\u5bcc\\u901a\",\"reqMethod\":\"GET\",\"payData\":null,\"orderCode\":\"3687\"},\"resMsg\":\"\",\"resCode\":\"0000\"}";
		JSONObject jsonObj4 = JSONObject.parseObject(str2);
		String resCode = jsonObj4.getString("resCode");
		if(resCode.equals(RespCodeEnum.U0.getResCode())){
			JSONObject dataObj = jsonObj4.getJSONObject("data");
			 String string = dataObj.getString("orderCode");
		}
		
		String jsonString = JsonMapper.toJsonString(param);
		Object parseJson2 = JsonUtil.parseJson(jsonString);
		JSONObject jsonObj2 = JSONObject.parseObject(jsonString);
		
		String str3 = "{\"data\":{\"payUrl\":\"https:\\/\\/statecheck.swiftpass.cn\\/pay\\/wappay?token_id\u003d2c211d0ac3c287197d14b092cbf348164\u0026service\u003dpay.weixin.wappayv2\",\"thirdParty\":\"\\u5a01\\u5bcc\\u901a\",\"reqMethod\":\"GET\",\"payData\":null,\"orderCode\":\"3677\"},\"resMsg\":\"\",\"resCode\":\"0000\"}";
		Object parseJson3 = JsonUtil.parseJson(str2);
		JSONObject jsonObj3 = JSONObject.parseObject(str2);

	}
	
//	public static void main(String[] args) throws Exception {
//		Map<String, String> param = Maps.newHashMap();
//		param.put("orderCode", "3498441");
//		param.put("paychannel", "02"); 
//		
//		HttpClientUtils httpClientUtils = new HttpClientUtils();
//		String para = httpClientUtils.post("http://legion-pre2.c4b619b35aca048e5bbdc3a589b51ca34.cn-beijing.alicontainer.com/nrfx/api/search/order/info", JsonMapper.toJsonString(param), "utf-8");
//		
//		JSONObject obj = parseAddOrUpdateParam(para);
//		//校验参数
//		if(!obj.containsKey("orderCode") || !obj.containsKey("sign") 
//				|| !obj.containsKey("timestamp") || !obj.containsKey("officeId")){
//			throw new ServiceException(RespCodeEnum.U4, null);
//		}
//		
//		//获取参数并验证签名
//		String key = "1WmkOMgQOXyn3bA5pyus4wqgYIRjdIS2xITCwGv3Ucy1gYKpkvBztsd35CS775lw";
//		String rstOrderCode = obj.getString("orderCode");
//		String timestamp = obj.getString("timestamp");
//		String resCode = obj.getString("resCode");
//		String sign = obj.getString("sign");
//		//验签
//		if(sign.equals(PaymentUtils.genSign(timestamp, rstOrderCode, resCode, key))){
//			if(resCode.equals(RespCodeEnum.U0.getResCode())){
//				System.out.println("成功");
//			}else {
//				System.out.println("失败");
//			}
//		}else{
//			System.out.println("验签失败");
//		}
//		
//	}
	
//	public static void main(String[] args) throws Exception {
//		//获取接口
//		ProviderInterface providerInterface = new ChinaCalendarInterface(new Office("200000002001"));
//		String userId = "ed14275cddc024cf622fbf7d829e2f97";
//		
//		/*ec58bdbd455051613423f6669237bd09
//		1ab65c5de76184a2
//		3dc2d1c5cd569bfd*/
//		
//		//保存用户
//		ProviderUser newUser = providerInterface.getUserInfo("{userId:'"+ userId +"'}", userId);
//		newUser.setId(newUser.getOriginalId());
//		System.out.println(newUser.getOriginalId());
//	}
	
	public static JSONObject parseAddOrUpdateParam(String para){
		para = JsonMapper.toJsonString(JSONUtils.parse(para));
		JSONObject jsonObj = JSONObject.parseObject(para);
		if(jsonObj.containsKey("para")){
			jsonObj = jsonObj.getJSONObject("para");
		}
		
		return jsonObj;
	}
}
