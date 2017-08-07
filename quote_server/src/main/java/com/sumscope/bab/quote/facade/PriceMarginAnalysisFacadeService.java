package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.facade.converter.PriceMarginAnalysisDtoConverter;
import com.sumscope.bab.quote.facade.converter.PriceTrendsParameterDtoConverter;
import com.sumscope.bab.quote.model.dto.*;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.ShiborParameter;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.externalinvoke.CdhClientHelper;
import com.sumscope.bab.quote.facade.converter.PriceTrendsDtoConverter;
import com.sumscope.bab.quote.model.model.PriceMarginModel;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsParameterModel;
import com.sumscope.bab.quote.service.PriceMarginAnalysisService;
import com.sumscope.bab.quote.service.QuotePriceTrendsSearchAndCalculationService;
import com.sumscope.bab.quote.service.QuotePriceTrendsCalculationResultsCacheService;
import com.sumscope.cdhplus.httpclient.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.*;

/**
 * 利差分析页面功能Facade服务。为了减少Facade实现类中实现过多的业务逻辑，使用此类进行业务逻辑处理。
 */
@Component
public class PriceMarginAnalysisFacadeService {

	@Autowired
	private PriceMarginAnalysisService priceMarginAnalysisService;

	@Autowired
	private PriceTrendsDtoConverter priceTrendsDtoConverter;
    
	@Autowired
	private PriceMarginAnalysisDtoConverter priceMarginAnalysisDtoConverter;

    @Autowired
    private CdhClientHelper cdhPlusWithCache;

    @Autowired
    private QuotePriceTrendsSearchAndCalculationService searchPriceTrendsService;

    @Autowired
    private QuotePriceTrendsCalculationResultsCacheService quotePriceTrendsCalculationResultsCacheService;

    @Autowired
    private PriceTrendsParameterDtoConverter trendsParameterDtoConverter;

    @Autowired
    private ApplicationFacadeService applicationFacadeService;

	/**
	 * 获取初始化数据，初始化数据包括直转利差，当前价格统计，当前Shibor数据，直转贴价格7天走势，公开市场净投放7天走势数据。每个数据都由对应的服务获取并通过Converter进行组装。
	 * 
	 * 本方法不进行缓存，因为价格统计信息间隔一定时间将刷新
	 */
	public PriceMarginAnalysisInitResponseDto getInitResponse(BABBillMedium billMedium) {
		PriceMarginAnalysisInitResponseDto dto=new PriceMarginAnalysisInitResponseDto();
		PriceMarginAnalysisInitResponseDto initAnalysisPrice = getInitAnalysisPrice(billMedium);
		BeanUtils.copyProperties(initAnalysisPrice,dto);
        dto.setCurrentShiborPrice(getShiborPriceBetweenYestodayDto());
        //缺省值为最近一个月的数据
        List<OpenmarketModelDto> openMarketModelDto = cdhPlusWithCache.getOpenmarketModelDtoByDate(getLastMonthTime(), getToday());
        List<NetVolumeOpenMarketDto> dtos = priceMarginAnalysisDtoConverter.converterOpenmarketModelDtoToNetVolumeOpenMarketDto(openMarketModelDto);
        dto.setNetVolumeHistory(dtos);
        //直转贴价格走势初始化默认取国股O/N的数据
        PriceMarginAnalysisPriceHistoryDto priceHistory = getPriceHistoryWithCache(QuoteDateUtils.getLastMonthTime(),  new Date(),
                true, true, billMedium, BABQuotePriceType.GG,true, ShiborParameter.SHIBOR_O_N);
        dto.setPriceHistory(priceHistory);

        return dto;
	}

