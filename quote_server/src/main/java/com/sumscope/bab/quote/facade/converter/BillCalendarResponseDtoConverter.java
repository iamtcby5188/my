package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.model.dto.BillCalendarDayInfoDto;
import com.sumscope.bab.quote.model.dto.BillCalendarResponseDto;
import com.sumscope.bab.quote.model.model.BillCalendarDayInfoModel;
import com.sumscope.bab.quote.model.model.BillCalendarResultModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan.bai on 2017/1/23.
 * BillCalendarResponseDto 转换器
 */
@Component
public class BillCalendarResponseDtoConverter {

    /**
     * @param model {@link BillCalendarResultModel}数据
     * @return BillCalendarResponseDto
     */
    public BillCalendarResponseDto convertToDto(BillCalendarResultModel model){
        BillCalendarResponseDto result = new BillCalendarResponseDto();
        result.setInvoiceMonthDays(convertDayInfoDto(model.getInvoiceMonthModels()));
        result.setMaturityMonthDays(convertDayInfoDto(model.getMaturityMonthModels()));
        result.setMaturityDate(model.getMaturityDate());
        result.setHoldingPeriod(model.getHoldingPeriod());
        return result;

    }

    private List<BillCalendarDayInfoDto> convertDayInfoDto(List<BillCalendarDayInfoModel> days){
        if(days == null){
            return null;
        }
        List<BillCalendarDayInfoDto> result = new ArrayList<>();
        for(BillCalendarDayInfoModel model: days){
            BillCalendarDayInfoDto dto = new BillCalendarDayInfoDto();
            BeanUtils.copyProperties(model,dto);
            result.add(dto);
        }
        return result;
    }
}
