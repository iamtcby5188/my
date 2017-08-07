package com.sumscope.bab.quote.commons.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 承兑方机构类型，类别分银票及商票两种模式，对应的机构类别不同。
 * 该枚举被用于数据库保持，同时也用于Web前端展示
 */
public enum BABAcceptingCompanyType implements DatabaseEnum,WEBEnum {
	/**
	 * 国股,银票使用
	 */
	GG("GG","国股", BABBillType.BKB),
	/**
	 * 城商,银票使用
	 */
	CS("CS","城商", BABBillType.BKB),
	/**
	 * 农商,银票使用
	 */
	NS("NS","农商", BABBillType.BKB),
	/**
	 * 农信,银票使用
	 */
	NX("NX","农信", BABBillType.BKB),
	/**
	 * 农合,银票使用
	 */
	NH("NH","农合", BABBillType.BKB),
	/**
	 * 村镇,银票使用
	 */
	CZ("CZ","村镇", BABBillType.BKB),
	/**
	 * 外资,银票使用
	 */
	WZ("WZ","外资", BABBillType.BKB),
	/**
	 * 财务公司,银票使用
	 */
	CW("CW","财务公司", BABBillType.BKB),

	/**
	 * 央企， Central Enterprise，商票使用
	 */
	CET("CET","央企",BABBillType.CMB),

	/**
	 * 国企 State-Owned Enterprise，商票使用
	 */
	SOE("SOE","国企",BABBillType.CMB),

	/**
	 * 地方性企业 Local Enterprise，商票使用
	 */
	LET("LET","地方性企业",BABBillType.CMB),

	/**
	 * 其他 Others，商票使用
	 */
	OTH("OTH","其他",BABBillType.CMB);

	private String dbCode;

	private String name;

	private BABBillType billType;

	BABAcceptingCompanyType(String code, String name, BABBillType type) {
		this.dbCode = code;
		this.name = name;
		this.billType = type;
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

	public BABBillType getBillType() {
		return billType;
	}

	/**
	 * @return 商票所属机构类别
	 */
	public static BABAcceptingCompanyType[] listCMBTypes(){
		return listTypesByParam(BABBillType.CMB);

	}

	/**
	 * @return 银票所属机构类型
	 */
	public static BABAcceptingCompanyType[] listBKBTypes(){
		return listTypesByParam(BABBillType.BKB);

	}

	private static BABAcceptingCompanyType[] listTypesByParam(BABBillType billType){
		List<BABAcceptingCompanyType> results = new ArrayList<>();
		for(BABAcceptingCompanyType type: BABAcceptingCompanyType.values()){
			if(type.getBillType() == billType){
				results.add(type);
			}
		}
		return results.toArray(new BABAcceptingCompanyType[results.size()]);
	}
}
