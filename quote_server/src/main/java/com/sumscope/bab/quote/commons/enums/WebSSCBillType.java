package com.sumscope.bab.quote.commons.enums;

/**
 * Created by fan.bai on 2017/1/9.
 * 用于Web端全国直贴界面的查询条件
 */
public enum WebSSCBillType implements WEBEnum {
    /**
     * 纸票 - 银票
     */
    PAP_BKB("PAP_BKB",false, BABBillMedium.PAP, BABBillType.BKB,"纸票"),
    /**
     * 电票 - 银票
     */
    ELE_BKB("ELE_BKB",false,BABBillMedium.ELE,BABBillType.BKB,"电票"),
    /**
     * 小纸票 - 银票
     */
    MINOR_PAP_BKB("MINOR_PAP_BKB",true,BABBillMedium.PAP,BABBillType.BKB,"小纸票"),
    /**
     * 小电票 - 银票
     */
    MINOR_ELE_BKB("MINOR_ELE_BKB",true,BABBillMedium.ELE,BABBillType.BKB,"小电票"),
    /**
     * 商票
     */
    CMB("CMB",false,null,BABBillType.CMB,"商票"),
    ;

    private final String code;
    private final boolean minorFlag;
    private final BABBillMedium medium;
    private final BABBillType billType;
    private final String displayName;

    WebSSCBillType(String code, boolean minorFlag, BABBillMedium medium, BABBillType billType, String displayName) {
        this.code = code;
        this.minorFlag = minorFlag;
        this.medium = medium;
        this.billType = billType;
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

    public boolean isMinorFlag() {
        return minorFlag;
    }

    public BABBillMedium getMedium() {
        return medium;
    }

    public BABBillType getBillType() {
        return billType;
    }
}
