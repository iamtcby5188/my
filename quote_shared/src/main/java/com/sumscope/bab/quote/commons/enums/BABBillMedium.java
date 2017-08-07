package com.sumscope.bab.quote.commons.enums;

/**
 * 票据介质枚举
 * 
 */
public enum BABBillMedium implements DatabaseEnum, WEBEnum {

	PAP("PAP","纸票"),

	ELE("ELE","电票");

	private String dbCode;

	private String name;

	BABBillMedium(String code, String name) {
		this.dbCode = code;
		this.name = name;
	}


	@Override
	public String getDbCode() {
		return dbCode;
	}


	@Override
	public String getCode() {
		return dbCode;
	}

	@Override
	public String getDisplayName() {
		return name;
	}
}
