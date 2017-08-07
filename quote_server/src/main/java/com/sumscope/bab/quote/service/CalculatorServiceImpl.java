package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.model.CalculatorRequestDetailsModel;
import com.sumscope.bab.quote.model.model.CalculatorRequestModel;
import com.sumscope.bab.quote.model.model.CalculatorResponseDetailsModel;
import com.sumscope.bab.quote.model.model.CalculatorResponseModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by fan.bai on 2017/1/24.
 * 计算服务接口实现类
 */
@Component
class CalculatorServiceImpl implements CalculatorService {
    /**
     * 本方法内不再做数据验证，调用方法前请确保数据有效性
     */
    @Override
    public CalculatorResponseModel calculateBills(CalculatorRequestModel requestModel) {
        CalculatorResponseModel responseModel = new CalculatorResponseModel();
        CalculatorResponseDetailsModel firstDetailsModel = calculateDetails(requestModel.getAmount(), requestModel.getDueDate(), requestModel.getFirstDetailsRequest());
        responseModel.setFirstDetailsResponse(firstDetailsModel);
        if (requestModel.getSecondDetailsRequest() != null) {
            CalculatorResponseDetailsModel secondDetailsModel = calculateDetails(requestModel.getAmount(), requestModel.getDueDate(), requestModel.getSecondDetailsRequest());
            responseModel.setSecondDetailsResponse(secondDetailsModel);
            responseModel.setTradeMarginAmount(calculateMarginAmount(firstDetailsModel.getDiscountAmount(), secondDetailsModel.getDiscountAmount()));
            responseModel.setMargin(calculateMargin(requestModel.getFirstDetailsRequest().getPrice(), requestModel.getSecondDetailsRequest().getPrice()));
        }
        return responseModel;
    }

    private BigDecimal calculateMarginAmount(BigDecimal discountAmount, BigDecimal discountAmount1) {
        //贴现金额差额的绝对值
        return discountAmount.subtract(discountAmount1).abs();
    }

    private BigDecimal calculateMargin(BigDecimal price, BigDecimal price1) {
        //        (a%－b%)*10000的绝对值 - 本方法不计%直接计算数值部分，因此只要乘以100即可
        return price.subtract(price1).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).abs();
    }


    private CalculatorResponseDetailsModel calculateDetails(BigDecimal amount, Date dueDate, CalculatorRequestDetailsModel firstDetailsRequest) {
        CalculatorResponseDetailsModel result = new CalculatorResponseDetailsModel();
        result.setAccrualDays(calculateAccrualDays(firstDetailsRequest.getTradeDate(), dueDate, firstDetailsRequest.getAdjustDays()));
        result.setDiscountInterest(calculateDiscountInterest(amount, firstDetailsRequest.getPrice(), result.getAccrualDays()));
        result.setDiscountAmount(calculateDiscountAmount(amount, result.getDiscountInterest()));
        return result;
    }

    private BigDecimal calculateDiscountAmount(BigDecimal amount, BigDecimal discountInterest) {
        //贴现金额=票面金额-贴现利息
        return amount.subtract(discountInterest).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateDiscountInterest(BigDecimal amount, BigDecimal price, int accrualDays) {
        //贴现利息 = 票面金额*利率％*计息天数÷360, 年利率保留3位小数，计算结果保留2位小数
        BigDecimal priceWithScale = price.setScale(20, BigDecimal.ROUND_HALF_UP); //price由前端传入时会默认设置为scale = 3， 此时进行除法（/100）运算时精度将不够，因此在这里重新设置足够长的小数位。
        priceWithScale = priceWithScale.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP).setScale(5, BigDecimal.ROUND_HALF_UP);
        return amount.multiply(priceWithScale).multiply(new BigDecimal(accrualDays)).divide(new BigDecimal(360), 5, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);

    }

    private int calculateAccrualDays(Date tradeDate, Date dueDate, int adjustDays) {
        //计算天数 = 到期日 - 交易日 + 调整天数
        int result = QuoteDateUtils.calculateDiffDays(tradeDate, dueDate);
        return result + adjustDays;
    }

}