	/**
	 *价差分析页面中 直转利差,直贴价格,转贴价格 初始化数据，web端每五分钟调用一次获取最新数据
     */
	public PriceMarginAnalysisInitResponseDto getInitAnalysisPrice(BABBillMedium billMedium) {
		Map andCalculatePriceMargins = priceMarginAnalysisService.getAndCalculatePriceMargins(billMedium);
        List<QuotePriceTrendsModel> quotePriceTrendsModels = (List<QuotePriceTrendsModel>) andCalculatePriceMargins.get(Constant.SSC_ANALYSIS);
        List<QuotePriceTrendsDto> sscQuotePriceTrendsDto = priceTrendsDtoConverter.convertToDtosForAnalysis(quotePriceTrendsModels,false);
		List<QuotePriceTrendsDto> npcQuotePriceTrendsDto = priceTrendsDtoConverter.convertToDtosForAnalysis((List<QuotePriceTrendsModel>) andCalculatePriceMargins.get(Constant.NPC_ANALYSIS),false);
		List<PriceMarginModel> currentPriceMarginModel = (List<PriceMarginModel>)andCalculatePriceMargins.get(Constant.PRICE_MARGIN_MODEL_ANALYSIS);
		List<PriceMarginDto> priceMarginDtos = priceMarginAnalysisDtoConverter.converterPriceMarginListToListDto(currentPriceMarginModel);
		List<PriceMarginDto> priceMarginDto = priceMarginAnalysisDtoConverter.converterNotesPostedPricesToDto(priceMarginDtos);
		PriceMarginAnalysisInitResponseDto dto=new PriceMarginAnalysisInitResponseDto();
		dto.setCurrentPriceTrendsSSC(sscQuotePriceTrendsDto);
		dto.setCurrentPriceTrendsNPC(npcQuotePriceTrendsDto);
		dto.setPriceMargin(priceMarginDto);
        dto.setLatestCalculationTime(new Date());
        return dto;
	}



    /**
     *获取shibor值 并计算shibor涨跌bp点
     */
	private List<ShiborPriceBetweenYestodayDto> getShiborPriceBetweenYestodayDto(){
        List<ShiborPriceBetweenYestodayDto> currentShiborPrice=new ArrayList<>();
		Map<String, ShiborDto> yesterdayMap = cdhPlusWithCache.getMapWithShiborDto(cdhPlusWithCache.dateToString(QuoteDateUtils.getYesterdayTime()), cdhPlusWithCache.dateToString(QuoteDateUtils.getYesterdayTime()));
        Map<String, ShiborDto> currentMap = cdhPlusWithCache.getMapWithShiborDto(cdhPlusWithCache.dateToString(new Date()),cdhPlusWithCache.dateToString(new Date()));
        for(ShiborParameter parameter : ShiborParameter.values()){
            ShiborPriceBetweenYestodayDto dto = new ShiborPriceBetweenYestodayDto();
            ShiborDto currentShiborDto;
            ShiborDto yesterdayShiborDto;
            if(parameter.getCode()==Constant.SHIBOR_O_N){
                yesterdayShiborDto = yesterdayMap.get(Constant.CODE_SHIBOR_O_N);
                currentShiborDto = currentMap.get(Constant.CODE_SHIBOR_O_N);
            }else{
                yesterdayShiborDto = yesterdayMap.get(parameter.getCode());
                currentShiborDto = currentMap.get(parameter.getCode());
            }
            BigDecimal yesterday = yesterdayShiborDto!=null ? yesterdayShiborDto.getIndex_Value() : null;
            BigDecimal current = currentShiborDto!=null ? currentShiborDto.getIndex_Value() : null;
            yesterday = yesterday!=null ? yesterday.setScale(4,BigDecimal.ROUND_HALF_UP) : null;
            current = current!=null ? current.setScale(4,BigDecimal.ROUND_HALF_UP) : null;
            if(yesterday!=null && current!=null){
                BigDecimal subtract = current.subtract(yesterday);
                if(subtract!=new BigDecimal(0)){
                    BigDecimal bp = current.subtract(yesterday).multiply(new BigDecimal(100));
                    dto.setMargin(bp);
                }
            }
            dto.setDate(currentShiborDto!=null ? QuoteDateUtils.getBigoningDateWithSpecifiedTime(currentShiborDto.getIndex_Date()) : null);
            if(parameter.getCode().equals(ShiborParameter.SHIBOR_O_N.getCode())){
                dto.setPeriod(Constant.SHIBOR_O_N);
            }else{
                dto.setPeriod(parameter.getCode());
            }
            dto.setPrice(current);
            currentShiborPrice.add(dto);
        }
        return currentShiborPrice;
	}


