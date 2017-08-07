package com.sumscope.bab.quote.model.dto;

import java.util.List;

/**
 * 利差分析价格走势Dto
 */
public class PriceMarginAnalysisPriceHistoryDto {


	/**
	 * 直转贴价格走势信息，初始化时默认显示7天数据。
	 */
	private List<QuotePriceTrendsDto> priceTrendsHistory;

	/**
	 * Shibor历史信息，用于图表走势显示。默认显示最近7天。
	 */
	private List<ShiborPriceDto> shiborHistory;

	/**
	 * 承兑行类别 和日期 枚举值
	 */
	private List<FilterDto> filterDto;

	public List<QuotePriceTrendsDto> getPriceTrendsHistory() {
		return priceTrendsHistory;
	}

	public void setPriceTrendsHistory(List<QuotePriceTrendsDto> priceTrendsHistory) {
		this.priceTrendsHistory = priceTrendsHistory;
	}

	public List<ShiborPriceDto> getShiborHistory() {
		return shiborHistory;
	}

	public void setShiborHistory(List<ShiborPriceDto> shiborHistory) {
		this.shiborHistory = shiborHistory;
	}

	public List<FilterDto> getFilterDto() {
		return filterDto;
	}

	public void setFilterDto(List<FilterDto> filterDto) {
		this.filterDto = filterDto;
	}
}
