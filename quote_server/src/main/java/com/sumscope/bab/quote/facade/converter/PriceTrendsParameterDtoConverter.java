package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsParameterDto;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsParameterModel;
import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.exceptions.ValidationException;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Date;

/**
 * PriceTrendsParameterDto转换器
 */
@Component
public class PriceTrendsParameterDtoConverter {

    /**
     * 转换dto至对应model
     */
    public QuotePriceTrendsParameterModel convertToModel(QuotePriceTrendsParameterDto dto) {
        ValidationUtil.validateModel(dto);
        validateParameterDto(dto);
        QuotePriceTrendsParameterModel model = new QuotePriceTrendsParameterModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    //tradeType字段仅用于全国转贴查询
    private void validateParameterDto(QuotePriceTrendsParameterDto dto) {
        if(dto.getQuoteType() == BABQuoteType.SSC && dto.getTradeType() != null){
            ValidationExceptionDetails details = new ValidationExceptionDetails(GeneralValidationErrorType.DATA_INVALID,
                   "tradeType", "全国直贴查询时不可设置买卖方式。");
            throw new ValidationException(Collections.singletonList(details));
        }

        if(dto.getQuoteType() == BABQuoteType.NPC && dto.getTradeType() == null){
            ValidationExceptionDetails details = new ValidationExceptionDetails(GeneralValidationErrorType.DATA_INVALID,
                    "tradeType", "全国转贴查询时必须设置买卖方式。");
            throw new ValidationException(Collections.singletonList(details));
        }
    }

    /**
     *查询最近七天的价格走势model参数
     */
    public QuotePriceTrendsParameterModel convertToModelSSCAndNPC(Date beginDate, Date endDate, BABQuoteType quoteType, BABQuotePriceType type, BABBillMedium billMedium, boolean init){
        QuotePriceTrendsParameterModel parameter = new QuotePriceTrendsParameterModel();
        parameter.setMinorFlag(false);
        parameter.setQuoteType(quoteType);
        //NPC 交易模式为：买断。SSC交易模式为空/不填
        parameter.setTradeType(quoteType == BABQuoteType.NPC ? BABTradeType.BOT : null);
        parameter.setBillType(BABBillType.BKB);
        if(init){
            //QuoteDateUtils.getLastWeekTime()//默认改为一个月的数据
            parameter.setStartDate(QuoteDateUtils.getLastMonthTime());
            parameter.setEndDate(new Date());
        }else{
            parameter.setEndDate(endDate);
            parameter.setStartDate(beginDate);
        }
        parameter.setQuotePriceType(type);
        parameter.setBillMedium(billMedium);
        return parameter;
    }
}
