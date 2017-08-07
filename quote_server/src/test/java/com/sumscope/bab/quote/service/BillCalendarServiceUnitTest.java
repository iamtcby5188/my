package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.model.BillCalendarResultModel;
import com.sumscope.bab.quote.commons.enums.WEBBillCalendarPeriod;
import com.sumscope.bab.quote.model.model.BillCalendarDayInfoModel;
import com.sumscope.bab.quote.model.model.HolidayInfoModel;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fan.bai on 2017/1/23.
 * BillCalendarServiceImpl 单元测试
 */
public class BillCalendarServiceUnitTest {
    private BillCalendarServiceImpl billCalendarService = new BillCalendarServiceImpl();

    private static List<HolidayInfoModel> getHolidayLists() throws ParseException {
        List<HolidayInfoModel> result = new ArrayList<>();
        result.add(getHolidayInfoModel("五一节", "2016-05-01"));
        result.add(getHolidayInfoModel("五一节", "2016-05-01"));
        result.add(getHolidayInfoModel("五一节", "2016-05-01"));
        result.add(getHolidayInfoModel("五一节", "2017-05-01"));
        result.add(getHolidayInfoModel("五一节", "2017-05-02"));
        result.add(getHolidayInfoModel("五一节", "2017-05-03"));
        result.add(getHolidayInfoModel("双休日", "2017-05-06"));
        result.add(getHolidayInfoModel("双休日", "2017-05-07"));
        result.add(getHolidayInfoModel("双休日", "2017-05-13"));
        result.add(getHolidayInfoModel("双休日", "2017-05-14"));
        result.add(getHolidayInfoModel("双休日", "2017-05-19"));
        result.add(getHolidayInfoModel("双休日", "2017-05-20"));
        result.add(getHolidayInfoModel("双休日", "2017-05-26"));
        result.add(getHolidayInfoModel("双休日", "2017-05-27"));
        return result;
    }

    private static HolidayInfoModel getHolidayInfoModel(String reason, String dateS) throws ParseException {
        HolidayInfoModel model = new HolidayInfoModel();
        model.setHolidayReason(reason);
        model.setDate(QuoteDateUtils.parserSimpleDateFormatString(dateS));
        return model;
    }

    @Test
    public void testCalculateCalendarForMonth() throws ParseException {
        Date startDate = QuoteDateUtils.parserSimpleDateFormatString("2016-11-01");
        BillCalendarResultModel resultModel = billCalendarService.calculateCanlendarForMonth(startDate, WEBBillCalendarPeriod.HALF_YEAR, getHolidayLists());
        assertMaturityDate(resultModel, "2017-05-04");
        Assert.assertTrue("持有期计算错误！", resultModel.getHoldingPeriod() == 184);
        assertMaturityMonth(resultModel);
        Assert.assertTrue("开始月计算错误！", resultModel.getInvoiceMonthModels().size() == 30);
        BillCalendarDayInfoModel model = resultModel.getInvoiceMonthModels().get(0);
        Assert.assertTrue("开始月日期持有期计算错误！", model.getHoldingPeriod() == 184);
        model = resultModel.getInvoiceMonthModels().get(1);
        Assert.assertTrue("开始月日期持有期计算错误！", model.getHoldingPeriod() == 183);
        model = resultModel.getInvoiceMonthModels().get(2);
        Assert.assertTrue("开始月日期持有期计算错误！", model.getHoldingPeriod() == 182);
    }

    /**
     * 测试到期日为节假日
     */
    @Test
    public void testCalculateCanlendarForDateConsiderHoliday() throws ParseException {
        Date startDate = QuoteDateUtils.parserSimpleDateFormatString("2016-05-01");
        BillCalendarResultModel billCalendarResultModel = billCalendarService.calculateCanlendarForDate(startDate, WEBBillCalendarPeriod.YEAR, getHolidayLists());
        assertMaturityDate(billCalendarResultModel, "2017-05-04");
        Assert.assertTrue("持有期计算错误！", billCalendarResultModel.getHoldingPeriod() == 368);
        assertMaturityMonth(billCalendarResultModel);
    }

    private void assertMaturityMonth(BillCalendarResultModel billCalendarResultModel) {
        Assert.assertTrue("到期月计算错误！", billCalendarResultModel.getMaturityMonthModels().size() == 31);
        BillCalendarDayInfoModel dayModel = billCalendarResultModel.getMaturityMonthModels().get(0);
        Assert.assertTrue("到期月第一天日期信息错误！", dayModel.getHolidayReason().equals("五一节"));
        Assert.assertTrue("到期月第一天日期信息错误！", dayModel.getDayOfWeek() == 2);
    }

    /**
     * 测试到期日为常规日期
     */
    @Test
    public void testCalculateCanlendarForDate() throws ParseException {
        Date startDate = QuoteDateUtils.parserSimpleDateFormatString("2016-05-10");
        BillCalendarResultModel billCalendarResultModel = billCalendarService.calculateCanlendarForDate(startDate, WEBBillCalendarPeriod.YEAR, getHolidayLists());
        assertMaturityDate(billCalendarResultModel, "2017-05-10");
        Assert.assertTrue("持有期计算错误！", billCalendarResultModel.getHoldingPeriod() == 365);
        assertMaturityMonth(billCalendarResultModel);

    }

    private void assertMaturityDate(BillCalendarResultModel billCalendarResultModel, String mDateS) throws ParseException {
        Date shouldResult = QuoteDateUtils.parserSimpleDateFormatString(mDateS);
        Date result = billCalendarResultModel.getMaturityDate();
        int diffDays = QuoteDateUtils.calculateDiffDays(shouldResult, result);
        Assert.assertTrue("到期日计算错误！", diffDays == 0);
    }


}
