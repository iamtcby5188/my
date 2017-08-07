package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.dao.QuotePriceTrendsDao;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.model.*;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.util.UUIDUtils;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by fan.bai on 2016/12/9.
 * 实现类
 */
@Component
public class QuotePriceTrendsSearchAndCalculationServiceImpl implements QuotePriceTrendsSearchAndCalculationService {
    private static final double VALID_PRICE_MAX = 7.0; //纳入计算的最大合理价格值
    private static final double VALID_PRICE_MIN = 2.0; //纳入计算的最小合理价格值
    @Autowired
    private QuotePriceTrendsDao quotePriceTrendsDao;
    @Autowired
    private SSCQuoteQueryService sscQuoteQueryService;
    @Autowired
    private NPCQuoteQueryService npcQuoteQueryService;

    @Override
    public List<QuotePriceTrendsModel> calculateCurrentPriceTrends(Date quoteDate) {
        Map<String, CalculationTmp> calculationTmpMap = new HashMap<>();

        QueryQuotesParameterModel parameterModel = buildCalculationSearchParameter(quoteDate);

        //查询全国直贴报价并计算
        List<SSCQuoteModel> sscQuoteModels = sscQuoteQueryService.retrieveQuotesByCondition(parameterModel);
        performCalculationForModels(calculationTmpMap, sscQuoteModels, BABQuoteType.SSC);

        //查询全国转贴报价并计算
        List<NPCQuoteModel> npcQuoteModels = npcQuoteQueryService.retrieveQuotesByCondition(parameterModel);
        performCalculationForModels(calculationTmpMap, npcQuoteModels, BABQuoteType.NPC);

        return buildCalculationResults(quoteDate, calculationTmpMap);
    }

    List<QuotePriceTrendsModel> buildCalculationResults(Date quoteDate, Map<String, CalculationTmp> calculationTmpMap) {
        List<QuotePriceTrendsModel> results = new ArrayList<>();
        Date now = Calendar.getInstance().getTime();
        for (CalculationTmp calculationTmp : calculationTmpMap.values()) {
            QuotePriceTrendsModel model = new QuotePriceTrendsModel();
            BeanUtils.copyProperties(calculationTmp, model);
            model.setId(UUIDUtils.generatePrimaryKey());
            results.add(model);
            model.setCreateDatetime(now);
            model.setQuoteDate(quoteDate);
            if (calculationTmp.priceSummary != null) {
                BigDecimal avgPrice = calculationTmp.priceSummary.divide(new BigDecimal(calculationTmp.numberOfQuotes),
                        3, BigDecimal.ROUND_HALF_UP);
                model.setPriceAvg(avgPrice);
            }
        }
        return results;
    }

