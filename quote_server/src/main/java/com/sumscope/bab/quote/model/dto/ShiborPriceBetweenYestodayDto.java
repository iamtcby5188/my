package com.sumscope.bab.quote.model.dto;


import java.math.BigDecimal;

/**
 * Shibor当前Dto，包括与昨日的价差
 */
public class ShiborPriceBetweenYestodayDto extends ShiborPriceDto {

	/**
	 * 与前一日的价差（BP），可能为负数
	 */
	private BigDecimal margin;

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}
}
