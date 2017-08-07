package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/2/4.
 */
public class PriceMarginAnalysisFacadeServiceTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private PriceMarginAnalysisFacadeService service;

    @Test
    public void  getInitAnalysisPrice(){
//        try {
//            PriceMarginAnalysisInitResponseDto initAnalysisPrice = service.getInitAnalysisPrice(BABBillMedium.PAP);
//            Assert.assertTrue("价差分析初始化失败！", (initAnalysisPrice.getCurrentPriceTrendsNPC()==null || initAnalysisPrice.getCurrentPriceTrendsNPC().size()<=0));
//        } catch (Exception e) {
//            Assert.assertTrue("价差分析初始化失败",true);
//        }
    }
}
