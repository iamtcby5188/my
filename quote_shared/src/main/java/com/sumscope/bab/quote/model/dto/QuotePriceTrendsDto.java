package com.sumscope.bab.quote.model.dto;


import com.sumscope.bab.quote.commons.enums.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  报价价格统计信息 
 *  按价格类型和日期区分 
 * 
 */
public class QuotePriceTrendsDto {

	/**
	 *  UUID主键 
	 * 
	 */
	private String id;

	/**
	 *  报价日期 
	 * 
	 */
	private Date quoteDate;

	/**
	 *  价格类型 
	 * 
	 */
	private BABQuotePriceType quotePriceType;

	/**
	 * 小金额票据标记
	 */
	private Boolean minorFlag;

	/**
	 * 票据介质
	 */
	private BABBillMedium billMedium;

	/**
	 * 票据类型
	 */
	private BABBillType billType;

	/**
	 *  报价类型 
	 * 
	 */
	private BABQuoteType quoteType;

	/**
	 *  最高价格（在合理值范围内的） 
	 * 
	 */
	private BigDecimal priceMax;

	/**
	 *  平均价格（在合理值范围内的） 
	 * 
	 */
	private BigDecimal priceAvg;

	/**
	 *  最低价格（在合理值范围内的） 
	 * 
	 */
	private BigDecimal priceMin;

	/**
	 *  数值生成日期时间 
	 * 
	 */
	private Date createDatetime;

	/**
	 * 此字段只在价差分析中用 直转贴价格走势图 标记是直贴价格 转贴价格 SHIBOR IBO001 R001曲线
	 */
	private String signCode;

	/**
	 * 购买方式：买断 or 回购，仅用于NPC报价单
	 */
	private BABTradeType tradeType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}

	public BABQuotePriceType getQuotePriceType() {
		return quotePriceType;
	}

	public void setQuotePriceType(BABQuotePriceType quotePriceType) {
		this.quotePriceType = quotePriceType;
	}

	public Boolean getMinorFlag() {
		return minorFlag;
	}

	public void setMinorFlag(Boolean minorFlag) {
		this.minorFlag = minorFlag;
	}

	public BABBillMedium getBillMedium() {
		return billMedium;
	}

	public void setBillMedium(BABBillMedium billMedium) {
		this.billMedium = billMedium;
	}

	public BABBillType getBillType() {
		return billType;
	}

	public void setBillType(BABBillType billType) {
		this.billType = billType;
	}

	public BABQuoteType getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(BABQuoteType quoteType) {
		this.quoteType = quoteType;
	}

	public BigDecimal getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(BigDecimal priceMax) {
		this.priceMax = priceMax;
	}

	public BigDecimal getPriceAvg() {
		return priceAvg;
	}

	public void setPriceAvg(BigDecimal priceAvg) {
		this.priceAvg = priceAvg;
	}

	public BigDecimal getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(BigDecimal priceMin) {
		this.priceMin = priceMin;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getSignCode() {
		return signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}

	public BABTradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(BABTradeType tradeType) {
		this.tradeType = tradeType;
	}
}
