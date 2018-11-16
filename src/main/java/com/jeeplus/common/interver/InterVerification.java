/**
 * 
 */
package com.jeeplus.common.interver;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.Validator;
/**
 * @Description 
 * @author ygq
 * @version 1.0
 * @Date 2015-7-23 上午10:02:38
 */
public abstract class InterVerification {
	/***
	 * 接口方法校验的公用类
	 * @Description 
	 * @author ygq
	 * @Date 2015-7-23 上午10:03:42
	 * @param param
	 * @return
	 */
	public String verification(String methodName,String para){
		return null;
	}
	/***
	 * 公用的信息保存的方法
	 * @Description 
	 * @author ygq
	 * @Date 2015-7-23 上午10:03:42
	 * @param param
	 * @return
	 */
	protected String commonVerification(String para){
		StringBuffer validValue = new StringBuffer();
		JSONObject object = JSONObject.parseObject(para);
		String loginName = object.getString("loginName");//根据登陆名获得用户的id
		String leavemessageType = object.getString("leavemessageType");
		if(null == loginName || "".equals(loginName)){
			validValue.append("登录名不能为空,");
		}
		if(null == leavemessageType || "".equals(leavemessageType) ){
			validValue.append("留言类型不能为空,");
		}else{
			boolean leavemessageTypeVa = Validator.validatePositiveInteger(leavemessageType);
			if(leavemessageTypeVa == false){
				validValue.append("留言类型只能为整数,");
			}
		}
		return validValue.toString();
	}
	/***
	 * 公用的信息保存的方法(页数信息校验)
	 * @Description 
	 * @author ygq
	 * @Date 2015-7-23 上午10:03:42
	 * @param param
	 * @return
	 */
	protected String commonPageVerification(String para){
		StringBuffer validValue = new StringBuffer();
		JSONObject object = JSONObject.parseObject(para);
		String pageNo = object.getString("pageNo");//根据登陆名获得用户的id
		String pageSize = object.getString("pageSize");
		if(null == pageNo || "".equals(pageNo) ){
			validValue.append("当前页数不能为空,");
		}else{
			boolean pageNoVa = Validator.validatePositiveInteger(pageNo);
			if(pageNoVa == false){
				validValue.append("当前页数只能为整数,");
			}
		}
		if(null == pageSize || "".equals(pageSize) ){
			validValue.append("每页显示的条数不能为空,");
		}else{
			boolean pageSizeVa = Validator.validatePositiveInteger(pageSize);
			if(pageSizeVa == false){
				validValue.append("每页显示的条数只能为整数,");
			}
		}
		return validValue.toString();
	}
	/***
	 * 截取掉验证参数最后的逗号
	 * @Description 
	 * @author ygq
	 * @Date 2015-7-23 上午11:35:56
	 * @param validValue
	 * @return
	 */
	protected String validStrSubString(String validValue){
		String result = "";
		if(null != validValue &&  !"".equals(validValue)){
			result = validValue.substring(0, validValue.length()-1);
		}
		return result;
	}
}
