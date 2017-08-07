package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;

/**
 * 价差分析初始化web端传参
 */
public class AnalysisParameterDto {

    private BABBillMedium billMedium;

    public BABBillMedium getBillMedium() {
        return billMedium;
    }

    public void setBillMedium(BABBillMedium billMedium) {
        this.billMedium = billMedium;
    }
}
