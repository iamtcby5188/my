package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.commons.enums.BABTradeType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 价格趋势查询参数Dto
 */
public class QuotePriceTrendsParameterModel implements Serializable {

	/**
	 * true： 小纸票（银行） or 小电票（银行）
	 * false： 纸票（银行） or 电票（银行）
	 * 不为商票查询服务
	 */
	@NotNull
	private boolean minorFlag;

	/**
	 * 纸票 or 电票
	 * 该参数不可为空
	 */
	@NotNull
	private BABBillMedium billMedium;

	/**
	 * 银行承兑 or 商业机构承兑
	 * 该参数不可为空
	 */
	@NotNull
	private BABBillType billType;

    /**
     * 价格类型
	 * 该参数不可为空
     */
	@NotNull
    private BABQuotePriceType quotePriceType;

    /**
     * 报价类型
	 * 该参数不可为空
     */
	@NotNull
    private BABQuoteType quoteType;

	/**
	 * 买断 or 回购，仅用于全国转贴报价
	 */
	private BABTradeType tradeType;

    /**
     * 开始日期
     */
	@NotNull
    private Date startDate;

    /**
     * 结束日期
     */

	@NotNull
    private Date endDate;

	public BABQuotePriceType getQuotePriceType() {
		return quotePriceType;
	}

	public void setQuotePriceType(BABQuotePriceType quotePriceType) {
		this.quotePriceType = quotePriceType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    public BABQuoteType getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(BABQuoteType quoteType) {
        this.quoteType = quoteType;
    }

	public boolean isMinorFlag() {
		return minorFlag;
	}

	public void setMinorFlag(boolean minorFlag) {
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

	public BABTradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(BABTradeType tradeType) {
		this.tradeType = tradeType;
	}
}
