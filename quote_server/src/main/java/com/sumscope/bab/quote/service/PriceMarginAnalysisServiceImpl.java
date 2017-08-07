package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import com.sumscope.bab.quote.model.model.PriceMarginModel;
import com.sumscope.bab.quote.model.model.PriceMarginPriceModel;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/1/26.
 * 价差分析service
 */
@Component
public class PriceMarginAnalysisServiceImpl implements PriceMarginAnalysisService{

    @Autowired
    private QuotePriceTrendsCalculationResultsCacheService  quotePriceTrendsService;

    @Override
    public List<PriceMarginModel> calculatePriceMargin(BABBillMedium billMedium, List<QuotePriceTrendsModel> currentPriceTrends) {
        Map <BABQuotePriceType,PriceMarginModel> map = new HashMap<>();
        if (setCurrentPriceTrendsToNull(currentPriceTrends)) {
            return new ArrayList<>();
        }
        for(QuotePriceTrendsModel priceTrends:currentPriceTrends){
            PriceMarginModel model = new PriceMarginModel();
            if(billMedium == priceTrends.getBillMedium()){
                PriceMarginModel priceMarginModel = map.get(priceTrends.getQuotePriceType());
                model.setQuotePriceType(priceTrends.getQuotePriceType());
                PriceMarginPriceModel marginModel = getPriceMarginPriceModel(priceTrends);
                if(priceMarginModel!=null){
                    setPriceMarginModelIsNotNull(map, priceTrends, model, priceMarginModel, marginModel);
                }else{
                    setPriceMarginModelIsNull(map, priceTrends, model, marginModel);
                }
            }
        }
        return converterListToMap(map);
    }

    private void setPriceMarginModelIsNull(Map<BABQuotePriceType, PriceMarginModel> map, QuotePriceTrendsModel priceTrends, PriceMarginModel model, PriceMarginPriceModel marginModel) {
        if(priceTrends.getQuoteType() == BABQuoteType.NPC && priceTrends.getTradeType() == BABTradeType.BOT){
            model.setNotesPostedPrices(marginModel);
            map.put(priceTrends.getQuotePriceType(),model);
        }
        if(priceTrends.getQuoteType() == BABQuoteType.SSC && !priceTrends.getMinorFlag()){
            model.setStraightStickPrices(marginModel);
            map.put(priceTrends.getQuotePriceType(),model);
        }
    }

    private void setPriceMarginModelIsNotNull(Map<BABQuotePriceType, PriceMarginModel> map, QuotePriceTrendsModel priceTrends, PriceMarginModel model, PriceMarginModel priceMarginModel, PriceMarginPriceModel marginModel) {
        if(priceMarginModel.getStraightStickPrices()==null && priceTrends.getQuoteType()== BABQuoteType.SSC &&
                !priceTrends.getMinorFlag()){
            model.setStraightStickPrices(marginModel);
            model.setNotesPostedPrices(priceMarginModel.getNotesPostedPrices());
            map.put(priceMarginModel.getQuotePriceType(),model);
        }
        if(priceMarginModel.getNotesPostedPrices()==null && priceTrends.getQuoteType()== BABQuoteType.NPC &&
                priceTrends.getTradeType()== BABTradeType.BOT){
            model.setNotesPostedPrices(marginModel);
            model.setStraightStickPrices(priceMarginModel.getStraightStickPrices());
            map.put(priceMarginModel.getQuotePriceType(),model);
        }
    }

    private PriceMarginPriceModel getPriceMarginPriceModel(QuotePriceTrendsModel priceTrends) {
        PriceMarginPriceModel marginModel=new PriceMarginPriceModel();
        marginModel.setAvgPrice(priceTrends.getPriceAvg());
        marginModel.setMaxPrice(priceTrends.getPriceMax());
        marginModel.setMinPrice(priceTrends.getPriceMin());
        return marginModel;
    }

    private boolean setCurrentPriceTrendsToNull(List<QuotePriceTrendsModel> currentPriceTrends) {
        if(currentPriceTrends==null || currentPriceTrends.size()==0){
            return true;
        }
        return false;
    }

    private  List<PriceMarginModel> converterListToMap(Map <BABQuotePriceType, PriceMarginModel> map){
        List<PriceMarginModel> list = new ArrayList<>();
        for(PriceMarginModel priceModel:map.values()){
            PriceMarginModel model = new PriceMarginModel();
            BigDecimal sscMin = null;
            BigDecimal sscMax = null;
            BigDecimal npcMin = null;
            BigDecimal npcMax = null;
            final int base=100;
            if(priceModel.getStraightStickPrices()!=null){
                sscMin = priceModel.getStraightStickPrices().getMinPrice();
                sscMax = priceModel.getStraightStickPrices().getMaxPrice();
            }
            if(priceModel.getNotesPostedPrices()!=null){
                npcMin = priceModel.getNotesPostedPrices().getMinPrice();
                npcMax = priceModel.getNotesPostedPrices().getMaxPrice();
            }
            BeanUtils.copyProperties(priceModel,model);
            //价差区间最小值=（直贴最低值%－转贴最高值%），小数点后一位 如对应的直贴或者转贴价格为null则默认返回给web
            model.setMinMargin((sscMin!=null && npcMax!=null) ? sscMin.subtract(npcMax).multiply(new BigDecimal(base)).intValue() : null);
            //价差区间最大值=（直贴最高值%－转贴最低值%），小数点后一位 如对应的直贴或者转贴价格为null则默认返回给web
            model.setMaxMargin((sscMax!=null && npcMin!=null) ? sscMax.subtract(npcMin).multiply(new BigDecimal(base)).intValue() : null);
            list.add(model);
        }
        return list;
    }

    @Override
    public Map getAndCalculatePriceMargins(BABBillMedium billMedium) {
        List<QuotePriceTrendsModel> currentResults = quotePriceTrendsService.getCurrentResults();
        List<PriceMarginModel> currentPriceMarginModel = calculatePriceMargin(billMedium, currentResults);
        Map<String ,Object> map = new HashMap<>();
        map.put(Constant.PRICE_MARGIN_MODEL_ANALYSIS,currentPriceMarginModel);
        map.put(Constant.SSC_ANALYSIS,getQuotePriceTrendsModelByQuoteType(currentResults,BABQuoteType.SSC,billMedium));
        map.put(Constant.NPC_ANALYSIS,getQuotePriceTrendsModelByQuoteType(currentResults,BABQuoteType.NPC,billMedium));
        return map;
    }

    private List<QuotePriceTrendsModel> getQuotePriceTrendsModelByQuoteType(List<QuotePriceTrendsModel> results,BABQuoteType babQuoteType,BABBillMedium billMedium){
        List<QuotePriceTrendsModel> list = new ArrayList<>();
        if (setCurrentPriceTrendsToNull(results)) {
            return new ArrayList<>();
        }
        for(QuotePriceTrendsModel trendsModel:results){
            if(trendsModel.getQuoteType()==BABQuoteType.NPC && trendsModel.getQuoteType()==babQuoteType
                    && billMedium == trendsModel.getBillMedium() && trendsModel.getTradeType()==BABTradeType.BOT){
                list.add(trendsModel);
            }
            if(trendsModel.getQuoteType()==BABQuoteType.SSC && trendsModel.getQuoteType()==babQuoteType
                    && billMedium == trendsModel.getBillMedium() ){
                list.add(trendsModel);
            }
        }
        return list;
    }
}
