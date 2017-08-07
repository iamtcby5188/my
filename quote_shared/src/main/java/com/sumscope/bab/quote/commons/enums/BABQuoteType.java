package com.sumscope.bab.quote.commons.enums;

/**
 * 报价类型枚举
 * 
 */
public enum BABQuoteType implements DatabaseEnum {

	/**
	 * Straight stick retail, 直贴零售报价，对应直贴交易大厅；
	 */
	SSR("SSR","直贴报价",BABQuoteMode.STRAIGHT_STICK),

	/**
	 *  Straight stick contry 全国直贴报价，对应全国直贴价格大厅；
	 */
	SSC("SSC","全国直贴",BABQuoteMode.STRAIGHT_STICK),

	/**
	 * Notes posted contry 全国转贴报价，对应全国转贴价格大厅。
	 */
	NPC("NPC","全国转贴",BABQuoteMode.NOTES_POSTED);

	private String dbCode;

	private String name;

	private BABQuoteMode babQuoteMode;

	BABQuoteType(String code, String name, BABQuoteMode babQuoteMode) {
		this.dbCode = code;
		this.name = name;
		this.babQuoteMode = babQuoteMode;
	}


	@Override
	public String getDbCode() {
		return dbCode;
	}


}
