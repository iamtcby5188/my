package com.sumscope.bab.quote.commons.enums;

/**
 * Created by Administrator on 2017/3/23.
 * 排序字段
 */
public enum  BABOrderByType implements WEBEnum{

    lastUpdateDatetime("last_update_datetime"),
    CreateDatetime("create_datetime"),
    EffectiveDatetime("effective_datetime"),
    ExpiredDatetime("expired_datetime"),
    DueDate("due_date"),
    GGPrice("gg_price"),
    CSPrice("cs_price"),
    NSPrice("ns_price"),
    NXPrice("nx_price"),
    NHPrice("nh_price"),
    CZPrice("cz_price"),
    WZPrice("wz_price"),
    CWPrice("cw_price"),
    YBHPrice("ybh_price"),
    WBHPrice("wbh_price"),
    Amount("amount"),
    Price("price");

    private String name;

    BABOrderByType( String name) {
        this.name = name;
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
