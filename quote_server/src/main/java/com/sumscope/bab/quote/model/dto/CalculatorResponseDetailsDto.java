package com.sumscope.bab.quote.model.dto;

import java.math.BigDecimal;

/**
 * 票据计算器计算结果明细值Dto。
 */
public class CalculatorResponseDetailsDto {

	/**
	 * 记息日期
	 */
	private int accrualDays;

	/**
	 * 贴现利息
	 */
	private BigDecimal discountInterest;

	/**
	 * 贴现金额
	 */
	private BigDecimal discountAmount;

	public int getAccrualDays() {
		return accrualDays;
	}

	public void setAccrualDays(int accrualDays) {
		this.accrualDays = accrualDays;
	}

	public BigDecimal getDiscountInterest() {
		return discountInterest;
	}

	public void setDiscountInterest(BigDecimal discountInterest) {
		this.discountInterest = discountInterest;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
}
