package com.sumscope.bab.quote.model.dto;

import javax.validation.constraints.NotNull;

public class IAMCompanyReferenceDto {

	/**
	 * IAM系统ID
	 */
	@NotNull
	private String id;

	/**
	 * 机构名称
	 */
	private String name;

	/**
	 * 省份、直辖市名称
	 */
	private String province;

	/**
	 * 公司全称
	 */
	private String fullName;
	/**
	 * 公司性质
	 */
	private String bankNature;

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBankNature() {
		return bankNature;
	}

	public void setBankNature(String bankNature) {
		this.bankNature = bankNature;
	}
}
