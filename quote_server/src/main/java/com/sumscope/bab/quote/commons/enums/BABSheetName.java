package com.sumscope.bab.quote.commons.enums;

/**
 * Created by Administrator on 2017/2/21.
 * 批量导入时的sheet name
 */
public enum  BABSheetName  implements WEBEnum {

    BAB_SSR_BKB_SHEET_NAME("BAB_SSR_BKB_SHEET_NAME","直贴银票业务"),

    BAB_SSR_CMB_SHEET_NAME("BAB_SSR_CMB_SHEET_NAME","直贴商票业务"),

    BAB_SSC_BKB_SHEET_NAME("BAB_SSC_BKB_SHEET_NAME","直贴银票价格"),

    BAB_SSC_CMB_SHEET_NAME("BAB_SSC_CMB_SHEET_NAME","直贴商票价格"),

    BAB_NPC_BKB_SHEET_NAME("BAB_NPC_BKB_SHEET_NAME","转贴银票价格");

    private String code;

    private String name;

    BABSheetName(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
