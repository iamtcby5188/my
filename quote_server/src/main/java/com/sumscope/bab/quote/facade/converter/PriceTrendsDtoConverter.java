package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.externalinvoke.CdhClientHelper;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsParameterDto;
import com.sumscope.cdhplus.httpclient.model.HolidayDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * PriceTrendsDto转换器
 */
@Component
public class PriceTrendsDtoConverter {
    @Autowired
    private CdhClientHelper cdhPlusWithCache;

    /**
     * 转换model至dto
     */
    public QuotePriceTrendsDto convertToDto(QuotePriceTrendsModel model) {
        QuotePriceTrendsDto quotePriceTrendsDto = new QuotePriceTrendsDto();
        BeanUtils.copyProperties(model, quotePriceTrendsDto);
        quotePriceTrendsDto.setQuoteDate(QuoteDateUtils.getBeginingTimeByDate(quotePriceTrendsDto.getQuoteDate()));
        quotePriceTrendsDto.setSignCode(quotePriceTrendsDto.getQuoteType().getDbCode());
        return quotePriceTrendsDto;
    }

    /**
     * 转换一组model置dto
     */
    public List<QuotePriceTrendsDto> convertToDtos(List<QuotePriceTrendsModel> models) {
        List<QuotePriceTrendsDto> list = new ArrayList<>();
        if (models != null && models.size() > 0) {
            for (QuotePriceTrendsModel model : models) {
                list.add(convertToDto(model));
            }
        }
        return list;
    }

    public List<QuotePriceTrendsDto> convertToResults(List<QuotePriceTrendsDto> todayQuotePriceTrendsDto,QuotePriceTrendsParameterDto parameterDto){
        List<QuotePriceTrendsDto> list = new ArrayList<>();
        for(QuotePriceTrendsDto dto :todayQuotePriceTrendsDto){
            doConditionByParam(parameterDto, list, dto);
        }
        return list;
    }

    private void doConditionByParam(QuotePriceTrendsParameterDto parameterDto, List<QuotePriceTrendsDto> list, QuotePriceTrendsDto dto) {
        if(parameterDto.getQuoteType() == BABQuoteType.SSC){
            setConditionBySSC(parameterDto, list, dto);
        }
        if(parameterDto.getQuoteType() == BABQuoteType.NPC){
            setConditionByNPC(parameterDto, list, dto);
        }
    }


    public List<QuotePriceTrendsDto> convertToDtos(List<QuotePriceTrendsModel> models,BABQuotePriceType quotePriceType) {
        List<QuotePriceTrendsDto> list = new ArrayList<>();
        if (models != null && models.size() > 0) {
            for(QuotePriceTrendsModel model: models){
                if(quotePriceType ==model.getQuotePriceType()){
                    list.add(convertToDto(model));
                }
            }
        }
        return list;
    }


    public List<QuotePriceTrendsDto> convertToDtosForAnalysis(List<QuotePriceTrendsModel> models, boolean isMinorFlag) {
        List<QuotePriceTrendsDto> list = convertToDtos(models);
        Map<String, QuotePriceTrendsDto> map = converterQuotePriceTrendsDtoListToMap(list, isMinorFlag);
        return getSortQuotePriceTrendsDtos(map);
    }

    private List<QuotePriceTrendsDto> getSortQuotePriceTrendsDtos(Map<String, QuotePriceTrendsDto> map) {
        List<QuotePriceTrendsDto> quotePriceTrendsDto = convertToQuotePriceTrendsDto(map);
        Collections.sort(quotePriceTrendsDto, new Comparator<QuotePriceTrendsDto>() {
            @Override
            public int compare(QuotePriceTrendsDto ob1, QuotePriceTrendsDto ob2) {
                return ob1.getQuotePriceType().compareTo(ob2.getQuotePriceType());
            }
        });
        return quotePriceTrendsDto;
    }


