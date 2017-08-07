package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.*;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsParameterModel;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Calendar;
import java.util.List;

/**
 * Created by fan.bai on 2016/12/9.
 * test
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.HISTORY_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/pricetrends/ini_data.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class QuotePriceTrendsSearchIntegrationTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private QuotePriceTrendsSearchAndCalculationService quotePriceTrendsSearchAndCalculationService;

    @Test
    public void testSearchCsEleBkbTrue(){
        QuotePriceTrendsParameterModel model = getQuotePriceTrendsParameterModel(BABQuoteType.SSC, BABQuotePriceType.CS, BABBillMedium.ELE, BABBillType.BKB,true);
        List<QuotePriceTrendsModel> quotePriceTrendsModels = quotePriceTrendsSearchAndCalculationService.searchPriceTrends(model);

        int size = quotePriceTrendsModels.size();
        Assert.assertTrue("价格趋势查询错误！",size == 2);


    }

    @Test
    public void testSearchCsEleBkbFalse(){
        QuotePriceTrendsParameterModel model = getQuotePriceTrendsParameterModel(BABQuoteType.SSC,BABQuotePriceType.CS,BABBillMedium.ELE,BABBillType.BKB,false);

        List<QuotePriceTrendsModel> quotePriceTrendsModels = quotePriceTrendsSearchAndCalculationService.searchPriceTrends(model);

        int size = quotePriceTrendsModels.size();
        Assert.assertTrue("价格趋势查询错误！",size == 0);
    }

    @Test
    public void testSearchNhPapBkbFalse(){
        QuotePriceTrendsParameterModel model = getQuotePriceTrendsParameterModel(BABQuoteType.SSC,BABQuotePriceType.NH,BABBillMedium.PAP,BABBillType.BKB,false);

        List<QuotePriceTrendsModel> quotePriceTrendsModels = quotePriceTrendsSearchAndCalculationService.searchPriceTrends(model);

        int size = quotePriceTrendsModels.size();
        Assert.assertTrue("价格趋势查询错误！",size == 1);
    }

    @Test
    public void testSearchNPCBBK(){
        QuotePriceTrendsParameterModel model = getQuotePriceTrendsParameterModel(BABQuoteType.NPC,BABQuotePriceType.NX,BABBillMedium.PAP,BABBillType.BKB,false);
        model.setTradeType(BABTradeType.BBK);

        List<QuotePriceTrendsModel> quotePriceTrendsModels = quotePriceTrendsSearchAndCalculationService.searchPriceTrends(model);

        int size = quotePriceTrendsModels.size();
        Assert.assertTrue("价格趋势查询错误！",size == 1);
    }

    private QuotePriceTrendsParameterModel getQuotePriceTrendsParameterModel(BABQuoteType quoteType,BABQuotePriceType priceType, BABBillMedium billMedium, BABBillType billType, boolean b) {
        QuotePriceTrendsParameterModel model = new QuotePriceTrendsParameterModel();
        Calendar c = Calendar.getInstance();
        c.set(2016,Calendar.JUNE,6);
        model.setStartDate(c.getTime());
        c.set(2016,Calendar.JUNE,30);
        model.setEndDate(c.getTime());
        model.setQuotePriceType(priceType);
        model.setBillMedium(billMedium);
        model.setBillType(billType);
        model.setMinorFlag(b);
        model.setQuoteType(quoteType);
        return model;
    }

}
