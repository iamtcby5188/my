package com.sumscope.bab.quote.commons.enums;

/**
 * 用于查询条件的金额枚举
 */
public enum WEBQuoteAmountCondition implements WEBEnum {

    LESS_30("LESS_30", "<30W", 0, 290000),

    A30_50("A30_50", "30-50W", 300000, 500000),

    A50_100("A50_100", "50-100W", 500000, 1000000),

    A100_300("A100_300", "100-300W", 1000000, 3000000),

    A300_500("A300_500", "300-500W", 3000000, 5000000),

    A500_1000("A500_1000", "500-1000W", 5000000, 10000000),

    A1000_5000("A1000_5000", "1000-5000W", 10000000, 50000000),

    LAGE_5000("LAGE_5000", "5000W以上", 50000000, Long.MAX_VALUE);

    private String code;

    private long amountLow;

    private long amountHigh;

    private String name;

    WEBQuoteAmountCondition(String code, String name, long amountLow, long amountHigh) {
        this.code = code;
        this.name = name;
        this.amountHigh = amountHigh;
        this.amountLow = amountLow;
    }

    @Override
    public String getCode() {
        return code;
    }

    public long getAmountLow() {
        return amountLow;
    }

    public long getAmountHigh() {
        return amountHigh;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
