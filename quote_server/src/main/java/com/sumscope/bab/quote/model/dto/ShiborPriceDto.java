package com.sumscope.bab.quote.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Shibor价格Dto
 */
public class ShiborPriceDto {

	/**
	 * 期限，例如： O/N，1W，2W等
	 */
	private String period;

	/**
	 * 价格，%
	 */
	private BigDecimal price;

	/**
	 * 日期，忽略时间
	 */
	private Date date;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
