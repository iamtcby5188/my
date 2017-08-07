package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.externalinvoke.CdhClientHelper;
import com.sumscope.bab.quote.facade.converter.BillCalendarResponseDtoConverter;
import com.sumscope.bab.quote.model.dto.BillCalendarRequestDto;
import com.sumscope.bab.quote.model.dto.BillCalendarResponseDto;
import com.sumscope.bab.quote.model.model.BillCalendarResultModel;
import com.sumscope.bab.quote.model.model.HolidayInfoModel;
import com.sumscope.bab.quote.service.BillCalendarService;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fan.bai on 2017/1/23.
 * 接口实现类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/billCalendar", produces = MediaType.APPLICATION_JSON_VALUE)
public class BillCalendarFacadeImpl extends AbstractPerformanceLogFacade implements BillCalendarFacade {
    @Autowired
    private BillCalendarService billCalendarService;

    @Autowired
    private BillCalendarResponseDtoConverter billCalendarResponseDtoConverter;

    @Autowired
    private CdhClientHelper cdhClientHelper;


    @Override
    @RequestMapping(value = "/billCalendarForDate", method = RequestMethod.POST)
    public void calculateCalendarForDate(HttpServletRequest request, HttpServletResponse response, @RequestBody BillCalendarRequestDto requestDto) {
        performWithExceptionCatch(response, () -> doCalculateCalendarForDate(requestDto));
    }

    BillCalendarResponseDto doCalculateCalendarForDate(BillCalendarRequestDto requestDto) {
        List<HolidayInfoModel> holidaysForTwoYears = getHolidayInfoModels(requestDto.getDate());
        BillCalendarResultModel billCalendarResultModel = billCalendarService.calculateCanlendarForDate(requestDto.getDate(), requestDto.getPeriod(), holidaysForTwoYears);
        return billCalendarResponseDtoConverter.convertToDto(billCalendarResultModel);
    }

    private List<HolidayInfoModel> getHolidayInfoModels(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return cdhClientHelper.getHolidaysForTwoYears(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
    }

    @Override
    @RequestMapping(value = "/billCalendarForMonth", method = RequestMethod.POST)
    public void calculateCalendarForMonth(HttpServletRequest request, HttpServletResponse response, @RequestBody BillCalendarRequestDto requestDto) {
        performWithExceptionCatch(response, () -> {
            return doCalculateCalendarForMonth(requestDto);
        });

    }

    BillCalendarResponseDto doCalculateCalendarForMonth(BillCalendarRequestDto requestDto) {
        List<HolidayInfoModel> holidaysForTwoYears = getHolidayInfoModels(requestDto.getDate());
        BillCalendarResultModel billCalendarResultModel = billCalendarService.calculateCanlendarForMonth(requestDto.getDate(), requestDto.getPeriod(), holidaysForTwoYears);
        return billCalendarResponseDtoConverter.convertToDto(billCalendarResultModel);
    }
}
