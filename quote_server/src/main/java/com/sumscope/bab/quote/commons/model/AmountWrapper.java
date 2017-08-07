package com.sumscope.bab.quote.commons.model;

import java.math.BigDecimal;

/**
 * Created by shaoxu.wang on 2017/1/3.
 */
public class AmountWrapper {
    private BigDecimal amountLow;

    private BigDecimal amountHigh;

    public BigDecimal getAmountLow() {
        return amountLow;
    }

    public void setAmountLow(BigDecimal amountLow) {
        this.amountLow = amountLow;
    }

    public BigDecimal getAmountHigh() {
        return amountHigh;
    }

    public void setAmountHigh(BigDecimal amountHigh) {
        this.amountHigh = amountHigh;
    }
}
