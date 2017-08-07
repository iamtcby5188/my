package com.sumscope.bab.quote.commons;

import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import org.junit.Assert;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by fan.bai on 2017/1/25.
 * BillCalendar测试工具类
 */
public class BillCalendarTestUtils {
    public static void assertDate(Date date, String mDateS) throws ParseException {
        Date shouldResult = QuoteDateUtils.parserSimpleDateFormatString(mDateS);
        int diffDays = QuoteDateUtils.calculateDiffDays(shouldResult, date);
        Assert.assertTrue("到期日计算错误！", diffDays == 0);
    }
}
