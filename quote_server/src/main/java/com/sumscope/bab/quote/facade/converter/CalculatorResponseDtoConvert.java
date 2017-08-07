package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.model.dto.CalculatorResponseDetailsDto;
import com.sumscope.bab.quote.model.dto.CalculatorResponseDto;
import com.sumscope.bab.quote.model.model.CalculatorResponseDetailsModel;
import com.sumscope.bab.quote.model.model.CalculatorResponseModel;
import org.springframework.stereotype.Component;

/**
 * Created by fan.bai on 2017/1/24.
 * CalculatorResponseDto 转换器
 */
@Component
public class CalculatorResponseDtoConvert {
    public CalculatorResponseDto convertToDto(CalculatorResponseModel model) {
        CalculatorResponseDto dto = new CalculatorResponseDto();
        dto.setMargin(model.getMargin());
        dto.setTradeMarginAmount(model.getTradeMarginAmount());
        dto.setFirstDetailsResponse(convertToDetailDto(model.getFirstDetailsResponse()));
        dto.setSecondDetailsResponse(convertToDetailDto(model.getSecondDetailsResponse()));
        return dto;
    }

    private CalculatorResponseDetailsDto convertToDetailDto(CalculatorResponseDetailsModel model) {
        if (model == null) {
            return null;
        }
        CalculatorResponseDetailsDto dto = new CalculatorResponseDetailsDto();
        dto.setAccrualDays(model.getAccrualDays());
        dto.setDiscountAmount(model.getDiscountAmount());
        dto.setDiscountInterest(model.getDiscountInterest());
        return dto;
    }
}
