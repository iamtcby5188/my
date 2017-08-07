package com.sumscope.bab.quote.commons.enums;

/**
 * 代报价显示模式，既在报价单中报价人是操作人还是代报价用户。
 */
public enum BABJoiningDisplayMode implements DatabaseEnum {

	/**
	 * Contactor 报价单显示报价人联系方式
	 */
	CTR("CTR","报价人模式"),

	/**
	 * Operator 报价单显示操作人联系方式
	 */
	OPT("OPT","操作人模式");

	private String dbCode;

	private String name;

	BABJoiningDisplayMode(String code, String name) {
		this.dbCode = code;
		this.name = name;
	}


	@Override
	public String getDbCode() {
		return dbCode;
	}

}
