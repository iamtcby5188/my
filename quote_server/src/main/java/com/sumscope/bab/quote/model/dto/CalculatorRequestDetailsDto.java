package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.Constant;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 票据计算器计算参数明细值Dto。
 */
public class CalculatorRequestDetailsDto {

	/**
	 * 交易日
	 */
	@NotNull
	private Date tradeDate;

	/**
	 * 调整天数，null时表示0
	 */
	private int adjustDays;

	/**
	 * 利率，按%计算，2位小数。
	 */
	@NotNull
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
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
