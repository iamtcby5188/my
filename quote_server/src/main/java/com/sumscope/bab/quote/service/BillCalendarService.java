package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.model.BillCalendarResultModel;
import com.sumscope.bab.quote.commons.enums.WEBBillCalendarPeriod;
import com.sumscope.bab.quote.model.model.HolidayInfoModel;

import java.util.Date;
import java.util.List;

/**
 * 票据日历计算服务
 */
public interface BillCalendarService {
	/**
	 * 根据参数计算票据到期日及到期日所属月份的每日假期信息。
	 * @param startDate 开始持有票据日期
	 * @param duration 持有长度
	 * @param holidays 假日列表
	 * @return 到期日及所属月的每日信息
	 */
	BillCalendarResultModel calculateCanlendarForDate(Date startDate, WEBBillCalendarPeriod duration, List<HolidayInfoModel> holidays);

	/**
	 * 根据参数计算持票开始日所属月的每日信息，以及到期日极其到期月每日信息。
	 * @param startDate 持票开始日
	 * @param duration 持有长度
	 * @param holidays 假日列表
	 * @return 开始日所属月每日信息，到期月每日信息及到期日
	 */
	BillCalendarResultModel calculateCanlendarForMonth(Date startDate, WEBBillCalendarPeriod duration, List<HolidayInfoModel> holidays);

}
