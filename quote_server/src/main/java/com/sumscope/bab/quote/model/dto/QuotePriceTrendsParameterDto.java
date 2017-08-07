package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.commons.enums.BABTradeType;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 查询历史报价统计功能的参数Dto
 */
public class QuotePriceTrendsParameterDto {

    /**
     * 价格类型
     */
    @NotNull
    private BABQuotePriceType quotePriceType;

	/**
	 * true： 小纸票（银行） or 小电票（银行）
	 * false： 纸票（银行） or 电票（银行）
	 * 不为商票查询服务
	 */
	private boolean minorFlag;

	/**
	 * 纸票 or 电票
	 */
	@NotNull
	private BABBillMedium billMedium;

	/**
	 * 银行承兑 or 商业机构承兑
	 */
	@NotNull
	private BABBillType billType;

    /**
     * 报价类型
     */
    @NotNull
    private BABQuoteType quoteType;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

	/**
	 * 买入方式，买断or回购，该字段可空，仅用于NPC全国转贴查询
	 */
	private BABTradeType tradeType;

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
