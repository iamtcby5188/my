package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.model.dto.AbstractQuoteDto;

import java.util.List;

/**
 * Excel文件解析报价单结果Dto
 * 
 */
public class ExcelParserResultDto {

	/**
	 * 成功解析的报价单
	 */
	private List<AbstractQuoteDto> quotes;

	/**
	 * 错误信息列表
	 * 
	 */
	private List<String> invalids;

	/**
	 * 用于标记是银票和是商票
	 */
	private BABBillType billType;

	public List<AbstractQuoteDto> getQuotes() {
		return quotes;
	}

	public void setQuotes(List<AbstractQuoteDto> quotes) {
		this.quotes = quotes;
	}

	public List<String> getInvalids() {
		return invalids;
	}

	public void setInvalids(List<String> invalids) {
		this.invalids = invalids;
	}

	public BABBillType getBillType() {
		return billType;
	}

	public void setBillType(BABBillType billType) {
		this.billType = billType;
	}
}