    private List<QuotePriceTrendsDto> convertToQuotePriceTrendsDto(Map<String, QuotePriceTrendsDto> map) {
        List<QuotePriceTrendsDto> trendsList = new ArrayList<>();
        if (map.size() == BABQuotePriceType.values().length - 2) {
            return getQuotePriceTrendsDtos(map, trendsList);
        } else {
            return getQuotePriceTrendsDtoForPriceNull(map, trendsList);
        }
    }

    /**
     * 承兑行类别 对应的 直贴价格或转帖价格最高、最低值为null时返回给web对应的字段且其值为null
     */
    private List<QuotePriceTrendsDto> getQuotePriceTrendsDtoForPriceNull(Map<String, QuotePriceTrendsDto> map, List<QuotePriceTrendsDto> trendsList) {
        for (BABQuotePriceType priceType : BABQuotePriceType.values()) {
            if (priceType == BABQuotePriceType.WBH || priceType == BABQuotePriceType.YBH) {
                continue;
            }
            QuotePriceTrendsDto quotePriceTrendsDto = map.get(priceType.getCode());
            if (quotePriceTrendsDto == null) {
                QuotePriceTrendsDto dto = new QuotePriceTrendsDto();
                dto.setQuotePriceType(priceType);
                dto.setPriceAvg(null);
                dto.setPriceMax(null);
                dto.setPriceMin(null);
                dto.setCreateDatetime(new Date());
                trendsList.add(dto);
            }else{
                trendsList.add(quotePriceTrendsDto);
            }
        }
        return trendsList;
    }

    /**
     * 获取银票 且是大票的数据
     * List<QuotePriceTrendsDto> 转 Map<String,QuotePriceTrendsDto>
     */
    private Map<String, QuotePriceTrendsDto> converterQuotePriceTrendsDtoListToMap(List<QuotePriceTrendsDto> list, boolean isMinorFlag) {
        Map<String, QuotePriceTrendsDto> map = new HashMap<>();
        for (QuotePriceTrendsDto dto : list) {
            if (dto.getMinorFlag() == isMinorFlag && dto.getBillType() == BABBillType.BKB) {
                map.put(dto.getQuotePriceType().getCode(), dto);
            }
        }
        return map;
    }

    private List<QuotePriceTrendsDto> converterQuotePriceTrendsDtoListToMaps(List<QuotePriceTrendsDto> list, QuotePriceTrendsParameterDto parameterDto) {
        List<QuotePriceTrendsDto> trendsDtoList = new ArrayList<>();
        for (QuotePriceTrendsDto dto : list) {
            if (isMatched(parameterDto, dto)) {
                trendsDtoList.add(dto);
            }
        }
        return trendsDtoList;
    }

    private boolean isMatched(QuotePriceTrendsParameterDto parameterDto, QuotePriceTrendsDto dto) {

        boolean result =  dto.getMinorFlag() == parameterDto.isMinorFlag() ;
        result = result &&
                dto.getBillType() == parameterDto.getBillType();
        result = result &&
                dto.getQuoteType() == parameterDto.getQuoteType() ;
        result = result &&
                dto.getBillMedium() == parameterDto.getBillMedium() ;
        result = result &&
                dto.getQuotePriceType() == parameterDto.getQuotePriceType();
        return result;
    }

