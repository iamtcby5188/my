package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.*;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import com.sumscope.bab.quote.commons.enums.OrderSeq;
import com.sumscope.bab.quote.commons.enums.WEBQuoteAmountCondition;
import com.sumscope.bab.quote.commons.model.DueDateWrapper;

import java.util.Date;
import java.util.List;

public class QueryQuoteParameterDto {

	private BABBillType billType;

	private Direction direction;

	private BABBillMedium billMedium;

    private List<BABQuotePriceType> quotePriceTypeList;

	private List<BABAcceptingCompanyType> companyTypes;

	private List<WEBQuoteAmountCondition> amountList;

	private List<DueDateWrapper> dueDateWrapperList;

	private List<String> provinceCodes;

    private BABTradeType tradeType;

    private boolean minor;

	private String orderByPriceType;

	private OrderSeq oderSeq;

	private int pageSize;

	private int pageNumber;

	private List<BABQuoteStatus> quoteStatusList;

    private String companyId;

    private String memo;

    private Date createTimeBegin;

    private Date createTimeEnd;

	private String userId;

	private Date lastUpdateTimeBegin;

    private Date lastUpdateTimeEnd;

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

	public List<WEBQuoteAmountCondition> getAmountList() {
		return amountList;
	}

	public void setAmountList(List<WEBQuoteAmountCondition> amountList) {
		this.amountList = amountList;
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

	public OrderSeq getOderSeq() {
		return oderSeq;
	}

	public void setOderSeq(OrderSeq oderSeq) {
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

    public boolean isMinor() {
        return minor;
    }

    public void setMinor(boolean minor) {
        this.minor = minor;
    }

    public List<BABQuotePriceType> getQuotePriceTypeList() {
        return quotePriceTypeList;
    }

    public void setQuotePriceTypeList(List<BABQuotePriceType> quotePriceTypeList) {
        this.quotePriceTypeList = quotePriceTypeList;
    }

	public List<DueDateWrapper> getDueDateWrapperList() {
		return dueDateWrapperList;
	}

	public void setDueDateWrapperList(List<DueDateWrapper> dueDateWrapperList) {
		this.dueDateWrapperList = dueDateWrapperList;
	}

	public List<BABQuoteStatus> getQuoteStatusList() {
		return quoteStatusList;
	}

	public void setQuoteStatusList(List<BABQuoteStatus> quoteStatusList) {
		this.quoteStatusList = quoteStatusList;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
}