    <T extends AbstractCountryQuoteModel> void performCalculationForModels(
            Map<String, CalculationTmp> calculationTmpMap, List<T> quoteModel, BABQuoteType quoteType) {
        for (AbstractCountryQuoteModel model : quoteModel) {
            performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(),getTradeType(model),
                    model.isMinor(), model.getCsPrice(), BABQuotePriceType.CS);
            performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(),getTradeType(model),
                    model.isMinor(), model.getCwPrice(), BABQuotePriceType.CW);
            performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(),getTradeType(model),
                    model.isMinor(), model.getCzPrice(), BABQuotePriceType.CZ);
            performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(),getTradeType(model),
                    model.isMinor(), model.getGgPrice(), BABQuotePriceType.GG);
            performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(),getTradeType(model),
                    model.isMinor(), model.getNhPrice(), BABQuotePriceType.NH);
            performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(),getTradeType(model),
                    model.isMinor(), model.getNsPrice(), BABQuotePriceType.NS);
            performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(),getTradeType(model),
                    model.isMinor(), model.getNxPrice(), BABQuotePriceType.NX);
            performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(),getTradeType(model),
                    model.isMinor(), model.getWzPrice(), BABQuotePriceType.WZ);
            if (quoteType == BABQuoteType.SSC) {
                performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(), null,
                        model.isMinor(), ((SSCQuoteModel) model).getYbhPrice(), BABQuotePriceType.YBH);
                performCalculationForPriceType(calculationTmpMap, quoteType, model.getBillType(), model.getBillMedium(), null,
                        model.isMinor(), ((SSCQuoteModel) model).getWbhPrice(), BABQuotePriceType.WBH);
            }
        }
    }

    private BABTradeType getTradeType(AbstractCountryQuoteModel model) {
        if( model instanceof  NPCQuoteModel){
            return ((NPCQuoteModel)model).getTradeType();
        }
        return null;
    }

    private void performCalculationForPriceType(Map<String, CalculationTmp> calculationTmpMap, BABQuoteType quoteType,
                                                BABBillType billType, BABBillMedium billMedium, BABTradeType tradeType, boolean minorFlag,
                                                BigDecimal price, BABQuotePriceType quotePriceType) {
        if (!priceInValidRange(price)) {
            return;
        }
        String key = quoteType.name() + ":" + billType + ":" + billMedium + ":" + minorFlag + ":" + quotePriceType + ":" + tradeType;
        CalculationTmp calculationTmp = calculationTmpMap.get(key);
        if (calculationTmp == null) {
            calculationTmp = new CalculationTmp();
            calculationTmp.quotePriceType = quotePriceType;
            calculationTmp.quoteType = quoteType;
            calculationTmp.billMedium = billMedium;
            calculationTmp.billType = billType;
            calculationTmp.minorFlag = minorFlag;
            calculationTmp.tradeType = tradeType;
            calculationTmpMap.put(key, calculationTmp);
        }
        calculateMaxPrice(calculationTmp, price);
        calculateMinPrice(calculationTmp, price);
        calculateAvgPrice(calculationTmp, price);
    }

    //用户报价时有可能输入不正常的价格，造成的因素可能是手误，也可能是合理的非正常价格，但是在统计时将不进行统计计算，
    // 以免使统计价格偏离正常范围，
    private boolean priceInValidRange(BigDecimal price) {
        return (price != null && price.doubleValue() < VALID_PRICE_MAX && price.doubleValue() > VALID_PRICE_MIN);
    }

    private void calculateAvgPrice(CalculationTmp calculationTmp, BigDecimal price) {
        if (calculationTmp.priceSummary == null) {
            calculationTmp.priceSummary = price;
            calculationTmp.numberOfQuotes = 1;
        } else {
            calculationTmp.priceSummary = calculationTmp.priceSummary.add(price);
            calculationTmp.numberOfQuotes++;
        }
    }

    private void calculateMinPrice(CalculationTmp calculationTmp, BigDecimal price) {
        if (calculationTmp.priceMin == null) {
            calculationTmp.priceMin = price;
        } else {
            if (calculationTmp.priceMin.doubleValue() > price.doubleValue()) {
                calculationTmp.priceMin = price;
            }
        }
    }

    private void calculateMaxPrice(CalculationTmp calculationTmp, BigDecimal price) {
        if (calculationTmp.priceMax == null) {
            calculationTmp.priceMax = price;
        } else {
            if (calculationTmp.priceMax.doubleValue() < price.doubleValue()) {
                calculationTmp.priceMax = price;
            }
        }
    }

    private QueryQuotesParameterModel buildCalculationSearchParameter(Date quoteDate) {
        //查询输入时刻仍然有效的处于已发布和已成交状态的报价单
        QueryQuotesParameterModel parameterModel = new QueryQuotesParameterModel();
        parameterModel.setPaging(false);
        parameterModel.setMinor(null);
        parameterModel.setEffectiveQuotesDate(quoteDate);
        BABQuoteStatus[] valids = {BABQuoteStatus.DLD, BABQuoteStatus.DSB};
        List<BABQuoteStatus> statuses = Arrays.asList(valids);
        parameterModel.setQuoteStatusList(statuses);
        parameterModel.setExpiredQuotesDate(new Date(3099 - 1900, 1, 1));
        return parameterModel;
    }

    @Override
    @Transactional(Constant.BUSINESS_TRANSACTION_MANAGER)
    public void persistentPriceTrends(Date calcationDate, List<QuotePriceTrendsModel> trends) {
        quotePriceTrendsDao.deleteAllPriceTrendsForDate(calcationDate);
        quotePriceTrendsDao.insertPriceTrends(trends);
    }

    @Override
    public List<QuotePriceTrendsModel> searchPriceTrends(QuotePriceTrendsParameterModel parameter) {
        //设置默认值
        if (parameter.getEndDate() == null) {
            parameter.setEndDate(Calendar.getInstance().getTime());
        }
        if (parameter.getStartDate() == null) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(parameter.getEndDate());
            instance.add(Calendar.DAY_OF_YEAR, -30);
            parameter.setStartDate(instance.getTime());
            parameter.setTradeType(parameter.getTradeType());
        }
        ValidationUtil.validateModel(parameter);
        return quotePriceTrendsDao.searchPriceTrends(parameter);
    }

    /**
     * 用于计算的中间数据类型
     */
    static class CalculationTmp {
        private BABQuoteType quoteType;

        private BABQuotePriceType quotePriceType;

        private boolean minorFlag;

        private BABBillMedium billMedium;

        private BABBillType billType;

        private BABTradeType tradeType;

        private BigDecimal priceMax;

        private BigDecimal priceMin;

        private BigDecimal priceSummary;

        private int numberOfQuotes;

        public BABQuoteType getQuoteType() {
            return quoteType;
        }

        public void setQuoteType(BABQuoteType quoteType) {
            this.quoteType = quoteType;
        }

        public BABQuotePriceType getQuotePriceType() {
            return quotePriceType;
        }

        public void setQuotePriceType(BABQuotePriceType quotePriceType) {
            this.quotePriceType = quotePriceType;
        }

        public boolean isMinorFlag() {
            return minorFlag;
        }

        public void setMinorFlag(boolean minorFlag) {
            this.minorFlag = minorFlag;
        }

        public BABBillMedium getBillMedium() {
            return billMedium;
        }

        public void setBillMedium(BABBillMedium billMedium) {
            this.billMedium = billMedium;
        }

        public BABBillType getBillType() {
            return billType;
        }

        public void setBillType(BABBillType billType) {
            this.billType = billType;
        }

        public BABTradeType getTradeType() {
            return tradeType;
        }

        public void setTradeType(BABTradeType tradeType) {
            this.tradeType = tradeType;
        }

        public BigDecimal getPriceMax() {
            return priceMax;
        }

        public void setPriceMax(BigDecimal priceMax) {
            this.priceMax = priceMax;
        }

        public BigDecimal getPriceMin() {
            return priceMin;
        }

        public void setPriceMin(BigDecimal priceMin) {
            this.priceMin = priceMin;
        }

        public BigDecimal getPriceSummary() {
            return priceSummary;
        }

        public void setPriceSummary(BigDecimal priceSummary) {
            this.priceSummary = priceSummary;
        }

        public int getNumberOfQuotes() {
            return numberOfQuotes;
        }

        public void setNumberOfQuotes(int numberOfQuotes) {
            this.numberOfQuotes = numberOfQuotes;
        }
    }
}
