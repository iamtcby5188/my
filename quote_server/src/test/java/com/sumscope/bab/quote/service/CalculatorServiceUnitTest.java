package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.model.CalculatorRequestDetailsModel;
import com.sumscope.bab.quote.model.model.CalculatorRequestModel;
import com.sumscope.bab.quote.model.model.CalculatorResponseModel;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fan.bai on 2017/1/24.
 * CalculatorServiceImpl单元测试类
 */
public class CalculatorServiceUnitTest {
    private CalculatorServiceImpl service = new CalculatorServiceImpl();


    private Date parseTimeString(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(s);
    }

    private CalculatorRequestDetailsModel getDetailsRequest(Date tradeDate, int adjustDays, BigDecimal interest) {
        CalculatorRequestDetailsModel result = new CalculatorRequestDetailsModel();
        result.setAdjustDays(adjustDays);
        result.setPrice(interest);
        result.setTradeDate(tradeDate);
        return result;

    }

    @Test
    public void firstCalculationTest() throws ParseException {
        CalculatorRequestModel requestModel = new CalculatorRequestModel();
        requestModel.setAmount(new BigDecimal(301652.00));
        requestModel.setDueDate(parseTimeString("2017-07-02"));
        requestModel.setFirstDetailsRequest(getDetailsRequest(parseTimeString("2017-01-24"), 0, new BigDecimal(3.257)));
        requestModel.setSecondDetailsRequest(getDetailsRequest(parseTimeString("2017-01-26"), 0, new BigDecimal(3.278)));

        CalculatorResponseModel responseModel = service.calculateBills(requestModel);
        Assert.assertTrue("点差计算错误！", responseModel.getMargin().doubleValue() == 2.1);
        Assert.assertTrue("差额计算错误！", responseModel.getTradeMarginAmount().doubleValue() == 26.96);
        Assert.assertTrue("输入1计息天数计算错误！", responseModel.getFirstDetailsResponse().getAccrualDays() == 159);
        Assert.assertTrue("输入1贴现利息计算错误！", responseModel.getFirstDetailsResponse().getDiscountAmount().doubleValue() == 297312.71);
        Assert.assertTrue("输入1贴现金额计算错误！", responseModel.getFirstDetailsResponse().getDiscountInterest().doubleValue() == 4339.29);
        Assert.assertTrue("输入2计息计算错误！", responseModel.getSecondDetailsResponse().getAccrualDays() == 157);
        Assert.assertTrue("输入2贴现利息计算错误！", responseModel.getSecondDetailsResponse().getDiscountInterest().doubleValue() == 4312.33);
        Assert.assertTrue("输入2贴现金额计算错误！", responseModel.getSecondDetailsResponse().getDiscountAmount().doubleValue() == 297339.67);
    }

    @Test
    public void singleCalculationTest() throws ParseException {
        CalculatorRequestModel requestModel = new CalculatorRequestModel();
        requestModel.setAmount(new BigDecimal(475013987.89));
        requestModel.setDueDate(parseTimeString("2017-12-19"));
        requestModel.setFirstDetailsRequest(getDetailsRequest(parseTimeString("2017-03-05"), 1, new BigDecimal(4.372)));

        CalculatorResponseModel responseModel = service.calculateBills(requestModel);
        Assert.assertTrue("点差计算错误！", responseModel.getMargin() == null);
        Assert.assertTrue("差额计算错误！", responseModel.getTradeMarginAmount() == null);
        Assert.assertTrue("输入1计息天数计算错误！", responseModel.getFirstDetailsResponse().getAccrualDays() == 290);
        Assert.assertTrue("输入1贴现利息计算错误！", responseModel.getFirstDetailsResponse().getDiscountAmount().doubleValue() == 458284523.03);
        Assert.assertTrue("输入1贴现金额计算错误！", responseModel.getFirstDetailsResponse().getDiscountInterest().doubleValue() == 16729464.86 );
    }

    @Test
    public void singleCalculationOverYearTest() throws ParseException {
        CalculatorRequestModel requestModel = new CalculatorRequestModel();
        requestModel.setAmount(new BigDecimal(5006410.00));
        requestModel.setDueDate(parseTimeString("2019-03-06"));
        requestModel.setFirstDetailsRequest(getDetailsRequest(parseTimeString("2018-06-03"), 2, new BigDecimal(5.791)));

        CalculatorResponseModel responseModel = service.calculateBills(requestModel);
        Assert.assertTrue("点差计算错误！", responseModel.getMargin() == null);
        Assert.assertTrue("差额计算错误！", responseModel.getTradeMarginAmount() == null);
        Assert.assertTrue("输入1计息天数计算错误！", responseModel.getFirstDetailsResponse().getAccrualDays() == 278);
        Assert.assertTrue("输入1贴现金额计算错误！", responseModel.getFirstDetailsResponse().getDiscountInterest().doubleValue() == 223883.60 );
        Assert.assertTrue("输入1贴现利息计算错误！", responseModel.getFirstDetailsResponse().getDiscountAmount().doubleValue() == 4782526.40);
    }

}
