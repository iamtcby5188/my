package com.sumscope.bab.quote.model.dto;

import java.util.Date;

/**
 * 日历每日信息Dto
 */
public class BillCalendarDayInfoDto {

    /**
     * 日期
     */
    private Date date;

    /**
     * 开始月每日实际持有期。到期月无需该数据。
     */
    private int holdingPeriod;

    /**
     * 节假日原因，工作日该字段为null
     */
    private String holidayReason;
    /**
     * 周日（1） - 周六（7）
     */
    private int dayOfWeek;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHoldingPeriod() {
        return holdingPeriod;
    }

    public void setHoldingPeriod(int holdingPeriod) {
        this.holdingPeriod = holdingPeriod;
    }

    public String getHolidayReason() {
        return holidayReason;
    }

    public void setHolidayReason(String holidayReason) {
        this.holidayReason = holidayReason;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