	/**
	 * 获取直转贴价格走势数据，根据参数确定时间区间以及是否获取IBO001和R001数据。
	 * 
	 */
	public PriceMarginAnalysisPriceHistoryDto getPriceHistoryWithCache(Date beginDate, Date endDate, boolean wantIbo001, boolean wantR001,
                                                                       BABBillMedium billMedium,BABQuotePriceType quotePriceType,boolean init,ShiborParameter parameter) {
        Date begin,end;
        PriceMarginAnalysisPriceHistoryDto dto = new PriceMarginAnalysisPriceHistoryDto();
        List<QuotePriceTrendsDto> list = new ArrayList<>();
        if(beginDate==null || endDate==null){
            begin = QuoteDateUtils.getLastMonthTime();
            end = new Date();
        }else{
            begin = beginDate;
            end = endDate;

        }
        //IBO001
        if(wantIbo001){
            List<QuotePriceTrendsDto> ibo001 = getIBO001(begin, end,parameter.getIboname());
            setR001AndIbo001(init, list, ibo001, parameter.getIboname());
        }
        //R001
        if(wantR001){
            List<QuotePriceTrendsDto> r001 = getR001(begin, end,parameter.getRname());
            setR001AndIbo001(init, list, r001,parameter.getRname());
        }
        //直贴价格、
        setQuotePriceTrendsDtoNPCandSSC(begin, end, billMedium, quotePriceType, init, list, BABQuoteType.SSC, BABQuoteType.SSC.getDbCode());
        //转贴价格
        setQuotePriceTrendsDtoNPCandSSC(begin, end, billMedium, quotePriceType, init, list, BABQuoteType.NPC, BABQuoteType.NPC.getDbCode());
        //返回web端PriceTrendsHistory
        dto.setPriceTrendsHistory(list);
        //返回web端shibor
        if(parameter.getCode()==Constant.SHIBOR_O_N){
           parameter.setCode(Constant.CODE_SHIBOR_O_N);
        }
        dto.setShiborHistory(getShiborPriceDtoListByDate(begin, end,init,parameter));
        //返回web端承兑行类别和期限枚举值
        List<FilterDto> filterDtoList=new ArrayList<>();
        applicationFacadeService.setWEBParameterEnumDtoForBabQuotePriceType(filterDtoList);
        applicationFacadeService.setWEBParameterEnumDtoForShiborParameter(filterDtoList);
        dto.setFilterDto(filterDtoList);
        return dto;
	}

    private void setR001AndIbo001(boolean init, List<QuotePriceTrendsDto> list, List<QuotePriceTrendsDto> r001, String r0012) {
        if (init) {
            Map<String, QuotePriceTrendsDto> map = setQuotePriceTrendsDtoNullModel(r001, r0012);
            priceTrendsDtoConverter.setQuotePriceTrendsDtoList(map,list);
        } else {
            //获取节假日
            Map<Date, HolidayDto> holiday = cdhPlusWithCache.getLastMonthHolidays(QuoteDateUtils.getLastMonthTime(),new Date());
            List<QuotePriceTrendsDto> r001s = new ArrayList<>();
            for(QuotePriceTrendsDto dto :r001){
                HolidayDto holidayDto = holiday.get(QuoteDateUtils.getBeginingTimeByDate(dto.getQuoteDate()));
                if(holidayDto!=null){
                    continue;
                }
                r001s.add(dto);
            }
            list.addAll(r001s);
        }
    }

    private void setQuotePriceTrendsDtoNPCandSSC(Date beginDate, Date endDate, BABBillMedium billMedium, BABQuotePriceType quotePriceType, boolean init, List<QuotePriceTrendsDto> list, BABQuoteType ssc, String dbCode) {
        QuotePriceTrendsParameterModel quotePriceTrendsParameterSSC = trendsParameterDtoConverter.convertToModelSSCAndNPC(beginDate, endDate, ssc, quotePriceType, billMedium, init);
        seQuotePriceTrendsDto(init, list, quotePriceTrendsParameterSSC, dbCode);
    }

