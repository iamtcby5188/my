package com.sumscope.bab.quote.commons.enums;

/**
 * 报价模式枚举
 * 直贴或转贴
 * 
 */
public enum BABQuoteMode implements DatabaseEnum {

	STRAIGHT_STICK("SS","直贴"),

	NOTES_POSTED("NP","转贴");

	private String dbCode;

	private String name;

	BABQuoteMode(String code, String name) {
		this.dbCode = code;
		this.name = name;
	}


	@Override
	public String getDbCode() {
		return dbCode;
	}

	public String getName() {
		return name;
	}
}
