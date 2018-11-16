package com.jeeplus.modules.bus.enums;

public enum CustomerEnum {
	WECHAT_MP(01,"公众平台openid"),
	WECHAT_OP(02,"开放平台openid"),
	TENCENT(03,"腾讯openid"),
    ;
    private Integer code;

    private String desc;
    
    private CustomerEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

 
	public Integer getCode() {
		return code;
	}
 
	public String getDesc() {
		return desc;
	}
}
