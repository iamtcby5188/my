package com.sumscope.bab.quote.model.model;

import java.util.Date;

/**
 * 假期模型
 */
public class HolidayInfoModel {

	/**
	 * 日期
	 */
	private Date date;

	/**
	 * 假期类型
	 */
	private String holidayReason;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getHolidayReason() {
		return holidayReason;
	}

	public void setHolidayReason(String holidayReason) {
		this.holidayReason = holidayReason;
	}
}