    private void seQuotePriceTrendsDto(boolean init,List<QuotePriceTrendsDto> list, QuotePriceTrendsParameterModel parameter, String sign) {
        List<QuotePriceTrendsDto> quotePriceTrendsDtos = getQuotePriceTrendsDtos(parameter);
        setR001AndIbo001(init, list, quotePriceTrendsDtos, sign);
    }

    public List<QuotePriceTrendsDto> getQuotePriceTrendsDtos(QuotePriceTrendsParameterModel parameter) {
        List<QuotePriceTrendsModel> trendsModel = searchPriceTrendsService.searchPriceTrends(parameter);//转贴价格
        List<QuotePriceTrendsModel> todayTrendsModels = quotePriceTrendsCalculationResultsCacheService.getCurrentResults();
        if(todayTrendsModels!=null && todayTrendsModels.size()>0){
            setConditionTrendsModel(parameter, trendsModel, todayTrendsModels);
        }
        return priceTrendsDtoConverter.convertToDtos(trendsModel,parameter.getQuotePriceType());
    }

    private void setConditionTrendsModel(QuotePriceTrendsParameterModel parameter, List<QuotePriceTrendsModel> trendsModel, List<QuotePriceTrendsModel> todayTrendsModels) {
        for(QuotePriceTrendsModel model:todayTrendsModels){
            if(parameter.getQuoteType()==BABQuoteType.SSC){
                doConditionTrendsForSSC(parameter, trendsModel, model);
            }
            if(parameter.getQuoteType()==BABQuoteType.NPC){
                doConditionTrendsForNPC(parameter, trendsModel, model);
            }
        }
    }

    private void doConditionTrendsForNPC(QuotePriceTrendsParameterModel parameter, List<QuotePriceTrendsModel> trendsModel, QuotePriceTrendsModel model) {
        if(model.getQuoteType() == BABQuoteType.NPC && isBooleanNPCByParam(parameter, model)){
            trendsModel.add(model);
        }
    }

    private void doConditionTrendsForSSC(QuotePriceTrendsParameterModel parameter, List<QuotePriceTrendsModel> trendsModel, QuotePriceTrendsModel model) {
        if(model.getQuoteType() == BABQuoteType.SSC && isBooleanSSCByParam(parameter, model)){
             trendsModel.add(model);
        }
    }

    private boolean isBooleanNPCByParam(QuotePriceTrendsParameterModel parameter, QuotePriceTrendsModel model) {
        return model.getQuoteType() == BABQuoteType.NPC &&  model.getTradeType()==parameter.getTradeType() && isBooleanUtils(parameter,model);
    }

    private boolean isBooleanSSCByParam(QuotePriceTrendsParameterModel parameter, QuotePriceTrendsModel model) {
        return model.getQuoteType() == BABQuoteType.SSC && isBooleanUtils(parameter,model);
    }

    private boolean isBooleanUtils(QuotePriceTrendsParameterModel parameter, QuotePriceTrendsModel model) {
        return model.getQuoteType() == parameter.getQuoteType()
                && model.getMinorFlag() == parameter.isMinorFlag()
                && model.getBillMedium() == parameter.getBillMedium()
                && model.getBillType() == parameter.getBillType();
    }

    private Map<String, QuotePriceTrendsDto> setQuotePriceTrendsDtoNullModel(List<QuotePriceTrendsDto> priceTrendsDto,String sign) {
        Map<String, QuotePriceTrendsDto> map = setMapMoth(sign);
        for(QuotePriceTrendsDto trendsDto: priceTrendsDto){
            QuotePriceTrendsDto trendsDto1 = map.get(QuoteDateUtils.getDateTimeToStringForAnalysis(trendsDto.getQuoteDate()));
            if(trendsDto1!=null && trendsDto.getSignCode().equals(sign)){
                trendsDto.setSignCode(sign);
                map.put(QuoteDateUtils.getDateTimeToStringForAnalysis(trendsDto.getQuoteDate()),trendsDto);
            }
        }
        return map;
    }

