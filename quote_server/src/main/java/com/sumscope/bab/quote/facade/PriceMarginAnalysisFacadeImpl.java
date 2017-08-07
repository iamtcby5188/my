package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.model.dto.AnalysisParameterDto;
import com.sumscope.bab.quote.model.dto.DatePeriodDto;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Administrator on 2017/1/26.
 * 价差分析
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/priceMarginAnalysis", produces = MediaType.APPLICATION_JSON_VALUE)
public class PriceMarginAnalysisFacadeImpl extends AbstractPerformanceLogFacade implements PriceMarginAnalysisFacade {

    @Autowired
    private PriceMarginAnalysisFacadeService priceMarginAnalysisFacadeService;

    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Override
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public void getInitResponse(HttpServletRequest request, HttpServletResponse response, @RequestBody AnalysisParameterDto analysisParameterDto) {
        performWithExceptionCatch(response, () -> {
            iamEntitlementCheck.checkValidUserWithNPCManagement(request);
            return priceMarginAnalysisFacadeService.getInitResponse(analysisParameterDto.getBillMedium());
        });
    }

    @Override
    @RequestMapping(value = "/analysisPrice", method = RequestMethod.POST)
    public void getInitAnalysisPrice(HttpServletRequest request, HttpServletResponse response, @RequestBody AnalysisParameterDto analysisParameterDto) {
        performWithExceptionCatch(response, () -> {
            return priceMarginAnalysisFacadeService.getInitAnalysisPrice(analysisParameterDto.getBillMedium());
        });
    }

    @Override
    @RequestMapping(value = "/getPriceTrendsHistory", method = RequestMethod.POST)
    public void getPriceTrendsHistory(HttpServletRequest request, HttpServletResponse response, @RequestBody DatePeriodDto datePeriod) {
        performWithExceptionCatch(response, () -> {
            return priceMarginAnalysisFacadeService.getPriceHistoryWithCache(datePeriod.getBeginDate(), datePeriod.getEndDate(), true, true,
                    datePeriod.getBillMedium(),datePeriod.getQuotePriceType(),false,datePeriod.getShiborParameter());
        });
    }

    @Override
    @RequestMapping(value = "/getNetVolumeHistory", method = RequestMethod.POST)
    public void getNetVolumeHistory(HttpServletRequest request, HttpServletResponse response, @RequestBody DatePeriodDto datePeriod) {
        performWithExceptionCatch(response, () -> {
            return priceMarginAnalysisFacadeService.getNetVolumeHistoryWithCache(datePeriod.getBeginDate(), datePeriod.getEndDate());
        });
    }

}
