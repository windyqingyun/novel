/**
 * 
 */
package com.jeeplus.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.common.constant.Constants;


/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月21日
 * @version V1.0
 *
 */
public class CookieUtil {

	public static void addCookieByName(HttpServletResponse response, String cookieKey, String value, int maxAge) {
        Cookie cookie = new Cookie(cookieKey, value);
        cookie.setDomain(Constants.Cookie.COOKIE_DOMAIN);
        cookie.setPath(Constants.Cookie.PATH);
        //https可以使用, 而http不能使用cookie
        // cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
	
	public static String getCookieValueByKey(HttpServletRequest request, String cookieKey) {
        Cookie[] cookies = request.getCookies();
        if(null != cookies && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if(cookieKey.equals(cookie.getName())) { 		// 2 获取Cookie中的value
                    return cookie.getValue();				    // 3 有 直接使用
                }
            }
        }
        return null;
    }
	
	public static String getCSESSIONID(HttpServletRequest request){
        return getCookieValueByKey(request, Constants.Cookie.CSESSIONID);
    }
	
	public static String getORIGIN(HttpServletRequest request){
        return getCookieValueByKey(request, Constants.Cookie.ORIGIN);
    }
}
