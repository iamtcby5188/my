package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.facade.converter.PriceTrendsDtoConverter;
import com.sumscope.bab.quote.facade.converter.PriceTrendsParameterDtoConverter;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsParameterDto;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsParameterModel;
import com.sumscope.bab.quote.service.QuotePriceTrendsCalculationResultsCacheService;
import com.sumscope.optimus.commons.facade.AbstractExceptionCatchedFacadeImpl;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/9.
 * 实例类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/trends", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuotePriceTrendsQueryFacadeImpl extends AbstractPerformanceLogFacade implements QuotePriceTrendsQueryFacade {

    @Autowired
    private QuotePriceTrendsCalculationResultsCacheService quotePriceTrendsCalculationResultsCacheService;

    @Autowired
    private PriceTrendsDtoConverter priceTrendsDtoConverter;

    @Autowired
    private PriceTrendsParameterDtoConverter priceTrendsParameterDtoConverter;

    @Autowired
    private PriceMarginAnalysisFacadeService priceMarginAnalysisFacadeService;

    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Override
    @RequestMapping(value = "/searchPriceTrendCalculation", method = RequestMethod.POST)
    public void searchCurrentPriceTrendCalculation(HttpServletRequest request, HttpServletResponse response,@RequestBody QuotePriceTrendsParameterDto parameterDto) {
        performWithExceptionCatch(response,()->{
            iamEntitlementCheck.checkValidUser(request);
            List<QuotePriceTrendsModel> currentResults = quotePriceTrendsCalculationResultsCacheService.getCurrentResults();
            List<QuotePriceTrendsDto> quotePriceTrendsDto = priceTrendsDtoConverter.convertToDtos(currentResults);
            return priceTrendsDtoConverter.convertToDtosByQuoteType(quotePriceTrendsDto,parameterDto);
        });
    }

    @Override
    @RequestMapping(value = "/searchPriceTrends", method = RequestMethod.POST)
    public void searchPriceTrends(HttpServletRequest request, HttpServletResponse response, @RequestBody QuotePriceTrendsParameterDto parameterDto) {
        performWithExceptionCatch(response,()->{
            iamEntitlementCheck.checkValidUser(request);
            QuotePriceTrendsParameterModel parameterModel = priceTrendsParameterDtoConverter.convertToModel(parameterDto);
            List<QuotePriceTrendsDto> quotePriceTrendsDto = priceMarginAnalysisFacadeService.getQuotePriceTrendsDtos(parameterModel);
            //把当天的数据也在走势图中展示出来
            List<QuotePriceTrendsDto> quotePriceTrendsDtos = priceTrendsDtoConverter.convertToResults(quotePriceTrendsDto,parameterDto);
            return priceTrendsDtoConverter.calculateQuotePriceTrendsDtosResult(quotePriceTrendsDtos, parameterDto);
        });

    }


}
