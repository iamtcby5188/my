package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.ShiborParameter;

import java.util.Date;

/**
 * 起始日期Dto
 */
public class DatePeriodDto {

	private Date beginDate;

	private Date endDate;

	private BABBillMedium billMedium;

	private BABQuotePriceType quotePriceType;

	private ShiborParameter shiborParameter;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BABBillMedium getBillMedium() {
		return billMedium;
	}

	public void setBillMedium(BABBillMedium billMedium) {
		this.billMedium = billMedium;
	}

	public BABQuotePriceType getQuotePriceType() {
		return quotePriceType;
	}

	public void setQuotePriceType(BABQuotePriceType quotePriceType) {
		this.quotePriceType = quotePriceType;
	}

	public ShiborParameter getShiborParameter() {
		return shiborParameter;
	}

	public void setShiborParameter(ShiborParameter shiborParameter) {
		this.shiborParameter = shiborParameter;
	}
}
