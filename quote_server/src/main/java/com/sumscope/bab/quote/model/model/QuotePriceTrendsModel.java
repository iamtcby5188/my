package com.sumscope.bab.quote.model.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.commons.enums.BABTradeType;

/**
 * 报价价格统计信息。
 * 根据报价价格类型和日期进行统计。
 */
public class QuotePriceTrendsModel implements Serializable {

	/**
	 * UUID主键
	 */
	private String id;

	/**
	 * 报价日期
	 */
	private Date quoteDate;

	/**
	 * 价格类型
	 */
	private BABQuotePriceType quotePriceType;

	/**
	 * true： 小纸票（银行） or 小电票（银行）
	 * false： 纸票（银行） or 电票（银行）
	 * 不为商票查询服务
	 */
	private Boolean minorFlag;

	/**
	 * 纸票 or 电票
	 */
	private BABBillMedium billMedium;

	/**
	 * 银行承兑 or 商业机构承兑
	 */
	private BABBillType billType;

    /**
     * 报价类型，用于区分全国直贴、全国转贴
     */
    private BABQuoteType quoteType;

	/**
	 * 最高价格（在合理值范围内的）
	 */
	private BigDecimal priceMax;

	/**
	 * 平均价格（在合理值范围内的）
	 */
	private BigDecimal priceAvg;

	/**
	 * 最低价格（在合理值范围内的）
	 */
	private BigDecimal priceMin;

	/**
	 * 数值生成日期时间
	 */
	private Date createDatetime;

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

    public BABQuoteType getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(BABQuoteType quoteType) {
        this.quoteType = quoteType;
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

    public Boolean getMinorFlag() {
        return minorFlag;
    }

    public void setMinorFlag(Boolean minorFlag) {
        this.minorFlag = minorFlag;
    }

	public BABTradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(BABTradeType tradeType) {
		this.tradeType = tradeType;
	}
}
