package com.jeeplus.modules.bus.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.bus.dao.BookUserDao;
import com.jeeplus.modules.bus.entity.BookUser;
import com.jeeplus.modules.bus.login.AfterLoginRedirectServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


/**
 * 微信
 */
@Service
public class WXService {

    private Logger logger = LoggerFactory.getLogger(WXService.class);

    @Autowired
    private BookUserDao bookUserDao;
    /**
     * 请求连接登录后2小时候不需要登录
     */
    private static String URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static String APPID;
    private static String SECRET;
    private static String GRANT_TYPE = "authorization_code";
    private static String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";
    private static String UATH_URL = "https://api.weixin.qq.com/sns/auth";
    private static String REF_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    private static RestTemplate restTemplate = new RestTemplate();
    /**
     * app微信登录
     * https://open.weixin.qq.com 申请网址
     * @param code
     */
    public void netWX(String code) throws Exception{
        String body = restTemplate.getForEntity(
                URL + "?appid=" + APPID + "&secret=" + SECRET
                        + "&grant_type=" + GRANT_TYPE + "&js_code=" + code, String.class)
                .getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if(jsonObject.getInteger("errcode") != null) {
            logger.error("微信登录错误信息-->" + jsonObject.toJSONString());
            return ;
        }
        /**
         * 获取的数据
         * access_token	接口调用凭证
         expires_in	access_token接口调用凭证超时时间，单位（秒）
         refresh_token	用户刷新access_token
         openid	授权用户唯一标识
         scope	用户授权的作用域，使用逗号（,）分隔
          unionid	当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
         */
        //更新有效期至30天
        JSONObject refresh_token = refreshToken(jsonObject.getString("refresh_token"));
        //获取用户信息并存储
        JSONObject userInfo = wxUserInfo(refresh_token.getString("access_token"), refresh_token.getString("openid"));
        //查询用户是否存在
        BookUser unionid = bookUserDao.findUserByUnionid(userInfo.getString("unionid"));
        if(unionid == null){ //不存在新增
            BookUser bookUser = new BookUser();
            bookUser.setId(IdGen.uuid());
            bookUser.setLogin(0);
            bookUser.setUnionid(userInfo.getString("unionid"));
            bookUser.setImgUrl("");
            bookUser.setNickname(userInfo.getString("nickname"));
            bookUser.setSex(userInfo.getInteger("sex"));
            /*bookUser.setAccessToken();*/
            bookUserDao.insert(bookUser);
        }




    }

    /**
     * 获取用户详情
     * @param accessToken
     * @param openid
     */
    public JSONObject wxUserInfo(String accessToken,String openid){
        String userInfo = restTemplate.getForEntity(
                USERINFO_URL + "?access_token=" + accessToken
                        + "&openid=" + openid, String.class)
                .getBody();
        JSONObject jsonUser = JSONObject.parseObject(userInfo);
        if(jsonUser.getInteger("errcode") != null) {
            logger.error("微信用户详情错误信息-->" + jsonUser.toJSONString());
            return new JSONObject();
        }
        logger.info("微信用户详情--》"+jsonUser.toJSONString());
        return jsonUser;
        /**
         * 最好保存 unionid
         * openid	普通用户的标识，对当前开发者帐号唯一
         nickname	普通用户昵称
         sex	普通用户性别，1为男性，2为女性
         province	普通用户个人资料填写的省份
         city	普通用户个人资料填写的城市
         country	国家，如中国为CN
         headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
         privilege	用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
         unionid	用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
         */
    }

    /**
     * 检验授权凭证（access_token）是否有效
     * @param accessToken 调用接口凭证
     * @param openid 普通用户标识，对该公众帐号唯一
     */
    public JSONObject uath(String accessToken,String openid){
        String uath = restTemplate.getForEntity(
                UATH_URL + "?access_token=" + accessToken
                        + "&openid=" + openid, String.class)
                .getBody();
        JSONObject jsonUath = JSONObject.parseObject(uath);
        if(jsonUath.getInteger("errcode") != null) {
            logger.error("微信验证错误信息-->" + jsonUath.toJSONString());
            return new JSONObject();
        }
        logger.info("微信验证--》"+jsonUath.toJSONString());
        return jsonUath;
        /**
         * 正确结果
         * {
         "errcode":0,"errmsg":"ok"
         }
         */
    }

    /**
     * 更新登录有效期至30天
     * @param refreshToken 填写通过access_token获取到的refresh_token参数
     * @return
     */
    public JSONObject refreshToken(String refreshToken){
        String ref = restTemplate.getForEntity(
                REF_URL + "?appid=" + APPID + "&grant_type="
                        + "refresh_token" + "&refresh_token=" + refreshToken, String.class)
                .getBody();
        JSONObject jsonRef = JSONObject.parseObject(ref);
        if(jsonRef.getInteger("errcode") != null) {
            logger.error("更新微信登录有效期错误-->" + jsonRef.toJSONString());
            return new JSONObject();
        }
        logger.info("更新微信登录有效期--》"+jsonRef.toJSONString());
        return jsonRef;
        /**
         * access_token	接口调用凭证
         expires_in	access_token接口调用凭证超时时间，单位（秒）
         refresh_token	用户刷新access_token
         openid	授权用户唯一标识
         scope	用户授权的作用域，使用逗号（,）分隔
         */
    }

    /**
     * 如果乱码使用此方法
     * @param accessToken
     * @param openId
     * @return
     */
    public Map<String, Object> getUserInfo(String accessToken, String openId) {
        String url = USERINFO_URL + "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
        String jsonUserStr = null;
        try {
            java.net.URL url1 = new URL(url);

            HttpURLConnection urlConnection = (HttpURLConnection)url1.openConnection();
            // 将返回的输入流转换成字符串
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader in = new BufferedReader(inputStreamReader);

            jsonUserStr =in.readLine().toString();

            System.out.println("jsonUserStr = "+jsonUserStr);

            // 释放资源
            inputStream.close();
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(jsonUserStr==null){
            throw new JSONException("[WX] GET USERINFO FAILED");
        }

        return JSONObject.parseObject(jsonUserStr, Map.class);
    }

    /**
     * qq登录
     * https://open.tencent.com/
     * @param code
     */
    public void netQQ(String code){

    }
}
