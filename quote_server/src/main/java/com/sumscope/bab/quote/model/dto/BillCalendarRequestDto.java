package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.WEBBillCalendarPeriod;

import java.util.Date;

/**
 * 计算请求参数Dto
 */
public class BillCalendarRequestDto {

    /**
     * 日历计算开始日期
     */
    private Date date;

    /**
     * 持有期：仅可选择半年或一年.
     */
    private WEBBillCalendarPeriod period;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public WEBBillCalendarPeriod getPeriod() {
        return period;
    }

    public void setPeriod(WEBBillCalendarPeriod period) {
        this.period = period;
    }
}
