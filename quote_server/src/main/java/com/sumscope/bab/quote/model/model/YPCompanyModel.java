package com.sumscope.bab.quote.model.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 本地数据库缓存的黄页机构信息数据。超过一定期限不使用后将删除以保证本地数据库性能。
 */
public class YPCompanyModel implements Serializable {

	/**
	 * 黄页系统ID
	 */
	@NotNull
	private String id;

	/**
	 * 机构名称
	 * 
	 */
	private String companyName;

	/**
	 * 机构类型，黄页系统中类型
	 */
	private String companyType;

	/**
	 * 最后与黄页系统同步日期
	 */
	private Date lastSynDatetime;

	/**
	 * 过期日期，超过过期日期的数据将从本地数据库删除
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

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public Date getLastSynDatetime() {
		return lastSynDatetime;
	}

	public void setLastSynDatetime(Date lastSynDatetime) {
		this.lastSynDatetime = lastSynDatetime;
	}

	public Date getExpiredDatetime() {
		return expiredDatetime;
	}

	public void setExpiredDatetime(Date expiredDatetime) {
		this.expiredDatetime = expiredDatetime;
	}


}
