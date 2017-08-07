package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.model.dto.BillCalendarRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 票据日历Facade接口
 */
public interface BillCalendarFacade {

    /**
     * 计算票据日历信息 - 根据输入日期计算到期日及到日期所属月的每日信息。此函数不计算开始日所属月信息。
     */
    void calculateCalendarForDate(HttpServletRequest request, HttpServletResponse response, BillCalendarRequestDto requestDto);

    /**
     * 计算票据日历信息 - 根据输入日期计算到期日及到日期所属月的每日信息，并且计算开始日所属月信息。
     */
    void calculateCalendarForMonth(HttpServletRequest request, HttpServletResponse response, BillCalendarRequestDto requestDto);

}
