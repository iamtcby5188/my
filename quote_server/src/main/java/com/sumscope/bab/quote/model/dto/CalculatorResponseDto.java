package com.sumscope.bab.quote.model.dto;

import java.math.BigDecimal;

/**
 * 票据计算器计算结果值Dto
 */
public class CalculatorResponseDto {

	/**
	 * 结果明细
	 */
	private CalculatorResponseDetailsDto firstDetailsResponse;

	/**
	 * 结果明细
	 */
	private CalculatorResponseDetailsDto secondDetailsResponse;

	/**
	 * 点差（BP），一位小数
	 */
	private BigDecimal margin;

	/**
	 * 交易金额
	 */
	private BigDecimal tradeMarginAmount;

	public CalculatorResponseDetailsDto getFirstDetailsResponse() {
		return firstDetailsResponse;
	}

	public void setFirstDetailsResponse(CalculatorResponseDetailsDto firstDetailsResponse) {
		this.firstDetailsResponse = firstDetailsResponse;
	}

	public CalculatorResponseDetailsDto getSecondDetailsResponse() {
		return secondDetailsResponse;
	}

	public void setSecondDetailsResponse(CalculatorResponseDetailsDto secondDetailsResponse) {
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
