package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.BillCalendarTestUtils;
import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.enums.WEBBillCalendarPeriod;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.dto.BillCalendarDayInfoDto;
import com.sumscope.bab.quote.model.dto.BillCalendarRequestDto;
import com.sumscope.bab.quote.model.dto.BillCalendarResponseDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by fan.bai on 2017/1/25.
 * BillCalendarFacade测试类
 */
public class BillCalendarFacadeIntegrationTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private BillCalendarFacade billCalendarFacade;

    @Test
    public void testCalculateCalendarForMonth() throws ParseException {
        BillCalendarRequestDto dto = new BillCalendarRequestDto();
        Date date = QuoteDateUtils.parserSimpleDateFormatString("2015-11-01");
        dto.setDate(date);
        dto.setPeriod(WEBBillCalendarPeriod.HALF_YEAR);
        BillCalendarResponseDto result = ((BillCalendarFacadeImpl) billCalendarFacade).doCalculateCalendarForMonth(dto);
        System.out.println(result);
        Assert.assertTrue("持有期计算错误！", result.getHoldingPeriod() == 184);
        BillCalendarTestUtils.assertDate(result.getMaturityDate(),"2016-05-03");
        assertMaturityMonth(result);
        Assert.assertTrue("开始月计算错误！",result.getInvoiceMonthDays().size() == 30);
        BillCalendarDayInfoDto dayDto = result.getInvoiceMonthDays().get(0);
        Assert.assertTrue("开始月日期持有期计算错误！", dayDto.getHoldingPeriod() == 184);
        dayDto = result.getInvoiceMonthDays().get(1);
        Assert.assertTrue("开始月日期持有期计算错误！", dayDto.getHoldingPeriod() == 183);
        dayDto = result.getInvoiceMonthDays().get(2);
        Assert.assertTrue("开始月日期持有期计算错误！", dayDto.getHoldingPeriod() == 182);
    }

    @Test
    public void testCalculateCalendarForDay() throws ParseException {
        BillCalendarRequestDto dto = new BillCalendarRequestDto();
        Date date = QuoteDateUtils.parserSimpleDateFormatString("2015-11-01");
        dto.setDate(date);
        dto.setPeriod(WEBBillCalendarPeriod.HALF_YEAR);
        BillCalendarResponseDto result = ((BillCalendarFacadeImpl) billCalendarFacade).doCalculateCalendarForDate(dto);
        Assert.assertTrue("持有期计算错误！", result.getHoldingPeriod() == 184);
        BillCalendarTestUtils.assertDate(result.getMaturityDate(),"2016-05-03");
        assertMaturityMonth(result);
        Assert.assertTrue("错误生成开始月数据！",result.getInvoiceMonthDays() == null);

    }


    private void assertMaturityMonth(BillCalendarResponseDto billCalendarResultModel) {
        Assert.assertTrue("到期月计算错误！", billCalendarResultModel.getMaturityMonthDays().size() == 31);
        BillCalendarDayInfoDto dayModel = billCalendarResultModel.getMaturityMonthDays().get(0);
        Assert.assertTrue("到期月第一天日期信息错误！"+dayModel.getHolidayReason(), dayModel.getHolidayReason() != null);
        Assert.assertTrue("到期月第一天日期信息错误！", dayModel.getDayOfWeek()==1);
    }
}
