package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.WEBDifference;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.dto.NetVolumeOpenMarketDto;
import com.sumscope.bab.quote.model.dto.PriceMarginDto;
import com.sumscope.bab.quote.model.dto.PriceMarginPriceDto;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.quote.model.model.PriceMarginModel;
import com.sumscope.bab.quote.model.model.PriceMarginPriceModel;
import com.sumscope.cdhplus.httpclient.model.*;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/2/6.
 * 价差分析Converter model与dto转换层
 */
@Component
public class PriceMarginAnalysisDtoConverter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<PriceMarginDto> converterPriceMarginListToListDto(List<PriceMarginModel> priceMarginModel) {
        List<PriceMarginDto> list = new ArrayList<>();
        if(priceMarginModel==null){
            return null;
        }
        for (PriceMarginModel priceModel : priceMarginModel) {
            PriceMarginDto dto = new PriceMarginDto();
            dto.setMinMargin(priceModel.getMinMargin());
            dto.setMaxMargin(priceModel.getMaxMargin());
            dto.setQuotePriceType(priceModel.getQuotePriceType());
            //价差区间最小值为正，显示为红色..价差区间最小值为负或0,显示为绿色..若当天价差区间为空，则显示为灰色
            if(priceModel.getMinMargin()!=null){
                if(priceModel.getMinMargin() > 0){
                    dto.setDifference(WEBDifference.RISE);
                }
                if(priceModel.getMinMargin() <= 0){
                    dto.setDifference(WEBDifference.FALL);
                }
            }else{
                dto.setDifference(WEBDifference.INVARIANT);
            }
            dto.setStraightStickPrices(priceModel.getStraightStickPrices()!=null ? converterNotesPostedPricesToDto(priceModel.getStraightStickPrices()) : null);
            dto.setNotesPostedPrices(priceModel.getNotesPostedPrices()!=null ? converterNotesPostedPricesToDto(priceModel.getNotesPostedPrices()) : null);
            list.add(dto);
        }
        return list;
    }

    public PriceMarginPriceDto converterNotesPostedPricesToDto(PriceMarginPriceModel pmpModel) {
        PriceMarginPriceDto pmpDto = new PriceMarginPriceDto();
        BeanUtils.copyProperties(pmpModel, pmpDto);
        return pmpDto;
    }

    public List<PriceMarginDto> converterNotesPostedPricesToDto(List<PriceMarginDto> pmpModel) {
        List<PriceMarginDto> list = new ArrayList<>();
        Map<String, PriceMarginDto> map = converterPriceMarginDtoListToMap(pmpModel);
        if (map != null && map.size() == BABQuotePriceType.values().length - 2) {
            for (PriceMarginDto dto : map.values()) {
                list.add(dto);
            }
        }else{
            list = getPriceMarginDtoForNull(list, map);
        }
        Collections.sort(list, new Comparator<PriceMarginDto>() {
            @Override
            public int compare(PriceMarginDto ob1, PriceMarginDto ob2) {
                return ob1.getQuotePriceType().compareTo(ob2.getQuotePriceType());
            }
        });
        return list;
    }

    /**
     * 承兑行类别 对应的 直转利差最高、最低值为null时返回给web对应的字段且其值为null
     */
    private List<PriceMarginDto> getPriceMarginDtoForNull(List<PriceMarginDto> list, Map<String, PriceMarginDto> map) {
        for (BABQuotePriceType priceType : BABQuotePriceType.values()) {
            if (priceType == BABQuotePriceType.WBH || priceType == BABQuotePriceType.YBH) {
                continue;
            }
            PriceMarginDto priceMarginDto = map.get(priceType.getCode());
            if (priceMarginDto == null || priceMarginDto.getQuotePriceType() == null) {
                PriceMarginDto dto = new PriceMarginDto();
                dto.setQuotePriceType(priceType);
                dto.setDifference(WEBDifference.INVARIANT);
                list.add(dto);
            }else{
                list.add(priceMarginDto);
            }
        }
        return list;
    }

    private Map<String, PriceMarginDto> converterPriceMarginDtoListToMap(List<PriceMarginDto> pmpModel) {
        Map<String, PriceMarginDto> map = new HashMap<>();
        if(pmpModel==null || pmpModel.size()==0){
            return map;
        }
        for (PriceMarginDto dto : pmpModel) {
            if (dto.getQuotePriceType() != BABQuotePriceType.YBH && dto.getQuotePriceType() != BABQuotePriceType.WBH) {
                map.put(dto.getQuotePriceType().getCode(), dto);
            }
        }
        return map;
    }

    public List<NetVolumeOpenMarketDto> converterOpenmarketModelDtoToNetVolumeOpenMarketDto(List<OpenmarketModelDto> openMarketModelDtoList) {
        List<NetVolumeOpenMarketDto> list = new ArrayList<>();
        for (OpenmarketModelDto openmarketModelDto : openMarketModelDtoList) {
            NetVolumeOpenMarketDto dto = new NetVolumeOpenMarketDto();
            dto.setDate(openmarketModelDto.getDate());
            dto.setCentralIssueOfVotes(openmarketModelDto.getYpfx());
            dto.setCentralTicketExpires(openmarketModelDto.getYpdq());

            BigDecimal ypdqZhgdq = openmarketModelDto.getYpdq().add(openmarketModelDto.getZhgdq());
            BigDecimal tfl = ypdqZhgdq.add(openmarketModelDto.getNhgcz());//投放量
            BigDecimal ypfxZhgcz = openmarketModelDto.getYpfx().add(openmarketModelDto.getZhgcz());
            BigDecimal hll = ypfxZhgcz.add(openmarketModelDto.getNhgdq());//回笼量
            BigDecimal netVolume = tfl.add(hll);//净投放量 =投放量+回笼量

            dto.setNetVolume(netVolume);
            dto.setPositiveBuyBackOperation(openmarketModelDto.getZhgcz());
            dto.setOpenMarketVolume(tfl);
            dto.setRepoMaturity(openmarketModelDto.getZhgdq());
            dto.setReverseRepoMaturity(openmarketModelDto.getNhgdq());
            dto.setWithdrawalFromCirculation(hll);
            dto.setReverseRepurchaseOperation(openmarketModelDto.getNhgcz());
            list.add(dto);
        }
        return converterMapToList(converterListToMap(list));
    }

    /**
     * Map<String, NetVolumeOpenMarketDto> 转 List<NetVolumeOpenMarketDto>
     */
    private List<NetVolumeOpenMarketDto> converterMapToList(Map<String, NetVolumeOpenMarketDto> map) {
        List<NetVolumeOpenMarketDto> list = new ArrayList<>();
        for (NetVolumeOpenMarketDto dto : map.values()) {
            list.add(dto);
        }
        return list;
    }

    /**
     * 根据 key值把map中空对象替换成list中的对象
     * List<NetVolumeOpenMarketDto> 转  Map<String, NetVolumeOpenMarketDto>
     */
    private Map<String, NetVolumeOpenMarketDto> converterListToMap(List<NetVolumeOpenMarketDto> list) {
        Map<String, NetVolumeOpenMarketDto> map = converterDtoToMap();
        for (NetVolumeOpenMarketDto dto : list) {
            NetVolumeOpenMarketDto netVolumeOpenMarketDto = map.get(JsonUtil.writeValueAsString(dto.getDate().getTime()));
            if (netVolumeOpenMarketDto != null && netVolumeOpenMarketDto.getNetVolume() == null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    Date date = sdf.parse(JsonUtil.writeValueAsString(dto.getDate().getTime()));
                    dto.setDate(new Timestamp(date.getTime()));
                    map.put(sdf.format(date), dto);
                } catch (ParseException e) {
                    LogStashFormatUtil.logWarrning(logger,"公开市场净投放日期转换失败！",e);
                }
            }
        }
        return map;
    }

    /**
     * 获取最近三十天的数据 map
     */
    public Map<String, NetVolumeOpenMarketDto> converterDtoToMap() {
        Map<String, NetVolumeOpenMarketDto> map = new HashMap<>();
        for (int i = -30; i <= 0; i++) {
            NetVolumeOpenMarketDto dto = new NetVolumeOpenMarketDto();
            dto.setDate(QuoteDateUtils.getDateTime(i).getTime());
            map.put(QuoteDateUtils.getDateTimeToStringForAnalysis(i), dto);
        }
        return map;
    }

    public List<QuotePriceTrendsDto> convertQuotePriceTrendsDto(List<IBOModelDto> iboModelDto) {
        List<QuotePriceTrendsDto> list = new ArrayList<>();
        for (IBOModelDto iboDto : iboModelDto) {
            QuotePriceTrendsDto trendsDto = convertQuotePriceTrendsDto(iboDto.getValue() !=null ? new BigDecimal(iboDto.getValue()) : null, iboDto.getDate(), iboDto.getCode());
            list.add(trendsDto);
        }
        return list;
    }

    public List<QuotePriceTrendsDto> convertQuotePriceTrendDto(List<R001ModelDto> r001ModelDto) {
        List<QuotePriceTrendsDto> list = new ArrayList<>();
        for (R001ModelDto r001Dto : r001ModelDto) {
            QuotePriceTrendsDto trendsDto = convertQuotePriceTrendsDto(r001Dto.getValue()!=null ? new BigDecimal(r001Dto.getValue()) : null, r001Dto.getDate(), r001Dto.getCode());
            list.add(trendsDto);
        }
        return list;
    }

    public QuotePriceTrendsDto convertQuotePriceTrendsDto(BigDecimal priceAvg, Date date, String code) {
        QuotePriceTrendsDto trendsDto = new QuotePriceTrendsDto();
        trendsDto.setPriceAvg(priceAvg);
        trendsDto.setQuoteDate(date);
        trendsDto.setSignCode(code);
        return trendsDto;
    }


}
