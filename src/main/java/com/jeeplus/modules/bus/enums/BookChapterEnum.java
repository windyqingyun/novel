/**
 * 
 */
package com.jeeplus.modules.bus.enums;

/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月16日
 * @version V1.0
 *
 */
public enum BookChapterEnum {
	/**
	 * 0: 免费
	 */
	IS_FREE(0, "免费"),
	/**
	 * 1: 收费
	 */
	IS_NOT_FREE(1, "收费"),
	
	
	/**
	 * 1:已购买
	 */
	IS_BUY(1,"已购买"),
	/**
	 * 0:未购买 
	 */
	IS_NOT_BUY(0,"未购买")
	
    ;
	
	
    private Integer code;

    private String desc;

    private BookChapterEnum(Integer code, String desc) {
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
