package com.sumscope.bab.quote.model.dto;

import java.util.List;
import java.util.Date;

/**
 * 利差分析页面初始化信息Dto
 */
public class PriceMarginAnalysisInitResponseDto {

	/**
	 * 直转利差信息，包括多个价格的直转利差计算结果。
	 */
	private List<PriceMarginDto> priceMargin;

	/**
	 * 当前价格统计信息，仅包括当日最后一次价格统计计算的数据，用于页面当前价格统计信息显示
	 */
	private List<QuotePriceTrendsDto> currentPriceTrendsSSC;

	private List<QuotePriceTrendsDto> currentPriceTrendsNPC;

	/**
	 * 当天Shibor信息，并且与前一日进行了比较。列表根据期限进行排序。
	 */
	private List<ShiborPriceBetweenYestodayDto> currentShiborPrice;

	private PriceMarginAnalysisPriceHistoryDto priceHistory;

	/**
	 * 公开市场投放量历史，默认获取最近7天。用于页面公开市场图表信息展示。
	 */
	private List<NetVolumeOpenMarketDto> netVolumeHistory;

	/**
	 * 价格统计信息是由计时器触发按固定频率进行计算的，本字段提示最新计算时间。
	 * 
	 */
	private Date latestCalculationTime;

	public List<PriceMarginDto> getPriceMargin() {
		return priceMargin;
	}

	public void setPriceMargin(List<PriceMarginDto> priceMargin) {
		this.priceMargin = priceMargin;
	}

	public List<QuotePriceTrendsDto> getCurrentPriceTrendsSSC() {
		return currentPriceTrendsSSC;
	}

	public void setCurrentPriceTrendsSSC(List<QuotePriceTrendsDto> currentPriceTrendsSSC) {
		this.currentPriceTrendsSSC = currentPriceTrendsSSC;
	}

	public List<QuotePriceTrendsDto> getCurrentPriceTrendsNPC() {
		return currentPriceTrendsNPC;
	}

	public void setCurrentPriceTrendsNPC(List<QuotePriceTrendsDto> currentPriceTrendsNPC) {
		this.currentPriceTrendsNPC = currentPriceTrendsNPC;
	}

	public List<ShiborPriceBetweenYestodayDto> getCurrentShiborPrice() {
		return currentShiborPrice;
	}

	public void setCurrentShiborPrice(List<ShiborPriceBetweenYestodayDto> currentShiborPrice) {
		this.currentShiborPrice = currentShiborPrice;
	}

	public PriceMarginAnalysisPriceHistoryDto getPriceHistory() {
		return priceHistory;
	}

	public void setPriceHistory(PriceMarginAnalysisPriceHistoryDto priceHistory) {
		this.priceHistory = priceHistory;
	}

	public List<NetVolumeOpenMarketDto> getNetVolumeHistory() {
		return netVolumeHistory;
	}

	public void setNetVolumeHistory(List<NetVolumeOpenMarketDto> netVolumeHistory) {
		this.netVolumeHistory = netVolumeHistory;
	}

	public Date getLatestCalculationTime() {
		return latestCalculationTime;
	}

	public void setLatestCalculationTime(Date latestCalculationTime) {
		this.latestCalculationTime = latestCalculationTime;
	}
}
