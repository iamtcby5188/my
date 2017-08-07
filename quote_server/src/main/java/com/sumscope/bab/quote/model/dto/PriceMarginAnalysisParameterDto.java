package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;

/**
 * 利差分析页面查询参数Dto
 */
public class PriceMarginAnalysisParameterDto {

	/**
	 * 票据介质
	 */
	private BABBillMedium billMedium;

	public BABBillMedium getBillMedium() {
		return billMedium;
	}

	public void setBillMedium(BABBillMedium billMedium) {
		this.billMedium = billMedium;
	}
}
