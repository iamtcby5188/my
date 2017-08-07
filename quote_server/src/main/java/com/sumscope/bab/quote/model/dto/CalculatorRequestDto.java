package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.Constant;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据计算器计算参数值Dto。
 */
public class CalculatorRequestDto {

	/**
	 * 票面金额，按元计价，支持2位小数。
	 */
	@NotNull
	@DecimalMax(value = Constant.AMOUNT_MAX_VALUE)
	@DecimalMin(value = Constant.AMOUNT_MIN_VALUE)
	private BigDecimal amount;

	/**
	 * 到期日，必填项目。
	 */
	@NotNull
	private Date dueDate;

	/**
	 * 第一部分计算请求明细
	 */
	@NotNull
	@Valid
	private CalculatorRequestDetailsDto firstDetailsRequest;
	/**
	 * 第二部分计算请求明细
	 */
	@Valid
	private CalculatorRequestDetailsDto secondDetailsRequest;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public CalculatorRequestDetailsDto getFirstDetailsRequest() {
		return firstDetailsRequest;
	}

	public void setFirstDetailsRequest(CalculatorRequestDetailsDto firstDetailsRequest) {
		this.firstDetailsRequest = firstDetailsRequest;
	}

	public CalculatorRequestDetailsDto getSecondDetailsRequest() {
		return secondDetailsRequest;
	}

	public void setSecondDetailsRequest(CalculatorRequestDetailsDto secondDetailsRequest) {
		this.secondDetailsRequest = secondDetailsRequest;
	}
}
