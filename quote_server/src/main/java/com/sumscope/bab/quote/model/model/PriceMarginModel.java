package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;

/**
 * 直转利差显示dto
 */
public class PriceMarginModel {

	/**
	 * 价格类型，例如：国股，城商，农商等
	 */
	private BABQuotePriceType quotePriceType;

	/**
	 * 直贴价格统计信息
	 */
	private PriceMarginPriceModel straightStickPrices;

	/**
	 * 转贴价格统计信息
	 */
	private PriceMarginPriceModel notesPostedPrices;

	/**
	 * 最小利差（BP）-可能存在负数。计算方式见需求说明
	 */
	private Integer minMargin;

	/**
	 * 最大利差（BP）-可能存在负数。计算方式见需求说明。
	 */
	private Integer maxMargin;


	public BABQuotePriceType getQuotePriceType() {
		return quotePriceType;
	}

	public void setQuotePriceType(BABQuotePriceType quotePriceType) {
		this.quotePriceType = quotePriceType;
	}

	public PriceMarginPriceModel getStraightStickPrices() {
		return straightStickPrices;
	}

	public void setStraightStickPrices(PriceMarginPriceModel straightStickPrices) {
		this.straightStickPrices = straightStickPrices;
	}

	public PriceMarginPriceModel getNotesPostedPrices() {
		return notesPostedPrices;
	}

	public void setNotesPostedPrices(PriceMarginPriceModel notesPostedPrices) {
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
}
