package com.sumscope.bab.quote.commons.enums;

public enum OrderSeq implements WEBEnum {

	DESC("DESC","降序"),

	ASC("ASC","升序");

	private String dbCode;
	private String name;

	OrderSeq(String code ,String name) {
		this.dbCode = code;
		this.name = name;
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
