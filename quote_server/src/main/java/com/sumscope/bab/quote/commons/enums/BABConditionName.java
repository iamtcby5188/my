package com.sumscope.bab.quote.commons.enums;

/**
 * Created by Administrator on 2016/12/22.
 */
public enum BABConditionName implements WEBEnum {

    RemainingTerm("剩余期限"),

    ParValue("票面金额"),

    AcceptanceBankCategory("承兑行类别"),

    Guarantee("保函"),

    AcceptanceCategory("承兑方类别"),

    Direction("报价方向"),

    TicketType("票据类型"),

    TransactionalModel("交易模式"),

    BABQuoteStatus("报价状态"),

    TradingLocation("交易地点"),

    DateTime("期限");
    private String name;

    BABConditionName( String name) {
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
