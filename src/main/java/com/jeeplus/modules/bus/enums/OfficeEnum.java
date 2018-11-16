package com.jeeplus.modules.bus.enums;

import org.apache.commons.lang3.StringUtils;

public enum OfficeEnum {
	CALENDAR_ORIGIN("zhwnl", "200000002001", "中华万年历")
    ;
    private String code;
    private String officeId;
    private String desc;

	private OfficeEnum(String code, String officeId, String desc) {
		this.code = code;
		this.officeId = officeId;
		this.desc = desc;
	}
    
	public String getCode() {
		return code;
	}
	
	public String getOfficeId() {
		return officeId;
	}

	public String getDesc() {
		return desc;
	}
	
    public static String officeIdOf(String code) {
    	for (OfficeEnum Enum : values()) {
            if (StringUtils.equals(Enum.getCode(), code)) {
                return Enum.getOfficeId();
            }
        }
        return null;
    }
	
}
