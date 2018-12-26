package com.jeeplus.modules.bus.enums;

public enum BookCategoryEnum {

	//，,
	FINE(0,"fine"),//精品推荐
	POPULARITY(1,"popularity"),//人气榜
	HOT_SEARCH(2,"hotSearch"),//热搜榜
	NEW_BOOK(3,"newbook"),//新书榜
	SOARING(4,"soaring"),//飙升榜
	COLLECTION(5,"collection"),//收藏榜
	LATEST_BOOK(6,"LatestBook"),//最新入库
	HOT_SELL(2,"hotsell"),
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
