package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.model.dto.CalculatorResponseDto;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.dto.CalculatorRequestDetailsDto;
import com.sumscope.bab.quote.model.dto.CalculatorRequestDto;
import com.sumscope.optimus.commons.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by fan.bai on 2017/1/24.
 */
public class CalculatorFacadeIntegrationTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private CalculatorFacade calculatorFacade;

    @Test
    public void testCalculation() throws ParseException {
        CalculatorRequestDto requestDto = new CalculatorRequestDto();
        requestDto.setAmount(new BigDecimal(301652.00));
        requestDto.setDueDate(QuoteDateUtils.parserSimpleDateFormatString("2017-07-02"));
        requestDto.setFirstDetailsRequest(getDetailsRequest(QuoteDateUtils.parserSimpleDateFormatString("2017-01-24"), 0, new BigDecimal(3.257)));
        requestDto.setSecondDetailsRequest(getDetailsRequest(QuoteDateUtils.parserSimpleDateFormatString("2017-01-26"), 0, new BigDecimal(3.278)));

        CalculatorResponseDto responseDto = ((CalculatorFacadeImpl) calculatorFacade).doCalculation(requestDto);
        Assert.assertTrue("点差计算错误！", responseDto.getMargin().doubleValue() == 2.1);
        Assert.assertTrue("差额计算错误！", responseDto.getTradeMarginAmount().doubleValue() == 26.96);
        Assert.assertTrue("输入1计息天数计算错误！", responseDto.getFirstDetailsResponse().getAccrualDays() == 159);
        Assert.assertTrue("输入1贴现利息计算错误！", responseDto.getFirstDetailsResponse().getDiscountAmount().doubleValue() == 297312.71);
        Assert.assertTrue("输入1贴现金额计算错误！", responseDto.getFirstDetailsResponse().getDiscountInterest().doubleValue() == 4339.29);
        Assert.assertTrue("输入2计息计算错误！", responseDto.getSecondDetailsResponse().getAccrualDays() == 157);
        Assert.assertTrue("输入2贴现利息计算错误！", responseDto.getSecondDetailsResponse().getDiscountInterest().doubleValue() == 4312.33);
        Assert.assertTrue("输入2贴现金额计算错误！", responseDto.getSecondDetailsResponse().getDiscountAmount().doubleValue() == 297339.67);


    }

    private CalculatorRequestDetailsDto getDetailsRequest(Date date, int adjustDays, BigDecimal price) {
        CalculatorRequestDetailsDto result = new CalculatorRequestDetailsDto();
        result.setAdjustDays(adjustDays);
        result.setPrice(price);
        result.setTradeDate(date);
        return result;
    }

    @Test
    public void singleCalculationTest() throws ParseException {
        CalculatorRequestDto requestDto = new CalculatorRequestDto();
        requestDto.setAmount(new BigDecimal(475013987.89));
        requestDto.setDueDate(QuoteDateUtils.parserSimpleDateFormatString("2017-12-19"));
        requestDto.setFirstDetailsRequest(getDetailsRequest(QuoteDateUtils.parserSimpleDateFormatString("2017-03-05"), 1, new BigDecimal(4.372)));

        CalculatorResponseDto responseDto = ((CalculatorFacadeImpl) calculatorFacade).doCalculation(requestDto);
        Assert.assertTrue("点差计算错误！", responseDto.getMargin() == null);
        Assert.assertTrue("差额计算错误！", responseDto.getTradeMarginAmount() == null);
        Assert.assertTrue("输入1计息天数计算错误！", responseDto.getFirstDetailsResponse().getAccrualDays() == 290);
        Assert.assertTrue("输入1贴现利息计算错误！", responseDto.getFirstDetailsResponse().getDiscountAmount().doubleValue() == 458284523.03);
        Assert.assertTrue("输入1贴现金额计算错误！", responseDto.getFirstDetailsResponse().getDiscountInterest().doubleValue() == 16729464.86);
    }

    @Test
    public void testInvalidNullRequest() {
        CalculatorRequestDto requestDto = new CalculatorRequestDto();
        try {
            CalculatorResponseDto responseDto = ((CalculatorFacadeImpl) calculatorFacade).doCalculation(requestDto);

        } catch (ValidationException e) {
            Assert.assertTrue("验证失败！", e.getExceptionDetails().size() == 3);
            return;
        }
        Assert.assertTrue("验证失败！", false);
    }

    @Test
    public void testInvalidAmountRequest() throws ParseException {
        CalculatorRequestDto requestDto = new CalculatorRequestDto();
        requestDto.setAmount(new BigDecimal(999999999999999.99)); //金额超限
        requestDto.setDueDate(QuoteDateUtils.parserSimpleDateFormatString("2017-12-19"));
        requestDto.setFirstDetailsRequest(new CalculatorRequestDetailsDto());
        requestDto.getFirstDetailsRequest().setTradeDate(QuoteDateUtils.parserSimpleDateFormatString("2017-05-15"));
        requestDto.getFirstDetailsRequest().setPrice(new BigDecimal(3.234));
        try {
            CalculatorResponseDto responseDto = ((CalculatorFacadeImpl) calculatorFacade).doCalculation(requestDto);

        } catch (ValidationException e) {
            Assert.assertTrue("验证失败！", e.getExceptionDetails().size() == 1);
            return;
        }
        Assert.assertTrue("验证失败！", false);
    }

    @Test
    public void testInvalidDetailsRequest() throws ParseException {
        CalculatorRequestDto requestDto = new CalculatorRequestDto();
        requestDto.setAmount(new BigDecimal(99999999999.99));
        requestDto.setDueDate(QuoteDateUtils.parserSimpleDateFormatString("2017-12-19"));
        requestDto.setFirstDetailsRequest(new CalculatorRequestDetailsDto()); //明细请求不可为空字段仍然为null
        try {
            CalculatorResponseDto responseDto = ((CalculatorFacadeImpl) calculatorFacade).doCalculation(requestDto);

        } catch (ValidationException e) {
            Assert.assertTrue("验证失败！", e.getExceptionDetails().size() == 2);
            return;
        }
        Assert.assertTrue("验证失败！", false);
    }


}
