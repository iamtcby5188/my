package com.sumscope.bab.quote.commons.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 报价价格类型，该类型指明当不进行特定承兑方限制时，报价时的价格类型取值范围。
 * 当为商票时取值范围为有保函、无保函
 * 当为银票时则为其余类型。
 */
public enum BABQuotePriceType implements DatabaseEnum, WEBEnum {

	GG("GG","国股", BABBillType.BKB,null, "gg_price"),

	CS("CS","城商", BABBillType.BKB,null, "cs_price"),

	NS("NS","农商", BABBillType.BKB,null, "ns_price"),

	NX("NX","农信", BABBillType.BKB,null, "nx_price"),

	NH("NH","农合", BABBillType.BKB,null, "nh_price"),

	CZ("CZ","村镇", BABBillType.BKB,null, "cz_price"),

	WZ("WZ","外资", BABBillType.BKB,null, "wz_price"),

	CW("CW","财务公司", BABBillType.BKB,null, "cw_price"),

	YBH("YBH","有保函", BABBillType.CMB,BABQuoteType.SSC, "ybh_price"),

	WBH("WBH","无保函", BABBillType.CMB,BABQuoteType.SSC, "wbh_price");

	private String dbCode;

	private String name;

	private BABBillType babBillType;

	private String tabCode;
	/**
	 * 报价价格对应的报价单类型，null表明适合所有报价类型
	 * 
	 */
	private BABQuoteType quoteType;

	BABQuotePriceType(String code, String name, BABBillType babBillType, BABQuoteType quoteType, String tabCode) {
		this.dbCode = code;
		this.name = name;
		this.babBillType = babBillType;
		this.quoteType = quoteType;
		this.tabCode = tabCode;
	}

	public static BABQuotePriceType[] bkbTypes(){
		return getBabQuotePriceTypes(BABBillType.BKB);
	}

	private static BABQuotePriceType[] getBabQuotePriceTypes(BABBillType type) {
		List<BABQuotePriceType> tmp = new ArrayList<>();
		getEnumsByBillType(tmp,type);
		return tmp.toArray(new BABQuotePriceType[tmp.size()]);
	}

	public static BABQuotePriceType[] cmbTypes(){
		return getBabQuotePriceTypes(BABBillType.CMB);
	}

	private static void getEnumsByBillType(List<BABQuotePriceType> tmp, BABBillType billType) {
		for(BABQuotePriceType type : BABQuotePriceType.values()){
			if(type.getBabBillType() == billType ){
				tmp.add(type);
			}
		}
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

	public BABBillType getBabBillType() {
		return babBillType;
	}

	public BABQuoteType getQuoteType() {
		return quoteType;
	}

	public String getTabCode() {
		return tabCode;
	}

	public void setTabCode(String tabCode) {
		this.tabCode = tabCode;
	}
}
