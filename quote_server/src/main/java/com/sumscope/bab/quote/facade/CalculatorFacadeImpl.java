package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.facade.converter.CalculatorResponseDtoConvert;
import com.sumscope.bab.quote.facade.converter.CalculatorRequestDtoConverter;
import com.sumscope.bab.quote.model.dto.CalculatorRequestDto;
import com.sumscope.bab.quote.model.dto.CalculatorResponseDto;
import com.sumscope.bab.quote.model.model.CalculatorRequestModel;
import com.sumscope.bab.quote.model.model.CalculatorResponseModel;
import com.sumscope.bab.quote.service.CalculatorService;
import com.sumscope.optimus.commons.facade.AbstractExceptionCatchedFacadeImpl;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fan.bai on 2017/1/24.
 * 计算器Facade实现类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/calculator", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalculatorFacadeImpl extends AbstractPerformanceLogFacade implements CalculatorFacade {
    @Autowired
    private CalculatorRequestDtoConverter calculatorRequestDtoConverter;

    @Autowired
    private CalculatorResponseDtoConvert calculatorResponseDtoConvert;

    @Autowired
    private CalculatorService calculatorService;


    @Override
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public void calculateBills(HttpServletRequest request, HttpServletResponse response, @RequestBody CalculatorRequestDto parameterDto) {
        performWithExceptionCatch(response, () -> doCalculation(parameterDto));

    }

    CalculatorResponseDto doCalculation(CalculatorRequestDto parameterDto) {
        CalculatorRequestModel requestModel = calculatorRequestDtoConverter.convertToModel(parameterDto);
        CalculatorResponseModel responseModel = calculatorService.calculateBills(requestModel);
        return calculatorResponseDtoConvert.convertToDto(responseModel);
    }
}
