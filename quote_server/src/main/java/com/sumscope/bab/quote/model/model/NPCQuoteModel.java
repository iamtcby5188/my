package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.BABTradeType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 全国转贴报价单
 */
public class NPCQuoteModel extends AbstractCountryQuoteModel implements Serializable {

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
