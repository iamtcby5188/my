package com.sumscope.bab.quote.commons.enums;

/**
 * 前端查询条件：剩余期限
 */
public enum WEBDueDateRange implements WEBEnum {

    MONTH("MONTH", "足月", 150, 181),

    LESS_MONTH("LESS_MONTH", "不足月", 1, 150),

    YEAR("YEAR", "足年", 330, 391),

    LESS_YEAR("LESS_YEAR", "不足年", 1, 330);

    private String code;

    private String name;

    private int dayMin;

    private int dayMax;

    WEBDueDateRange(String code, String name, int dayMin, int dayMax) {
        this.code = code;
        this.name = name;
        this.dayMax = dayMax;
        this.dayMin = dayMin;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public int getDayMin() {
        return dayMin;
    }

    public int getDayMax() {
        return dayMax;
    }
}
