package com.jeeplus.modules.bus.enums;

public enum BookCategoryEnum {
	FINE(0,"fine"),
	POPULARITY(1,"popularity"),
	HOT_SELL(2,"hotsell"),
	NEW_BOOK(3,"newbook")
    ;
    private Integer code;

    private String desc;

    private BookCategoryEnum(Integer code, String desc) {
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
