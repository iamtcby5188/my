package com.sumscope.bab.quote.model.dto;


import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;

public class QuoteAdditionalInfoDto {

	/**
	 * 报价机构名称，若用户以非申浦机构方式报价，这机构ID为空，将机构名称直接写入本字段
	 */
	private String quoteCompanyName;

	/**
	 * 联系人名称，若用户以非申浦机构方式报价，这联系人ID为空，将联系人名称直接写入本字段
	 */
	private String contactName;

	/**
	 * 联系人电话，若用户以非申浦机构方式报价，这联系人ID为空，将联系人电话直接写入本字段
	 */
	private String contactTelephone;

	/**
	 * 承兑方全称
	 */
	private String acceptingHouseName;

	/**
	 * 承兑方类别
	 */
	private BABAcceptingCompanyType companyType;

	public String getQuoteCompanyName() {
		return quoteCompanyName;
	}

	public void setQuoteCompanyName(String quoteCompanyName) {
		this.quoteCompanyName = quoteCompanyName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTelephone() {
		return contactTelephone;
	}

	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}

	public String getAcceptingHouseName() {
		return acceptingHouseName;
	}

	public void setAcceptingHouseName(String acceptingHouseName) {
		this.acceptingHouseName = acceptingHouseName;
	}

	public BABAcceptingCompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(BABAcceptingCompanyType companyType) {
		this.companyType = companyType;
	}
}
