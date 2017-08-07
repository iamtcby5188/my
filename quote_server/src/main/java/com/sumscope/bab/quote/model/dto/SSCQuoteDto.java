package com.sumscope.bab.quote.model.dto;

import javax.validation.Valid;

/**
 * 全国直贴报价单Dto
 */
public class SSCQuoteDto extends AbstractCountryQuoteDto {

	/**
	 * 报价区域，新增时如果该类不为null，则code必须存在并检查合法性
	 */
	@Valid
	private QuoteProvinceDto quoteProvinces;

	/**
	 * 有保函价格
	 */
	private String ybhPrice;

	/**
	 * 无保函价格
	 */
	private String wbhPrice;

	public String getYbhPrice() {
		return ybhPrice;
	}

	public void setYbhPrice(String ybhPrice) {
		this.ybhPrice = ybhPrice;
	}

	public String getWbhPrice() {
		return wbhPrice;
	}

	public void setWbhPrice(String wbhPrice) {
		this.wbhPrice = wbhPrice;
	}

	public QuoteProvinceDto getQuoteProvinces() {
		return quoteProvinces;
	}

	public void setQuoteProvinces(QuoteProvinceDto quoteProvinces) {
		this.quoteProvinces = quoteProvinces;
	}
}
