package com.sumscope.bab.quote.externalinvoke;

import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.model.HolidayInfoModel;
import com.sumscope.cdhplus.httpclient.CDHPlusClientConstant;
import com.sumscope.cdhplus.httpclient.CdhPlusHttpClientWithCache;
import com.sumscope.cdhplus.httpclient.model.*;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * CDH系统外部调用封装类，避免各facade直接使用CDH的客户端。
 */
@Component
public class CdhClientHelper {
    public static final String ROOT_METHOD_NAME_START_DATE_END_DATE = "#root.method.name+#startDate+#endDate";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CIB = "CIB"; //银行间
    private static final String CNY = "CNY"; //中国
    @Autowired
    private CdhPlusHttpClientWithCache cdhPlusHttpClientWithCache;
    /**
     * 获取今天开始今年以及明年的假期列表
     * 票据系统使用CIB和CNY数据：银行见及中国大陆节假日
     */
    @Cacheable(value = CDHPlusClientConstant.CDHPLUS_CACHING_NAME,key = "#root.method.name+#year+#month")
    public List<HolidayInfoModel> getHolidaysForTwoYears(int year, int month) {
        Calendar calendar = QuoteDateUtils.getCalendar(year, month);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 2);
        Date twoYearAfter = calendar.getTime();
        List<HolidayDto> holidays = null;
        try {
            holidays = cdhPlusHttpClientWithCache.getHolidayDtoWithCache(dateToString(startDate), dateToString(twoYearAfter));
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logError(logger,"获取CDH假期失败！",e);
        }
        List<HolidayInfoModel> results = new ArrayList<>();
        if(holidays != null){
            for (HolidayDto dto : holidays) {
                if (CIB.equals(dto.getMarket_type()) && CNY.equals(dto.getCountry())) {
                    HolidayInfoModel model = new HolidayInfoModel();
                    model.setHolidayReason(dto.getHoliday_reason());
                    model.setDate(dto.getHoliday_date());
                    results.add(model);
                }
            }
        }
        return results;
    }

    @Cacheable(value = CDHPlusClientConstant.CDHPLUS_CACHING_NAME,key = "#root.method.name")
    public Map<Date,HolidayDto> getLastMonthHolidays(Date startDate,Date endDate){
        Map<Date,HolidayDto> map = new HashMap<>();
            List<HolidayDto> holidays = null;
            try {
                holidays = cdhPlusHttpClientWithCache.getHolidayDtoWithCache(dateToString(startDate), dateToString(endDate));
            } catch (ExternalInvocationFailedException e) {
                LogStashFormatUtil.logError(logger,"获取CDH假期失败",e);
            }
            if(holidays!=null && holidays.size()>0){
                for(HolidayDto dto:holidays){
                    map.put(QuoteDateUtils.getBeginingTimeByDate(dto.getHoliday_date()),dto);
                }
            }
        return map;
    }

    /**
     *获取 Map<String , ShiborDto>
     *key：source_code 例如 "SHIBOR_O/N"
     */
    @Cacheable(value = CDHPlusClientConstant.CDHPLUS_CACHING_NAME,key = ROOT_METHOD_NAME_START_DATE_END_DATE)
    public Map<String , ShiborDto> getMapWithShiborDto(String startDate,String endDate){
        Map<String , ShiborDto> map = new HashMap<>();
        try {
            List<ShiborDto> shiborDto = cdhPlusHttpClientWithCache.getShiborWithCache(startDate,endDate);
            if(shiborDto!=null && shiborDto.size()>0){
                for(ShiborDto dto:shiborDto){
                    map.put(dto.getSource_code(),dto);
                }
            }
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logError(logger,"获取 List shiborDto 失败!",e);
        }
        return map;
    }

    /**
     *获取 时间段的公开市场净投放
     */
    @Cacheable(value = CDHPlusClientConstant.CDHPLUS_CACHING_NAME,key = ROOT_METHOD_NAME_START_DATE_END_DATE)
    public List<OpenmarketModelDto> getOpenmarketModelDtoByDate(String startDate, String endDate){
        try {
            return cdhPlusHttpClientWithCache.getOpenMarketWithCache(startDate, endDate);
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logError(logger,"公开市场净投放Http请求失败！",e);
        }
        return new ArrayList<>();
    }

    /**
     *获取 IBO001 对应时间段的数据
     */
    @Cacheable(value = CDHPlusClientConstant.CDHPLUS_CACHING_NAME,key = ROOT_METHOD_NAME_START_DATE_END_DATE)
    public List<IBOModelDto> getIBOModelDtoByDate(String startDate, String endDate){
        try {
            return cdhPlusHttpClientWithCache.getIBOWithCache(startDate, endDate);
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logError(logger,"IBO001Http请求失败！",e);
        }
        return new ArrayList<>();
    }

    /**
     *获取 R001 对应时间断的数据
     */
    @Cacheable(value = CDHPlusClientConstant.CDHPLUS_CACHING_NAME,key = ROOT_METHOD_NAME_START_DATE_END_DATE)
    public List<R001ModelDto> getR001ModelDtoByDate(String startDate, String endDate){
        try {
            return cdhPlusHttpClientWithCache.getR001WithCache(startDate, endDate);
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logError(logger,"R001Http请求失败！",e);
        }
        return new ArrayList<>();
    }
    /**
     *获取 shibor 对应时间断的数据
     */
    @Cacheable(value = CDHPlusClientConstant.CDHPLUS_CACHING_NAME,key = ROOT_METHOD_NAME_START_DATE_END_DATE,condition="#shibor==null || #shibor.size()<=0")
    public List<ShiborDto> getShiborDtoByDate(String startDate, String endDate){
        try {
            return cdhPlusHttpClientWithCache.getShiborWithCache(startDate, endDate);
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logError(logger,"shiborHttp请求失败！",e);
        }
        return new ArrayList<>();
    }

    public static String dateToString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }
}
