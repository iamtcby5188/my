package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class QuoteAdditionalInfoModel implements Serializable {
    @NotNull
	@Length(max=32)
	private String quoteId;

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
	 * 商票机构名称，当商票机构ID为空时该字段生效
	 */
	private String acceptingHouseName;
	/**
	 * 承兑行类别
	 */
	private BABAcceptingCompanyType acceptingHouseType;

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

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

	public BABAcceptingCompanyType getAcceptingHouseType() {
		return acceptingHouseType;
	}

	public void setAcceptingHouseType(BABAcceptingCompanyType acceptingHouseType) {
		this.acceptingHouseType = acceptingHouseType;
	}
}
