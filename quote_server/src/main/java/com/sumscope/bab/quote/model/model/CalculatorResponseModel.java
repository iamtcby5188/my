package com.sumscope.bab.quote.model.model;

import java.math.BigDecimal;

/**
 * 票据计算器计算结果值Dto
 */
public class CalculatorResponseModel {

	/**
	 * 结果明细
	 */
	private CalculatorResponseDetailsModel firstDetailsResponse;

	/**
	 * 结果明细
	 */
	private CalculatorResponseDetailsModel secondDetailsResponse;

	/**
	 * 点差（BP）,最多一位
	 */
	private BigDecimal margin;

	/**
	 * 交易金额
	 */
	private BigDecimal tradeMarginAmount;

	public CalculatorResponseDetailsModel getFirstDetailsResponse() {
		return firstDetailsResponse;
	}

	public void setFirstDetailsResponse(CalculatorResponseDetailsModel firstDetailsResponse) {
		this.firstDetailsResponse = firstDetailsResponse;
	}

	public CalculatorResponseDetailsModel getSecondDetailsResponse() {
		return secondDetailsResponse;
	}

	public void setSecondDetailsResponse(CalculatorResponseDetailsModel secondDetailsResponse) {
		this.secondDetailsResponse = secondDetailsResponse;
	}

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

	public BigDecimal getTradeMarginAmount() {
		return tradeMarginAmount;
	}

	public void setTradeMarginAmount(BigDecimal tradeMarginAmount) {
		this.tradeMarginAmount = tradeMarginAmount;
	}
}
