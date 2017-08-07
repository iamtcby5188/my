package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.facade.converter.PriceTrendsDtoConverter;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.service.QuotePriceTrendsCalculationResultsCacheService;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by fan.bai on 2017/2/23.
 * QuoteInterSystemFacade 实现类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/interSystem", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuoteInterSystemFacadeImpl extends AbstractPerformanceLogFacade implements QuoteInterSystemFacade {

    @Autowired
    private QuotePriceTrendsCalculationResultsCacheService quotePriceTrendsCalculationResultsCacheService;

    @Autowired
    private PriceTrendsDtoConverter priceTrendsDtoConverter;

    @Autowired
    private QuoteManagementInterSystemInnerFacade quoteManagementInterSystemInnterFacade;

    @Override
    @RequestMapping(value = "/retrieveCurrentSSRPriceTrends", method = RequestMethod.POST)
    public void getCurrentSSRPriceTrends(HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response,()->{
            List<QuotePriceTrendsModel> currentResults = quotePriceTrendsCalculationResultsCacheService.getCurrentResults();
            return priceTrendsDtoConverter.convertToDtos(currentResults);
        });
    }

    @Override
    @RequestMapping(value = "/insertSSRQuotes", method = RequestMethod.POST)
    public void insertNewSSRQuotes(HttpServletRequest request, HttpServletResponse response,@RequestBody List<SSRQuoteDto> dtos) {
        performWithExceptionCatch(response, () -> {
            List<SSRQuoteDto> ssrQuoteDtos = quoteManagementInterSystemInnterFacade.doInsertNewSSRQuotes(dtos);
            return ssrQuoteDtos;
        });
    }
}
