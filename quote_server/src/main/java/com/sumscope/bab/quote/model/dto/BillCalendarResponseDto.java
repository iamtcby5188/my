package com.sumscope.bab.quote.model.dto;

import java.util.Date;
import java.util.List;

/**
 * 日历计算结果Dto
 */
public class BillCalendarResponseDto {

    /**
     * 日历计算开始月份每日信息，列表中每一个dto代表日历中的一个日期。
     */
    private List<BillCalendarDayInfoDto> invoiceMonthDays;

    /**
     * 到期月每日信息，代表开始计算日期到期日所处月份的每日信息。
     */
    private List<BillCalendarDayInfoDto> maturityMonthDays;
    /**
     * 到期日
     */
    private Date maturityDate;

    /**
     * 从计算开始日期到到期日的持有期-单位天
     */
    private int holdingPeriod;

    public List<BillCalendarDayInfoDto> getInvoiceMonthDays() {
        return invoiceMonthDays;
    }

    public void setInvoiceMonthDays(List<BillCalendarDayInfoDto> invoiceMonthDays) {
        this.invoiceMonthDays = invoiceMonthDays;
    }

    public List<BillCalendarDayInfoDto> getMaturityMonthDays() {
        return maturityMonthDays;
    }

    public void setMaturityMonthDays(List<BillCalendarDayInfoDto> maturityMonthDays) {
        this.maturityMonthDays = maturityMonthDays;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public int getHoldingPeriod() {
        return holdingPeriod;
    }

    public void setHoldingPeriod(int holdingPeriod) {
        this.holdingPeriod = holdingPeriod;
    }
}
