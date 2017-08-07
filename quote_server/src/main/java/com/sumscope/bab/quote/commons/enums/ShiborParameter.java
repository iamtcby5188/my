package com.sumscope.bab.quote.commons.enums;

/**
 * Created by Administrator on 2017/2/6.
 * shibor 参数枚举值
 */
public enum ShiborParameter implements WEBEnum {

    SHIBOR_O_N ("SHIBOR_O/N","O/N","R001","IBO001"),

    SHIBOR_1W("SHIBOR_1W","7D","R007","IBO007"),

    SHIBOR_2W("SHIBOR_2W","14D","R014","IBO014"),

    SHIBOR_1M("SHIBOR_1M","1M","R1M","IBO1M"),

    SHIBOR_3M("SHIBOR_3M","3M","R3M","IBO3M"),

    SHIBOR_6M("SHIBOR_6M","6M","R6M","IBO6M"),

    SHIBOR_9M("SHIBOR_9M","9M","R9M","IBO9M"),

    SHIBOR_1Y("SHIBOR_1Y","1Y","R1Y","IBO1Y");

    private String code;

    private String name;

    private String rname;

    private String iboname;

    ShiborParameter(String code, String name,String rname,String iboname) {
        this.code = code;
        this.name = name;
        this.rname = rname;
        this.iboname = iboname;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public String getRname() {
        return rname;
    }

    public String getIboname() {
        return iboname;
    }
}
