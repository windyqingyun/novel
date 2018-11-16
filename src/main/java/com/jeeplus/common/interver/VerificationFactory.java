/**
 * 
 */
package com.jeeplus.common.interver;

/**
 * @Description 用于校验具体的实现类
 * @author ygq
 * @version 1.0
 * @Date 2015-7-23 下午01:47:04
 */
public class VerificationFactory {
	/****
	 * 静态的验证类工厂
	 * @Description 
	 * @author ygq
	 * @Date 2015-7-23 下午01:48:34
	 * @param type
	 * @return
	 */
	 public static InterVerification getVerificationInstance(String className){
		  InterVerification c = null;
	        try {
	            c=(InterVerification)Class.forName(className).newInstance();//利用反射得到反射的验证类
	        } catch (InstantiationException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return c;
	    }
}
