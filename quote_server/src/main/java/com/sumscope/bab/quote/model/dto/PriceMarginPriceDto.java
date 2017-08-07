package com.sumscope.bab.quote.model.dto;

import java.math.BigDecimal;

/**
 * 直转利差-价格统计信息dto
 */
public class PriceMarginPriceDto {

	/**
	 * 最高价
	 */
	private BigDecimal maxPrice;

	/**
	 * 最低价
	 */
	private BigDecimal minPrice;

	/**
	 * 平均价
	 */
	private BigDecimal avgPrice;

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}
}
