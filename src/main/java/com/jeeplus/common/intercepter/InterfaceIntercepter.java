/**
 * 
 */
package com.jeeplus.common.intercepter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.annotation.ContentProvider;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.enums.RespCodeEnum;
import com.jeeplus.common.utils.RequestUtils;
import com.jeeplus.common.utils.ServiceUtil;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.service.OfficeInterfaceConfigService;
/**
 * @Description 接口校验的拦截器
 * @author ygq
 * @version 1.0
 * @Date 2015-7-27 上午10:55:37
 */
public class InterfaceIntercepter implements HandlerInterceptor {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static OfficeInterfaceConfigService officeInterfaceConfigService = SpringContextHolder.getBean(OfficeInterfaceConfigService.class);
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
		boolean va = true;

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		try{
			//如果拦截的方法有内容提供端注解，需要解密和验证该小说站的接口开关
			if(handlerMethod.getMethodAnnotation(ContentProvider.class) != null
					|| handlerMethod.getBean().getClass().getAnnotation(ContentProvider.class) != null){
				String para =  request.getParameter("para");//如果有参数，则参数名必须叫param
				//如果请求使用原始的ajax请求，tomcat不会把参数放置在request的parameters中,使用流的方式获取参数
				if(StringUtils.isBlank(para)) {
					para = RequestUtils.getRequestPayload(request);
				}
				
//				logger.info("请求地址"+request.getRequestURI()+"请求参数:" + para);
				
				//如果para是json参数，解析json
				JSONObject jsonObj = parseAddOrUpdateParam(para);
				if(jsonObj != null && jsonObj.containsKey("officeId")) {
					logger.info("进入到拦截方法");
					//参数包含officeId，判断机构接口开关
//					if(jsonObj.containsKey("officeId")) {
//						String officeId = jsonObj.getString("officeId");
//						OfficeInterfaceConfig interfaceConfig = officeInterfaceConfigService.get(officeId);
//						if(interfaceConfig == null || interfaceConfig.getEnable().equals(Global.NO)) {
//							va = false;
//							resultJsonMessage(RespCodeEnum.U4, "不存在接口接入权限，请联系系统管理员", response);
//						}else {
//							/*有接入权限，对data进行解密，重新设置request中的para参数
//							  这样在controller中不用重新解密*/
//							String deCode = Des3.decode(jsonObj.getString("data"), interfaceConfig.getKeys());
//							
//							jsonObj.put("data", deCode);
//							para = jsonObj.toJSONString();
//						}
//					}
					//para = jsonObj.toJSONString();
				}
			}
			
			logger.info("拦截结果:"+(va?"通过":"拦截"));
			return va;
		}catch(Exception ex){
			ex.printStackTrace();
			resultJsonMessage(RespCodeEnum.U1, null, response);
			return false;
		}
	}
	
	public JSONObject parseAddOrUpdateParam(String para){
		JSONObject jsonObj = null;
		try {
			para = RequestUtils.getJson(decodeJson(para));
			jsonObj = JSONObject.parseObject(para);
			if(jsonObj.containsKey("para")){
				jsonObj = jsonObj.getJSONObject("para");
			}
		} catch (Exception e) {
			String fileName = Global.getConfig("userfiles.basedir")+System.currentTimeMillis()+".txt";
			File file = new File(fileName);
			try {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(para);
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.info("添加小说错误："+fileName);
			e.printStackTrace();
		}
		
		return jsonObj;
	}
	
	public String decodeJson(String para) throws UnsupportedEncodingException{
		para = URLDecoder.decode(para, "utf-8");
		if(para.startsWith("null")){
			para = para.replace("null", "");
		}
		if(para.startsWith("para=")){
			para = para.replace("para=", "");
		}
		return para;
	}
	
	public void resultJsonMessage(RespCodeEnum respCodeEnum, String errInfo, HttpServletResponse response) throws IOException {
		String message = ServiceUtil.getMessage(respCodeEnum, errInfo);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(message);
		out.flush();
		out.close();
	}
	
}
