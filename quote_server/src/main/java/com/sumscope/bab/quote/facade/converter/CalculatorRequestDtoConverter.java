package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.model.dto.CalculatorRequestDetailsDto;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.model.dto.CalculatorRequestDto;
import com.sumscope.bab.quote.model.model.CalculatorRequestDetailsModel;
import com.sumscope.bab.quote.model.model.CalculatorRequestModel;
import org.springframework.stereotype.Component;

/**
 * Created by fan.bai on 2017/1/24.
 */
@Component
public class CalculatorRequestDtoConverter {
    public CalculatorRequestModel convertToModel(CalculatorRequestDto dto) {
        ValidationUtil.validateModel(dto);
        CalculatorRequestModel model = new CalculatorRequestModel();
        model.setAmount(dto.getAmount());
        model.setDueDate(dto.getDueDate());
        model.setFirstDetailsRequest(convertToDetailModel(dto.getFirstDetailsRequest()));
        model.setSecondDetailsRequest(convertToDetailModel(dto.getSecondDetailsRequest()));
        return model;

    }

    private CalculatorRequestDetailsModel convertToDetailModel(CalculatorRequestDetailsDto dto) {
        if (dto == null) {
            return null;
        }
        CalculatorRequestDetailsModel model = new CalculatorRequestDetailsModel();
        model.setTradeDate(dto.getTradeDate());
        model.setPrice(dto.getPrice());
        model.setAdjustDays(dto.getAdjustDays());
        return model;
    }
}
