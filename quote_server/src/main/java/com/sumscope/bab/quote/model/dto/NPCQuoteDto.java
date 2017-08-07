package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABTradeType;

import javax.validation.constraints.NotNull;

/**
 * 全国转贴报价单Dto
 */
public class NPCQuoteDto extends AbstractCountryQuoteDto {

	/**
	 * 买断or回购
	 */
	@NotNull
	private BABTradeType tradeType;

	public BABTradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(BABTradeType tradeType) {
		this.tradeType = tradeType;
	}
}
