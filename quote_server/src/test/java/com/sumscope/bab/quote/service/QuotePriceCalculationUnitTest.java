package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.model.model.AbstractCountryQuoteModel;
import com.sumscope.bab.quote.model.model.NPCQuoteModel;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import com.sumscope.bab.quote.model.model.SSCQuoteModel;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by fan.bai on 2016/12/21.
 * 测试统计价格计算的单元测试
 */
public class QuotePriceCalculationUnitTest {
    private QuotePriceTrendsSearchAndCalculationServiceImpl service = new QuotePriceTrendsSearchAndCalculationServiceImpl();

    @Test
    public void performBuildResultsTest(){
        Map<String, QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp> calculationTmpMap = new HashMap<>();

        QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp tmp1 = new QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp();
        tmp1.setQuoteType(BABQuoteType.SSC);
        tmp1.setBillType(BABBillType.BKB);
        tmp1.setBillMedium(BABBillMedium.ELE);
        tmp1.setMinorFlag(true);
        tmp1.setQuotePriceType(BABQuotePriceType.CS);
        tmp1.setPriceMax(new BigDecimal(2.78));
        tmp1.setPriceMin(new BigDecimal(2.172));
        tmp1.setPriceSummary(new BigDecimal(78.25));
        tmp1.setNumberOfQuotes(31); //2.524193
        calculationTmpMap.put("SSC:CS",tmp1);

        QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp tmp2 = new QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp();
        tmp2.setQuoteType(BABQuoteType.SSC);
        tmp1.setBillType(BABBillType.BKB);
        tmp1.setBillMedium(BABBillMedium.ELE);
        tmp1.setMinorFlag(false);
        tmp2.setQuotePriceType(BABQuotePriceType.YBH);
        tmp2.setPriceMax(new BigDecimal(3.56));
        tmp2.setPriceMin(new BigDecimal(2.772));
        tmp2.setPriceSummary(new BigDecimal(45.224));
        tmp2.setNumberOfQuotes(15); //3.01493333
        calculationTmpMap.put("SSC:YBH",tmp2);

        List<QuotePriceTrendsModel> models = service.buildCalculationResults(Calendar.getInstance().getTime(), calculationTmpMap);

        Assert.assertTrue("模型分类错误！",models.size() == 2);

        QuotePriceTrendsModel model1 = models.get(0); //CS
        Assert.assertTrue("CS价格类型错误",model1.getQuotePriceType() == tmp1.getQuotePriceType());
        Assert.assertTrue("CS价格票据介质错误",model1.getBillMedium() == tmp1.getBillMedium());
        Assert.assertTrue("CS价格票据类型错误",model1.getBillType() == tmp1.getBillType());
        Assert.assertTrue("CS报价小票类型错误",model1.getMinorFlag() == tmp1.isMinorFlag());
        Assert.assertTrue("CS报价类型错误",model1.getQuoteType() == tmp1.getQuoteType());
        Assert.assertTrue("CS价格最大值错误",model1.getPriceMax().equals(tmp1.getPriceMax()));
        Assert.assertTrue("CS价格最小值错误",model1.getPriceMin().equals(tmp1.getPriceMin()));
        Assert.assertTrue("CS价格最大值错误",model1.getPriceAvg().doubleValue() == 2.524);

        QuotePriceTrendsModel model2 = models.get(1); //YBH
        Assert.assertTrue("CS价格类型错误",model2.getQuotePriceType() == tmp2.getQuotePriceType());
        Assert.assertTrue("CS价格票据介质错误",model2.getBillMedium() == tmp2.getBillMedium());
        Assert.assertTrue("CS价格票据类型错误",model2.getBillType() == tmp2.getBillType());
        Assert.assertTrue("CS报价小票类型错误",model2.getMinorFlag() == tmp2.isMinorFlag());
        Assert.assertTrue("CS报价类型错误",model2.getQuoteType() == tmp2.getQuoteType());
        Assert.assertTrue("YBH价格最大值错误",model2.getPriceMax().equals(tmp2.getPriceMax()));
        Assert.assertTrue("YBH价格最小值错误",model2.getPriceMin().equals(tmp2.getPriceMin()));
        Assert.assertTrue("YBH价格平均值错误",model2.getPriceAvg().doubleValue() == 3.015);

    }