    /**
     * 获取缓存中的数据，并根据web端的值筛选出对应的数据
     */
    public List<QuotePriceTrendsDto> convertToDtosByQuoteType(List<QuotePriceTrendsDto> quotePriceTrends, QuotePriceTrendsParameterDto parameterDto) {
        List<QuotePriceTrendsDto> list = new ArrayList<>();
        for (QuotePriceTrendsDto dto : quotePriceTrends) {
            doConditionByParam(parameterDto, list, dto);
        }
        return convertQuotePriceTrendsDtoListToMap(list,parameterDto.getBillType());
    }

//    private void setConditionForNPC(QuotePriceTrendsParameterDto parameterDto, List<QuotePriceTrendsDto> list, QuotePriceTrendsDto dto) {
//        if(isBooleanNPCByParam(parameterDto, dto) && dto.getQuotePriceType() ==parameterDto.getQuotePriceType()){
//            list.add(dto);
//        }
//    }
//    private void setConditionForSSC(QuotePriceTrendsParameterDto parameterDto, List<QuotePriceTrendsDto> list, QuotePriceTrendsDto dto) {
//        if (isBooleanSSCByParam(parameterDto, dto) && dto.getQuotePriceType() ==parameterDto.getQuotePriceType()) {
//            list.add(dto);
//        }
//    }
    private void setConditionByNPC(QuotePriceTrendsParameterDto parameterDto, List<QuotePriceTrendsDto> list, QuotePriceTrendsDto dto) {
        if(isBooleanNPCByParam(parameterDto, dto)){
            list.add(dto);
        }
    }
    private void setConditionBySSC(QuotePriceTrendsParameterDto parameterDto, List<QuotePriceTrendsDto> list, QuotePriceTrendsDto dto) {
        if (isBooleanSSCByParam(parameterDto, dto)) {
            list.add(dto);
        }
    }

    private boolean isBooleanNPCByParam(QuotePriceTrendsParameterDto parameterDto, QuotePriceTrendsDto dto) {
        return dto.getQuoteType() == BABQuoteType.NPC &&  dto.getTradeType()==parameterDto.getTradeType()
                && isBooleanUtils(parameterDto,dto);
    }

    private boolean isBooleanSSCByParam(QuotePriceTrendsParameterDto parameterDto, QuotePriceTrendsDto dto) {
        return dto.getQuoteType() == BABQuoteType.SSC && isBooleanUtils(parameterDto,dto);
    }
    private boolean isBooleanUtils(QuotePriceTrendsParameterDto parameterDto, QuotePriceTrendsDto dto) {
        return dto.getQuoteType() == parameterDto.getQuoteType()
                && dto.getMinorFlag() == parameterDto.isMinorFlag()
                && dto.getBillMedium() == parameterDto.getBillMedium()
                && dto.getBillType() == parameterDto.getBillType();
    }

    private List<QuotePriceTrendsDto> convertQuotePriceTrendsDtoListToMap(List<QuotePriceTrendsDto> list,BABBillType babBillType) {
        Map<String, QuotePriceTrendsDto> map = converterQuotePriceTrendsDtoMapForNull(babBillType);
        if(list == null ){
            return new ArrayList<>();
        }
        if (list.size() == BABQuotePriceType.values().length - 2) {
            return list;
        } else {
            setQuotePriceTrendsDtoMapByList(list, map);
            return converterQuotePriceTrendsDtoMapToList(map);
        }
    }

    //根据key = QuotePriceType.getCode() 把list中的对象放到map中
    private void setQuotePriceTrendsDtoMapByList(List<QuotePriceTrendsDto> list, Map<String, QuotePriceTrendsDto> map) {
        for (QuotePriceTrendsDto dto : list) {
            QuotePriceTrendsDto trendsDto = map.get(dto.getQuotePriceType().getCode());
            if (trendsDto != null && trendsDto.getQuoteDate() == null) {
                map.put(dto.getQuotePriceType().getCode(), dto);
            }
        }
    }

    private Map<String, QuotePriceTrendsDto> converterQuotePriceTrendsDtoMapForNull(BABBillType babBillType) {
        Map<String, QuotePriceTrendsDto> map = new HashMap<>();
        for (BABQuotePriceType type : BABQuotePriceType.values()) {
            if(babBillType == BABBillType.CMB){
                if (type == BABQuotePriceType.WBH || type == BABQuotePriceType.YBH) {
                    setBABQuotePriceType(map, type);
                }
            }else{
                if (type != BABQuotePriceType.WBH && type != BABQuotePriceType.YBH) {
                    setBABQuotePriceType(map, type);
                }
            }
        }
        return map;
    }

    private void setBABQuotePriceType(Map<String, QuotePriceTrendsDto> map, BABQuotePriceType type) {
        QuotePriceTrendsDto trendsDto = new QuotePriceTrendsDto();
        trendsDto.setQuotePriceType(type);
        map.put(type.getCode(), trendsDto);
    }


