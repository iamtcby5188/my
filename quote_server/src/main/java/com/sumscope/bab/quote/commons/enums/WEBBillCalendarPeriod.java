package com.sumscope.bab.quote.commons.enums;

/**
 * Created by fan.bai on 2017/1/23.
 * 用于票据日历的持有期枚举
 */
public enum WEBBillCalendarPeriod implements WEBEnum {
    HALF_YEAR ("halfYear","半年"),
    YEAR("year","一年");
    private String code;
    private String displayName;

    WEBBillCalendarPeriod(String code, String displayName){
        this.code = code;
        this.displayName = displayName;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
