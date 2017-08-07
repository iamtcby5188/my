package com.sumscope.bab.quote.commons.util;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by shaoxu.wang on 2016/12/30.
 * 价格字符串->BigDecimal转换。由于Web端精度丢失情况，我们使用String作为Dto中传递价格的类型。
 */
public final class StringPriceUtil {
    private StringPriceUtil() {
    }

    public static BigDecimal string2Price(String str) {
        if (str == null || StringUtils.isEmpty(str)) {
            return null;
        }
        BigDecimal decimal = new BigDecimal(str);
        return decimal.setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public static String price2String(BigDecimal decimal) {
        if (decimal == null) {
            return "";
        }
        return decimal.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
    }
}