    /**
     * 固定给web端返回最近一个月的list数据,如果对应的天数没有价格值，也返回对应的对象，只是价格为空
     */
    public List<QuotePriceTrendsDto> calculateQuotePriceTrendsDtosResult(List<QuotePriceTrendsDto> quotePriceTrendsDtos, QuotePriceTrendsParameterDto parameterDto) {
        List<QuotePriceTrendsDto> quotePriceTrendsDtoList = converterQuotePriceTrendsDtoListToMaps(quotePriceTrendsDtos, parameterDto);
        Map<String, QuotePriceTrendsDto> map = converterQuotePriceTrendsDtoMap(parameterDto);
        setQuotePriceTrendsDtosMapByList(quotePriceTrendsDtoList, map);
        return converterQuotePriceTrendsDtoMapToList(map);
    }

    /**
     * 根据key 把list中对应的对象放到map中
     */
    private void setQuotePriceTrendsDtosMapByList(List<QuotePriceTrendsDto> quotePriceTrendsDtoList, Map<String, QuotePriceTrendsDto> map) {
        for (QuotePriceTrendsDto dto : quotePriceTrendsDtoList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dto.getCreateDatetime());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String createTime = year + "-" + month + "-" + day;
            QuotePriceTrendsDto trendsDto = map.get(createTime);
            if (trendsDto != null && trendsDto.getCreateDatetime() == null) {
                map.put(createTime, dto);
            }
        }
    }

    /**
     * Map<String,QuotePriceTrendsDto> 转 List<QuotePriceTrendsDto>
     */
    private List<QuotePriceTrendsDto> converterQuotePriceTrendsDtoMapToList(Map<String, QuotePriceTrendsDto> map) {
        List<QuotePriceTrendsDto> list = new ArrayList<>();
        return getQuotePriceTrendsDtos(map, list);
    }

    private List<QuotePriceTrendsDto> getQuotePriceTrendsDtos(Map<String, QuotePriceTrendsDto> map, List<QuotePriceTrendsDto> list) {
        setQuotePriceTrendsDtoList(map, list);
        return list;
    }

    public void setQuotePriceTrendsDtoList(Map<String, QuotePriceTrendsDto> map, List<QuotePriceTrendsDto> list) {
        Map<Date, HolidayDto> holiday = cdhPlusWithCache.getLastMonthHolidays(QuoteDateUtils.getLastMonthTime(),new Date());
        for (QuotePriceTrendsDto dto : map.values()) {
            HolidayDto holidayDto = dto.getQuoteDate()!=null ? holiday.get(QuoteDateUtils.getBeginingTimeByDate(dto.getQuoteDate())) : null;
            if(holidayDto!=null){
                continue;
            }
            dto.setPriceAvg(dto.getPriceAvg()!=null ? dto.getPriceAvg().setScale(2, BigDecimal.ROUND_HALF_UP) : null);
            dto.setPriceMax(dto.getPriceMax()!=null ? dto.getPriceMax().setScale(2, BigDecimal.ROUND_HALF_UP) : null);
            dto.setPriceMin(dto.getPriceMin()!=null ? dto.getPriceMin().setScale(2, BigDecimal.ROUND_HALF_UP) : null);
            list.add(dto);
        }
    }

    /**
     * 固定给web端返回最近一个月的list数据,如果对应的天数没有价格值，也返回对应的对象，只是价格为空
     */
    private Map<String, QuotePriceTrendsDto> converterQuotePriceTrendsDtoMap(QuotePriceTrendsParameterDto parameterDto) {
        Map<String, QuotePriceTrendsDto> map = new HashMap<>();
        for (int i = -30; i <= 0; i++) {
            QuotePriceTrendsDto dto = new QuotePriceTrendsDto();
            BeanUtils.copyProperties(parameterDto, dto);
            dto.setQuoteDate(QuoteDateUtils.getDateTime(i).getTime());
            map.put(QuoteDateUtils.getDateTimeToString(i), dto);
        }
        return map;
    }


}
