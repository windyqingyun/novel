package com.jeeplus.common.log;

import org.apache.commons.lang3.StringUtils;


public enum ActionEnum {
	CLIK_BOOK(0,"click-book")
    ;

    public static Integer descOf(String desc) {
    	for (ActionEnum Enum : values()) {
            if (StringUtils.equals(Enum.getDesc(), desc)) {
                return Enum.getCode();
            }
        }
        return null;
    }
    
    
    private ActionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    
    private String desc;
    
	public Integer getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	
 
}
