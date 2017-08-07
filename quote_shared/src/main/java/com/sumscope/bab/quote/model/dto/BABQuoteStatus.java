package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.DatabaseEnum;
import com.sumscope.bab.quote.commons.enums.WEBEnum;

/**
 * 报价状态枚举
 */
public enum BABQuoteStatus implements DatabaseEnum, WEBEnum {
    DFT("DFT", "询价中"),

    DSB("DSB", "已发布"),

    DLD("DLD", "已成交"),

    CAL("CAL", "已撤销"),

    DEL("DEL", "已删除");

    private String dbCode;

    private String name;

    BABQuoteStatus(String code, String name) {
        this.dbCode = code;
        this.name = name;
    }


    @Override
    public String getDbCode() {
        return dbCode;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
