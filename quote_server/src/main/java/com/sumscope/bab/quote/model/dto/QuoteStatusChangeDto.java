package com.sumscope.bab.quote.model.dto;

import java.util.List;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;

/**
 * 用于修改报价单状态的Dto
 */
public class QuoteStatusChangeDto {

	private List<String> ids;

	private BABQuoteStatus status;

	/**
	 * 报价token
	 */
	private String quoteToken;

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public BABQuoteStatus getStatus() {
		return status;
	}

	public void setStatus(BABQuoteStatus status) {
		this.status = status;
	}

	public String getQuoteToken() {
		return quoteToken;
	}

	public void setQuoteToken(String quoteToken) {
		this.quoteToken = quoteToken;
	}
}
