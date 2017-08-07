package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.*;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import com.sumscope.bab.quote.commons.model.AmountWrapper;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.commons.model.DueDateWrapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QueryQuotesParameterModel implements Serializable {

	private BABBillType billType;

	private Direction direction;

	private BABBillMedium billMedium;

    private List<BABQuotePriceType> quotePriceTypeList;

    private List<BABAcceptingCompanyType> companyTypes;

	private List<AmountWrapper> amountList;

	private List<DueDateWrapper> dueDateWrapperList;

	private List<String> provinceCodes;

	private String orderByPriceType;

	private String oderSeq;

	private int pageSize;

	private int pageNumber;

    /**
     * 分页查询标志位，为true时按照pageSize,pageNumber字段进行分页查询
     * 为false时不按分页方式查询
     */
    private boolean paging = false;

    /**
     * 在该字段时刻仍然有效的报价单，逻辑为：effective_datetime <= effectiveQuotesDate && effectiveQuotesDate < expired_datetime
     * 该字段为空时不做限制
     */
    private Date effectiveQuotesDate;

    /**
     * 在该字段时刻过期的报价单，逻辑为 effectiveQuotesDate >= expired_datetime
     * 该字段为空时不做限制
     */
    private Date expiredQuotesDate;

    /**
     * 报价单状态查询列表
     * 为空时不做限制
     */
    private List<BABQuoteStatus> quoteStatusList;

    private String companyId;
    private String companyName;
    private String memo;


    private BABTradeType tradeType;

    private Boolean minor;

    private String userId;

    private Date createTimeBegin;

    private Date createTimeEnd;

    private Date lastUpdateTimeBegin;

    private Date lastUpdateTimeEnd;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryQuotesParameterModel)) return false;

        QueryQuotesParameterModel that = (QueryQuotesParameterModel) o;

        if (getPageSize() != that.getPageSize()) return false;
        if (getPageNumber() != that.getPageNumber()) return false;
        if (isPaging() != that.isPaging()) return false;
        if (getBillType() != that.getBillType()) return false;
        if (getDirection() != that.getDirection()) return false;
        if (getBillMedium() != that.getBillMedium()) return false;
        if (getQuotePriceTypeList() != null ? !getQuotePriceTypeList().equals(that.getQuotePriceTypeList()) : that.getQuotePriceTypeList() != null)
            return false;
        if (getCompanyTypes() != null ? !getCompanyTypes().equals(that.getCompanyTypes()) : that.getCompanyTypes() != null)
            return false;
        if (getAmountList() != null ? !getAmountList().equals(that.getAmountList()) : that.getAmountList() != null)
            return false;
        if (getDueDateWrapperList() != null ? !getDueDateWrapperList().equals(that.getDueDateWrapperList()) : that.getDueDateWrapperList() != null)
            return false;
        if (getProvinceCodes() != null ? !getProvinceCodes().equals(that.getProvinceCodes()) : that.getProvinceCodes() != null)
            return false;
        if (getOrderByPriceType() != null ? !getOrderByPriceType().equals(that.getOrderByPriceType()) : that.getOrderByPriceType() != null)
            return false;
        if (getOderSeq() != null ? !getOderSeq().equals(that.getOderSeq()) : that.getOderSeq() != null) return false;
        if (getEffectiveQuotesDate() != null ? !getEffectiveQuotesDate().equals(that.getEffectiveQuotesDate()) : that.getEffectiveQuotesDate() != null)
            return false;
        if (getExpiredQuotesDate() != null ? !getExpiredQuotesDate().equals(that.getExpiredQuotesDate()) : that.getExpiredQuotesDate() != null)
            return false;
        if (getQuoteStatusList() != null ? !getQuoteStatusList().equals(that.getQuoteStatusList()) : that.getQuoteStatusList() != null)
            return false;
        if (getCompanyId() != null ? !getCompanyId().equals(that.getCompanyId()) : that.getCompanyId() != null)
            return false;
        if (getMemo() != null ? !getMemo().equals(that.getMemo()) : that.getMemo() != null) return false;
        if (getTradeType() != that.getTradeType()) return false;
        if (getMinor() != null ? !getMinor().equals(that.getMinor()) : that.getMinor() != null) return false;
        if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) return false;
        if (getCreateTimeBegin() != null ? !getCreateTimeBegin().equals(that.getCreateTimeBegin()) : that.getCreateTimeBegin() != null)
            return false;
        if (getCreateTimeEnd() != null ? !getCreateTimeEnd().equals(that.getCreateTimeEnd()) : that.getCreateTimeEnd() != null)
            return false;
        if (getLastUpdateTimeBegin() != null ? !getLastUpdateTimeBegin().equals(that.getLastUpdateTimeBegin()) : that.getLastUpdateTimeBegin() != null)
            return false;
        return getLastUpdateTimeEnd() != null ? getLastUpdateTimeEnd().equals(that.getLastUpdateTimeEnd()) : that.getLastUpdateTimeEnd() == null;

    }

    @Override
    public int hashCode() {
        int result = getBillType() != null ? getBillType().hashCode() : 0;
        result = 31 * result + (getDirection() != null ? getDirection().hashCode() : 0);
        result = 31 * result + (getBillMedium() != null ? getBillMedium().hashCode() : 0);
        result = 31 * result + (getQuotePriceTypeList() != null ? getQuotePriceTypeList().hashCode() : 0);
        result = 31 * result + (getCompanyTypes() != null ? getCompanyTypes().hashCode() : 0);
        result = 31 * result + (getAmountList() != null ? getAmountList().hashCode() : 0);
        result = 31 * result + (getDueDateWrapperList() != null ? getDueDateWrapperList().hashCode() : 0);
        result = 31 * result + (getProvinceCodes() != null ? getProvinceCodes().hashCode() : 0);
        result = 31 * result + (getOrderByPriceType() != null ? getOrderByPriceType().hashCode() : 0);
        result = 31 * result + (getOderSeq() != null ? getOderSeq().hashCode() : 0);
        result = 31 * result + getPageSize();
        result = 31 * result + getPageNumber();
        result = 31 * result + (isPaging() ? 1 : 0);
        result = 31 * result + (getEffectiveQuotesDate() != null ? getEffectiveQuotesDate().hashCode() : 0);
        result = 31 * result + (getExpiredQuotesDate() != null ? getExpiredQuotesDate().hashCode() : 0);
        result = 31 * result + (getQuoteStatusList() != null ? getQuoteStatusList().hashCode() : 0);
        result = 31 * result + (getCompanyId() != null ? getCompanyId().hashCode() : 0);
        result = 31 * result + (getMemo() != null ? getMemo().hashCode() : 0);
        result = 31 * result + (getTradeType() != null ? getTradeType().hashCode() : 0);
        result = 31 * result + (getMinor() != null ? getMinor().hashCode() : 0);
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        result = 31 * result + (getCreateTimeBegin() != null ? getCreateTimeBegin().hashCode() : 0);
        result = 31 * result + (getCreateTimeEnd() != null ? getCreateTimeEnd().hashCode() : 0);
        result = 31 * result + (getLastUpdateTimeBegin() != null ? getLastUpdateTimeBegin().hashCode() : 0);
        result = 31 * result + (getLastUpdateTimeEnd() != null ? getLastUpdateTimeEnd().hashCode() : 0);
        return result;
    }

    public int getStartNum() {
        return pageSize * pageNumber;
    }

    public BABBillType getBillType() {
		return billType;
	}

	public void setBillType(BABBillType billType) {
		this.billType = billType;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public List<String> getProvinceCodes() {
		return provinceCodes;
	}

	public void setProvinceCodes(List<String> provinceCodes) {
		this.provinceCodes = provinceCodes;
	}

	public String getOrderByPriceType() {
		return orderByPriceType;
	}

	public void setOrderByPriceType(String orderByPriceType) {
		this.orderByPriceType = orderByPriceType;
	}

	public String getOderSeq() {
		return oderSeq;
	}

	public void setOderSeq(String oderSeq) {
		this.oderSeq = oderSeq;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public Date getEffectiveQuotesDate() {
        return effectiveQuotesDate;
    }

    public void setEffectiveQuotesDate(Date effectiveQuotesDate) {
        this.effectiveQuotesDate = effectiveQuotesDate;
    }

    public Date getExpiredQuotesDate() {
        return expiredQuotesDate;
    }

    public void setExpiredQuotesDate(Date expiredQuotesDate) {
        this.expiredQuotesDate = expiredQuotesDate;
    }

    public List<BABQuoteStatus> getQuoteStatusList() {
        return quoteStatusList;
    }

    public void setQuoteStatusList(List<BABQuoteStatus> quoteStatusList) {
        this.quoteStatusList = quoteStatusList;
    }

	public BABBillMedium getBillMedium() {
		return billMedium;
	}

	public void setBillMedium(BABBillMedium billMedium) {
		this.billMedium = billMedium;
	}

    public BABTradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(BABTradeType tradeType) {
        this.tradeType = tradeType;
    }

    public Boolean getMinor() {
        return minor;
    }

    public void setMinor(Boolean minor) {
        this.minor = minor;
    }

    public List<BABQuotePriceType> getQuotePriceTypeList() {
        return quotePriceTypeList;
    }

    public void setQuotePriceTypeList(List<BABQuotePriceType> quotePriceTypeList) {
        this.quotePriceTypeList = quotePriceTypeList;
    }

    public List<AmountWrapper> getAmountList() {
        return amountList;
    }

    public void setAmountList(List<AmountWrapper> amountList) {
        this.amountList = amountList;
    }

    public List<DueDateWrapper> getDueDateWrapperList() {
        return dueDateWrapperList;
    }

    public void setDueDateWrapperList(List<DueDateWrapper> dueDateWrapperList) {
        this.dueDateWrapperList = dueDateWrapperList;
    }

    public List<BABAcceptingCompanyType> getCompanyTypes() {
        return companyTypes;
    }

    public void setCompanyTypes(List<BABAcceptingCompanyType> companyTypes) {
        this.companyTypes = companyTypes;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }



    public Date getLastUpdateTimeBegin() {
        return lastUpdateTimeBegin;
    }

    public void setLastUpdateTimeBegin(Date lastUpdateTimeBegin) {
        this.lastUpdateTimeBegin = lastUpdateTimeBegin;
    }

    public Date getLastUpdateTimeEnd() {
        return lastUpdateTimeEnd;
    }

    public void setLastUpdateTimeEnd(Date lastUpdateTimeEnd) {
        this.lastUpdateTimeEnd = lastUpdateTimeEnd;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