    private Map<String,QuotePriceTrendsDto> setMapMoth(String sign) {
        Map<String,QuotePriceTrendsDto> map = new HashMap<>();
        //获取节假日
        Map<Date, HolidayDto> holiday = cdhPlusWithCache.getLastMonthHolidays(QuoteDateUtils.getLastMonthTime(),new Date());
        for(int i=-30;i<=0;i++){
            QuotePriceTrendsDto priceDto= new QuotePriceTrendsDto();
            priceDto.setSignCode(sign);
            if(holiday.get(QuoteDateUtils.getBeginingTimeByDate(QuoteDateUtils.getIsDateTime(i).getTime()))!=null){
                continue;
            }
            priceDto.setQuoteDate(QuoteDateUtils.getIsDateTime(i).getTime());
            map.put(QuoteDateUtils.getDateTimeToStringForAnalysis(i),priceDto);
        }
        return map;
    }

    //根据日期 获取IBO001数据，转为IBOModelDto 放入list中
    private List<QuotePriceTrendsDto> getIBO001(Date beginDate, Date endDate,String iboName) {
        List<IBOModelDto> iboModelDto = cdhPlusWithCache.getIBOModelDtoByDate(cdhPlusWithCache.dateToString(beginDate), cdhPlusWithCache.dateToString(endDate));
        List<IBOModelDto> dto = new ArrayList<>();
        for(IBOModelDto  ibo :iboModelDto){
            if(ibo.getCode().equals(iboName)){
                dto.add(ibo);
            }
        }
        return priceMarginAnalysisDtoConverter.convertQuotePriceTrendsDto(dto);
    }
    //根据日期 获取R001数据，转为R001ModelDto 放入list中
    private List<QuotePriceTrendsDto> getR001(Date beginDate, Date endDate,String rName) {
        List<R001ModelDto> r001ModelDto = cdhPlusWithCache.getR001ModelDtoByDate(cdhPlusWithCache.dateToString(beginDate), cdhPlusWithCache.dateToString(endDate));
        List<R001ModelDto> dto = new ArrayList<>();
        for(R001ModelDto  r :r001ModelDto){
            if(r.getCode().equals(rName)){
                dto.add(r);
            }
        }
        return priceMarginAnalysisDtoConverter.convertQuotePriceTrendDto(dto);
    }



    private List<ShiborPriceDto> getShiborPriceDtoListByDate(Date beginDate, Date endDate, boolean init, ShiborParameter parameter) {
        List<ShiborDto> dto = cdhPlusWithCache.getShiborDtoByDate(cdhPlusWithCache.dateToString(beginDate), cdhPlusWithCache.dateToString(endDate));
        List<ShiborPriceDto> shiborHistory =new ArrayList<>();
        if(init){
            Map<String, ShiborPriceDto> map = setMapForShiborPrice();
            for(ShiborDto shiborDto : dto){
                //初始化数据获取SHIBOR_O_N数据
                if(shiborDto.getSource_code().equals(parameter.getCode()) ){
                    ShiborPriceDto priceDto = converterShiborDtoToShiborPriceDto(shiborDto);
                    setShiborHistory(parameter, shiborHistory, priceDto);
                    setMapByShiborPriceDto(map, priceDto);
                }
            }
            convertShiborPriceDtoListToMap(map, shiborHistory);
            return getShiborPriceDtosMapToList(map);
        }else{
            Map<Date, HolidayDto> holiday = cdhPlusWithCache.getLastMonthHolidays(QuoteDateUtils.getLastMonthTime(),new Date());
            for(ShiborDto shiborDto : dto){
                if(parameter.getCode() .equals(shiborDto.getSource_code())){
                    HolidayDto holidayDto = holiday.get(QuoteDateUtils.getBigoningDateWithSpecifiedTime(shiborDto.getIndex_Date()));
                    if(holidayDto!=null){
                       continue;
                    }
                    ShiborPriceDto priceDto = converterShiborDtoToShiborPriceDto(shiborDto);
                    setShiborHistory(parameter, shiborHistory, priceDto);
                }
            }
            return shiborHistory;
        }
    }

