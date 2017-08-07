package com.sumscope.bab.quote.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据计算器计算参数明细值Dto。
 */
public class CalculatorRequestDetailsModel {

	/**
	 * 交易日
	 */
	private Date tradeDate;

	/**
	 * 调整天数，null时表示0
	 */
	private int adjustDays;

	/**
	 * 利率，按%计算，2位小数。
	 */
	private BigDecimal price;

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public int getAdjustDays() {
		return adjustDays;
	}

	public void setAdjustDays(int adjustDays) {
		this.adjustDays = adjustDays;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
