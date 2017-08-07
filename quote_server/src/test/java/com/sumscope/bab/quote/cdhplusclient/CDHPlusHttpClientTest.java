package com.sumscope.bab.quote.cdhplusclient;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.cdhplus.httpclient.CdhPlusHttpClientWithCache;
import com.sumscope.cdhplus.httpclient.model.HolidayDto;
import com.sumscope.cdhplus.httpclient.model.ShiborDto;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2017/01/19.
 * CDHPlus客户端测试
 */
public class CDHPlusHttpClientTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private CdhPlusHttpClientWithCache cdhPlusHttpClientWithCache;

    @Test
    public void testHolidayCdhPlusHttpClientWithCache() {
        try {
            List<HolidayDto> holidayDtoWithCache = cdhPlusHttpClientWithCache.getHolidayDtoWithCache(dateToString(new Date()),dateToString(getYear(1)));
            Assert.assertTrue("获取节假日失败！",(holidayDtoWithCache!=null && holidayDtoWithCache.size()>0));
        } catch (ExternalInvocationFailedException e) {
            Assert.assertTrue("调用远程CDHPlus服务失败！",true);
        }
    }

    @Test
    public void testShiborCdhPlusHttpClientWithCache() {
        try {
            List<ShiborDto> shiborDto = cdhPlusHttpClientWithCache.getShiborWithCache(dateToString(getYear(-1)),dateToString(new Date()));
            Assert.assertTrue("获取shibor失败！",(shiborDto!=null && shiborDto.size()>0));
        } catch (ExternalInvocationFailedException e) {
            Assert.assertTrue("调用远程CDHPlus服务失败！",true);
        }
    }

    private static Date getYear(int i){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, i); //根据i获取某一年
        return calendar.getTime();
    }
    public static String dateToString(Date date){
        String str= null;
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
            str = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
