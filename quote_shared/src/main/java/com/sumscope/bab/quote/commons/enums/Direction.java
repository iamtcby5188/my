package com.sumscope.bab.quote.commons.enums;

/**
 * 报价方向枚举
 * 
 */
public enum Direction implements DatabaseEnum, WEBEnum {

	IN("IN","买入"),

	OUT("OUT","卖出"),

	UDF("UDF","未定义");

	private String dbCode;

	private String name;

	Direction(String code, String name) {
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
