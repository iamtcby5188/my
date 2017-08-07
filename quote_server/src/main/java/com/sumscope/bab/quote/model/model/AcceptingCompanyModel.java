package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;

import java.io.Serializable;
import java.util.Date;

public class AcceptingCompanyModel implements Serializable {
	/**
	 * 黄页系统主键id
	 */
	private String id;

	/**
	 * 机构类型，黄页系统中类型
	 */
	private BABAcceptingCompanyType companyType;

	/**
	 * IAM机构ID
	 */
	private String iamCompanyID;

	/**
	 * 机构名称
	 */
	private String companyName;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 负责人
	 */
	private String manager;

	/**
	 * 工商注册号
	 */
	private String registrationNumber;

	/**
	 * 机构名称的拼音首字母
	 */
	private String companyNamePY;

	/**
	 * 机构名称的拼音
	 */
	private String companyNamePinYin;

	/**
	 * 最后更新日期
	 */
	private Date lastSynDateTime;

	/**
	 * 过期日期
	 */
	private Date expiredDatetime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BABAcceptingCompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(BABAcceptingCompanyType companyType) {
		this.companyType = companyType;
	}

	public Date getLastSynDateTime() {
		return lastSynDateTime;
	}

	public void setLastSynDateTime(Date lastSynDateTime) {
		this.lastSynDateTime = lastSynDateTime;
	}

	public Date getExpiredDatetime() {
		return expiredDatetime;
	}

	public void setExpiredDatetime(Date expiredDatetime) {
		this.expiredDatetime = expiredDatetime;
	}

	public String getCompanyNamePinYin() {
		return companyNamePinYin;
	}

	public void setCompanyNamePinYin(String companyNamePinYin) {
		this.companyNamePinYin = companyNamePinYin;
	}

	public String getCompanyNamePY() {
		return companyNamePY;
	}

	public void setCompanyNamePY(String companyNamePY) {
		this.companyNamePY = companyNamePY;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIamCompanyID() {
		return iamCompanyID;
	}

	public void setIamCompanyID(String iamCompanyID) {
		this.iamCompanyID = iamCompanyID;
	}
}
