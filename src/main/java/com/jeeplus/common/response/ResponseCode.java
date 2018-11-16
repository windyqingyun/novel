package com.jeeplus.common.response;

/**
 * 
 * @Description: 统一返回码
 * @author lzp
 * @date 2018年5月11日
 * @version V1.0
 *
 */
public enum ResponseCode {
	SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"),
    ILLEGAL_ORIGIN(3,"ILLEGAL_ORIGIN"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    SYS_ERROR(-1,"SYS_ERROR");

    private final int code;
    private final String desc;


    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }
}
