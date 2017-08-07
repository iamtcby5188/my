package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.WEBDifference;

/**
 * 直转利差显示dto
 */
public class PriceMarginDto {

	/**
	 * 价格类型，例如：国股，城商，农商等
	 */
	private BABQuotePriceType quotePriceType;

	/**
	 * 直贴价格统计信息
	 */
	private PriceMarginPriceDto straightStickPrices;

	/**
	 * 转贴价格统计信息
	 */
	private PriceMarginPriceDto notesPostedPrices;

	/**
	 * 最小利差（BP）-可能存在负数。计算方式见需求说明
	 */
	private Integer minMargin;

	/**
	 * 最大利差（BP）-可能存在负数。计算方式见需求说明。
	 */
	private Integer maxMargin;

	/**
	 * 与前日比较上涨还是下跌
	 */
	private WEBDifference difference;

	public BABQuotePriceType getQuotePriceType() {
		return quotePriceType;
	}

	public void setQuotePriceType(BABQuotePriceType quotePriceType) {
		this.quotePriceType = quotePriceType;
	}

	public PriceMarginPriceDto getStraightStickPrices() {
		return straightStickPrices;
	}

	public void setStraightStickPrices(PriceMarginPriceDto straightStickPrices) {
		this.straightStickPrices = straightStickPrices;
	}

	public PriceMarginPriceDto getNotesPostedPrices() {
		return notesPostedPrices;
	}

	public void setNotesPostedPrices(PriceMarginPriceDto notesPostedPrices) {
		this.notesPostedPrices = notesPostedPrices;
	}

	public Integer getMinMargin() {
		return minMargin;
	}

	public void setMinMargin(Integer minMargin) {
		this.minMargin = minMargin;
	}

	public Integer getMaxMargin() {
		return maxMargin;
	}

	public void setMaxMargin(Integer maxMargin) {
		this.maxMargin = maxMargin;
	}

	public WEBDifference getDifference() {
		return difference;
	}

	public void setDifference(WEBDifference difference) {
		this.difference = difference;
	}
}
