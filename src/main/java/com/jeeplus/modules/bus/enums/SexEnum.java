/**
 * 
 */
package com.jeeplus.modules.bus.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月11日
 * @version V1.0
 *
 */
public enum SexEnum {
	FEMALE(0,"女"),
	MALE(1,"男")
    ;
    private Integer code;

    private String desc;

    public static Integer descOf(String desc) {
    	for (SexEnum Enum : values()) {
            if (StringUtils.equals(Enum.getDesc(), desc)) {
                return Enum.getCode();
            }
        }
        return null;
    }
    
    
    private SexEnum(Integer code, String desc) {
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
