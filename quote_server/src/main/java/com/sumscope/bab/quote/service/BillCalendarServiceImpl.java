package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.enums.WEBBillCalendarPeriod;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.model.BillCalendarDayInfoModel;
import com.sumscope.bab.quote.model.model.BillCalendarResultModel;
import com.sumscope.bab.quote.model.model.HolidayInfoModel;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fan.bai on 2017/1/23.
 * {@link BillCalendarService}实现类
 */
@Component
public class BillCalendarServiceImpl implements BillCalendarService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public BillCalendarResultModel calculateCanlendarForMonth(Date startDate, WEBBillCalendarPeriod duration, List<HolidayInfoModel> holidays) {
        BillCalendarResultModel result = calculateCanlendarForDate(startDate, duration, holidays);
        List<BillCalendarDayInfoModel> invoiceDays = getMonthModels(startDate, holidays);
        for (BillCalendarDayInfoModel model : invoiceDays) {
            Date date = model.getDate();
            model.setHoldingPeriod(getHoldingPeriod(date, duration, holidays));
        }
        result.setInvoiceMonthModels(invoiceDays);
        return result;
    }

    private int getHoldingPeriod(Date date, WEBBillCalendarPeriod duration, List<HolidayInfoModel> holidays) {
        Date maturityDate = getMaturityDate(date, duration, holidays);
        return QuoteDateUtils.calculateDiffDays(date, maturityDate);
    }

    @Override
    public BillCalendarResultModel calculateCanlendarForDate(Date startDate, WEBBillCalendarPeriod duration, List<HolidayInfoModel> holidays) {
        BillCalendarResultModel result = new BillCalendarResultModel();
        //仅计算传入日期对应的到期日
        Date maturityDate = getMaturityDate(startDate, duration, holidays);
        //计算到期日所属月份的日期信息
        List<BillCalendarDayInfoModel> maturityMonthDays = getMonthModels(maturityDate, holidays);
        result.setMaturityMonthModels(maturityMonthDays);
        result.setMaturityDate(maturityDate);
        result.setHoldingPeriod(QuoteDateUtils.calculateDiffDays(startDate, maturityDate));
        return result;
    }

    /**
     * 获取输入日所属月份所有日期信息
     *
     * @param date     到期日
     * @param holidays 节假日列表
     * @return 该月所有日期信息
     */
    private List<BillCalendarDayInfoModel> getMonthModels(Date date, List<HolidayInfoModel> holidays) {
        Date firstDay = getFirstDayOfMonth(date);
        Calendar calendar = Calendar.getInstance();

        List<BillCalendarDayInfoModel> results = new ArrayList<>();
        int currentMonth = getCalendarInfo(firstDay, calendar, Calendar.MONTH);

        for (int i = 0; i < 33; i++) {
            Date day = calendar.getTime();
            if (calendar.get(Calendar.MONTH) != currentMonth) {
                break;
            }
            BillCalendarDayInfoModel model = new BillCalendarDayInfoModel();
            String holidayReason = getHolidayReason(day, holidays);
            model.setDate(day);
            model.setHolidayReason(holidayReason);
            model.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            results.add(model);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        }
        return results;


    }

    private int getCalendarInfo(Date firstDay, Calendar calendar, int info) {
        calendar.setTime(firstDay);
        return calendar.get(info);
    }

    private Date getFirstDayOfMonth(Date maturityDate) {
        Calendar calendar = Calendar.getInstance();
        int year = getCalendarInfo(maturityDate, calendar, Calendar.YEAR);
        int month = getCalendarInfo(maturityDate, calendar, Calendar.MONTH);
        Calendar maturityMonthC = QuoteDateUtils.getCalendar(year, month);
        return maturityMonthC.getTime();
    }


    /**
     * 根据开始日期，持有期限，计算对应的到期日期。
     * 如果到期日期正好是节假日，取下一个工作日作为到期日。
     *
     * @param startDate 开始日期
     * @param duration  持有期，单位为日
     * @param holidays  近两年节假日列表
     * @return 到期日日期
     */
    private Date getMaturityDate(Date startDate, WEBBillCalendarPeriod duration, List<HolidayInfoModel> holidays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        if (duration == WEBBillCalendarPeriod.HALF_YEAR) {
            calendar.add(Calendar.MONTH, 6);
        } else {
            calendar.add(Calendar.YEAR, 1);
        }
        Date maturityDate = calendar.getTime();

        //如若到期日为星期六或星期日，往后推迟两天或一天
        maturityDate = getAddDayForWeekOfDate(calendar, maturityDate);

        String holidayReason = getHolidayReason(maturityDate, holidays);
        if (StringUtils.isNotBlank(holidayReason)) {
            maturityDate = getNextWorkingDay(maturityDate, holidays);
        }
        return maturityDate;
    }

    private Date getAddDayForWeekOfDate(Calendar calendar, Date maturityDate) {
        if(QuoteDateUtils.getWeekOfDate(maturityDate) == 6){
            calendar.add(Calendar.DAY_OF_MONTH,2);
            return calendar.getTime();
        }
        if(QuoteDateUtils.getWeekOfDate(maturityDate) == 7){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            return calendar.getTime();
        }
        return maturityDate;
    }

    /**
     * 查找输入日期的下一个工作日。如果输入日期不是节假日则直接输入日期。
     *
     * @param date     输入日期
     * @param holidays 节假日列表
     * @return 输入日期的下一个工作日
     */
    private Date getNextWorkingDay(Date date, List<HolidayInfoModel> holidays) {
        if (StringUtils.isNotBlank(getHolidayReason(date, holidays))) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //100天以内循环查找下一个工作日，不可能有连续30天的节假日。如果真的遇到，抛出程序异常。
            for (int i = 1; i < 30; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                if (StringUtils.isBlank(getHolidayReason(calendar.getTime(), holidays))) {
                    return calendar.getTime();
                }
            }
            LogStashFormatUtil.logError(logger,"节假日列表可能出错！出现了连续30天的节假日！");
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER, "节假日列表可能出错！");
        }
        return date;
    }


    /**
     * 判断输入日期是否为节假日并返回节假日名称
     *
     * @param date     日期
     * @param holidays 节假日列表
     * @return 节假日名称，若为工作日则返回null
     */
    private String getHolidayReason(Date date, List<HolidayInfoModel> holidays) {
        for (HolidayInfoModel model : holidays) {
            if (QuoteDateUtils.isSameDay(date, model.getDate())) {
                return model.getHolidayReason();
            }
        }
        return null;
    }

}