    @Test
    public void performCalculationForModels(){
        Map<String, QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp> calculationTmpMap = new HashMap<>();
        List<AbstractCountryQuoteModel> quoteModel = buildTestModels();
        BABQuoteType quoteType = BABQuoteType.SSC;
        service.performCalculationForModels(calculationTmpMap,quoteModel,quoteType);
        Assert.assertTrue("计算结果类型错误！",calculationTmpMap.values().size() == 3);


        QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp calculationTmp = calculationTmpMap.get("SSC:CMB:ELE:false:WBH:null");
        Assert.assertTrue("无保函价格计算结果不可为空！",calculationTmp != null);
        Assert.assertTrue("无保函价格最小值计算错误！",calculationTmp.getPriceMin().doubleValue() == 2.456);
        Assert.assertTrue("无保函价格最大值计算错误！",calculationTmp.getPriceMax().doubleValue() == 4);
        Assert.assertTrue("无保函价格累加值计算错误！",calculationTmp.getPriceSummary().doubleValue() == 2.456+4);
        Assert.assertTrue("无保函价格累计报价单数量计算错误！",calculationTmp.getNumberOfQuotes() == 2);

        calculationTmp = calculationTmpMap.get("SSC:CMB:ELE:false:CS:null");
        Assert.assertTrue("城商价格计算结果不可为空！",calculationTmp != null);
        Assert.assertTrue("城商价格最小值计算错误！",calculationTmp.getPriceMin().doubleValue() == 2.778);
        Assert.assertTrue("城商价格最大值计算错误！",calculationTmp.getPriceMax().doubleValue() ==  2.778);
        Assert.assertTrue("城商价格累加值计算错误！",calculationTmp.getPriceSummary().doubleValue() == 2.778);
        Assert.assertTrue("城商价格累计报价单数量计算错误！",calculationTmp.getNumberOfQuotes() == 1);


    }

    @Test
    public void performCalculationForNPC(){
        Map<String, QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp> calculationTmpMap = new HashMap<>();

        List<NPCQuoteModel> quoteModel = buildNpcTestModels();
        BABQuoteType quoteType = BABQuoteType.NPC;
        service.performCalculationForModels(calculationTmpMap,quoteModel,quoteType);

        QuotePriceTrendsSearchAndCalculationServiceImpl.CalculationTmp calculationTmp = calculationTmpMap.get("NPC:BKB:ELE:false:CS:BBK");
        Assert.assertTrue("城商价格计算结果不可为空！",calculationTmp != null);
        Assert.assertTrue("城商价格最小值计算错误！",calculationTmp.getPriceMin().doubleValue() == 2.778);
        Assert.assertTrue("城商价格最大值计算错误！",calculationTmp.getPriceMax().doubleValue() ==  3.658);
        Assert.assertTrue("城商价格累加值计算错误！",calculationTmp.getPriceSummary().doubleValue() == 2.778 + 3.658);
        Assert.assertTrue("城商价格累计报价单数量计算错误！",calculationTmp.getNumberOfQuotes() == 2);
    }

    private List<NPCQuoteModel> buildNpcTestModels(){
        List<NPCQuoteModel> models = new ArrayList<>();
        NPCQuoteModel npcModel = new NPCQuoteModel();
        npcModel.setBillMedium(BABBillMedium.ELE);
        npcModel.setBillType(BABBillType.BKB);
        npcModel.setMinor(false);
        npcModel.setCsPrice(new BigDecimal(2.778));
        npcModel.setTradeType(BABTradeType.BBK);
        models.add(npcModel);

        npcModel = new NPCQuoteModel();
        npcModel.setBillMedium(BABBillMedium.ELE);
        npcModel.setBillType(BABBillType.BKB);
        npcModel.setMinor(false);
        npcModel.setCsPrice(new BigDecimal(3.658));
        npcModel.setTradeType(BABTradeType.BBK);
        models.add(npcModel);
        return models;
    }

    private List<AbstractCountryQuoteModel> buildTestModels() {
        List<AbstractCountryQuoteModel> models = new ArrayList<>();
        SSCQuoteModel model = new SSCQuoteModel();
        model.setBillMedium(BABBillMedium.ELE);
        model.setBillType(BABBillType.CMB);
        model.setMinor(false);
        model.setWbhPrice(new BigDecimal(1.232));
        model.setCsPrice(new BigDecimal(2.778));
        models.add(model);

        model = new SSCQuoteModel();
        model.setBillMedium(BABBillMedium.ELE);
        model.setBillType(BABBillType.CMB);
        model.setMinor(false);
        model.setWbhPrice(new BigDecimal(2.456));
        models.add(model);

        model = new SSCQuoteModel();
        model.setBillMedium(BABBillMedium.ELE);
        model.setBillType(BABBillType.BKB);
        model.setMinor(true);
        model.setWbhPrice(new BigDecimal(3.21));
        models.add(model);

        model = new SSCQuoteModel();
        model.setBillMedium(BABBillMedium.ELE);
        model.setBillType(BABBillType.CMB);
        model.setMinor(false);
        model.setWbhPrice(new BigDecimal(4));
        models.add(model);

        model = new SSCQuoteModel();
        model.setBillMedium(BABBillMedium.ELE);
        model.setBillType(BABBillType.CMB);
        model.setMinor(false);
        model.setWbhPrice(new BigDecimal(89));
        models.add(model);

        return models;
    }

}
