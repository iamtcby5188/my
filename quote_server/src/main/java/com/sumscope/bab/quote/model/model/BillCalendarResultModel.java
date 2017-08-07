package com.sumscope.bab.quote.model.model;

import java.util.Date;
import java.util.List;

/**
 * Created by fan.bai on 2017/1/23.
 * 票据日历计算结果数据模型
 */
public class BillCalendarResultModel {
    /**
     * 起始月份每日信息，该字段仅在必要时设值
     */
    private List<BillCalendarDayInfoModel> invoiceMonthModels;
    /**
     * 到期月每日信息
     */
    private List<BillCalendarDayInfoModel> maturityMonthModels;
    /**
     * 对应持票开始日的到期日期
     */
    private Date maturityDate;

    /**
     * 持有日 - 单位天
     */
    private int holdingPeriod;

    public List<BillCalendarDayInfoModel> getInvoiceMonthModels() {
        return invoiceMonthModels;
    }

    public void setInvoiceMonthModels(List<BillCalendarDayInfoModel> invoiceMonthModels) {
        this.invoiceMonthModels = invoiceMonthModels;
    }

    public List<BillCalendarDayInfoModel> getMaturityMonthModels() {
        return maturityMonthModels;
    }

    public void setMaturityMonthModels(List<BillCalendarDayInfoModel> maturityMonthModels) {
        this.maturityMonthModels = maturityMonthModels;
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
