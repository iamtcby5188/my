package com.sumscope.bab.quote.commons.enums;

/**
 * 上涨或者下跌
 */
public enum WEBDifference implements WEBEnum {
    RISE("RISE","上涨"),
    FALL("FALL","下跌"),
    INVARIANT("INVARIANT","不变");

    private final String code;
    private final String name;

    WEBDifference(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode(){
        return code;
    }

    public String getDisplayName(){
        return name;
    }

}
