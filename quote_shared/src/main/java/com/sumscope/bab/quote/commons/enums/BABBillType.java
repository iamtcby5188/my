package com.sumscope.bab.quote.commons.enums;

/**
 * 票据类型枚举
 */
public enum BABBillType implements DatabaseEnum {

	BKB("BKB","银行票据"),

	CMB("CMB","商业票据");

	private String dbCode;

	private String name;

	BABBillType(String code, String name) {
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
