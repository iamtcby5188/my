package com.sumscope.bab.quote.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据计算器计算参数值Dto。
 */
public class CalculatorRequestModel {

	/**
	 * 票面金额，按元计价，支持2位小数。
	 */
	private BigDecimal amount;

	/**
	 * 到期日，必填项目。
	 */
	private Date dueDate;

	/**
	 * 第一部分计算请求明细
	 */
	private CalculatorRequestDetailsModel firstDetailsRequest;
	/**
	 * 第二部分计算请求明细
	 */
	private CalculatorRequestDetailsModel secondDetailsRequest;

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

	public CalculatorRequestDetailsModel getFirstDetailsRequest() {
		return firstDetailsRequest;
	}

	public void setFirstDetailsRequest(CalculatorRequestDetailsModel firstDetailsRequest) {
		this.firstDetailsRequest = firstDetailsRequest;
	}

	public CalculatorRequestDetailsModel getSecondDetailsRequest() {
		return secondDetailsRequest;
	}

	public void setSecondDetailsRequest(CalculatorRequestDetailsModel secondDetailsRequest) {
		this.secondDetailsRequest = secondDetailsRequest;
	}
}
