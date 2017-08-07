package com.sumscope.bab.quote.model.model;

import java.util.Date;

/**
 * 日历数据模型
 */
public class BillCalendarDayInfoModel {

    /**
     * 日期
     */
    private Date date;

    /**
     * 持有期
     */
    private int holdingPeriod;

    /**
     * 假期类型
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