    private void setShiborHistory(ShiborParameter parameter, List<ShiborPriceDto> shiborHistory, ShiborPriceDto priceDto) {
        if(parameter.getCode().equals(ShiborParameter.SHIBOR_O_N.getCode())){
            priceDto.setPeriod(Constant.SHIBOR_O_N);
        }
        priceDto.setDate(QuoteDateUtils.getBigoningDateWithSpecifiedTime(priceDto.getDate()));
        shiborHistory.add(priceDto);
    }

    private void setMapByShiborPriceDto(Map<String, ShiborPriceDto> map, ShiborPriceDto priceDto) {
        ShiborPriceDto shiborPriceDto = map.get(QuoteDateUtils.getDateTimeToStringForAnalysis(priceDto.getDate()));
        if(shiborPriceDto!=null && shiborPriceDto.getPeriod()==null){
            priceDto.setDate(QuoteDateUtils.getBigoningDateWithSpecifiedTime(priceDto.getDate()));
            map.put(QuoteDateUtils.getDateTimeToStringForAnalysis(priceDto.getDate()),priceDto);
        }
    }

    private ShiborPriceDto converterShiborDtoToShiborPriceDto(ShiborDto shiborDto) {
        ShiborPriceDto priceDto = new ShiborPriceDto();
        priceDto.setPrice(shiborDto.getIndex_Value());
        priceDto.setDate(QuoteDateUtils.getBigoningDateWithSpecifiedTime(shiborDto.getIndex_Date()));
        priceDto.setPeriod(shiborDto.getSource_code());
        return priceDto;
    }

    /**
     *list 转 map
     */
    private void convertShiborPriceDtoListToMap(Map<String, ShiborPriceDto> map, List<ShiborPriceDto> shiborHistory) {
        if(shiborHistory!=null && shiborHistory.size()<30 && shiborHistory.size()>1){
            for( ShiborPriceDto shiborDto:shiborHistory){
                setMapByShiborPriceDto(map, shiborDto);
            }
        }
    }

    /**
     * map 转 list
     */
    private List<ShiborPriceDto> getShiborPriceDtosMapToList(Map<String, ShiborPriceDto> map) {
        List<ShiborPriceDto> history =new ArrayList<>();
        for(ShiborPriceDto dto: map.values()){
            history.add(dto);
        }
        return history;
    }

    /**
     *
     * 获取30天的空数据ShiborPriceDto list
     */
    private Map<String,ShiborPriceDto> setMapForShiborPrice() {
        Map<String,ShiborPriceDto> map = new HashMap<>();
        //节假日
        Map<Date, HolidayDto> holiday = cdhPlusWithCache.getLastMonthHolidays(QuoteDateUtils.getLastMonthTime(),new Date());
        for(int i=-30;i<=0;i++){
            ShiborPriceDto priceDto= new ShiborPriceDto();

            if(holiday.get(QuoteDateUtils.getBeginingTimeByDate(QuoteDateUtils.getIsDateTime(i).getTime()))!=null){
                continue;
            }
            priceDto.setDate(QuoteDateUtils.getBigoningDateWithSpecifiedTime(new Date()));
            map.put(QuoteDateUtils.getDateTimeToStringForAnalysis(i),priceDto);
        }
        return map;
    }

    /**
	 * 获取指定日期端公开市场净投放数据。
	 * 
	 */
	public List<NetVolumeOpenMarketDto> getNetVolumeHistoryWithCache(Date beginDate, Date endDate) {
        List<OpenmarketModelDto> openMarketModelDto = cdhPlusWithCache.getOpenmarketModelDtoByDate(cdhPlusWithCache.dateToString(beginDate),cdhPlusWithCache.dateToString(endDate));
		return priceMarginAnalysisDtoConverter.converterOpenmarketModelDtoToNetVolumeOpenMarketDto(openMarketModelDto);
	}

    private String getToday(){
        return cdhPlusWithCache.dateToString(new Date());
    }
    private String getLastMonthTime(){
        return cdhPlusWithCache.dateToString(QuoteDateUtils.getLastMonthTime());
    }

}
