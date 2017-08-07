package com.sumscope.bab.quote.commons.enums;

public enum BABTradeType implements DatabaseEnum, WEBEnum {
	/**
	 * 买断 Buy Out
	 * 
	 */
	BOT("BOT","买断"),

	/**
	 * 回购 Buy Back
	 */
	BBK("BBK","回购");

	private String dbCode;

	private String name;

	BABTradeType(String code, String name) {
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
