package com.jeeplus.modules.bus.entity;

public class BookUser {
    /**
     * 小说用户
     */
    private String id;

    /**
     * 微信登录0，QQ登录1
     */
    private Integer login;

    /**
     * 微信用户统一标识符
     */
    private String unionid;

    /**
     * 用户头像可以为获取的微信或qq头像
     */
    private String imgUrl;

    /**
     * 用户继承，可为微信或qq
     */
    private String nickname;

    /**
     * 性别1：男，2女
     */
    private Integer sex;

    /**
     * 接口调用凭证，微信qq共用
     */
    private String accessToken;

    /**
     * 授权用户唯一标识，微信qq共用
     */
    private String openid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getLogin() {
        return login;
    }

    public void setLogin(Integer login) {
        this.login = login;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? null : unionid.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }
}