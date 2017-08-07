package com.sumscope.bab.quote.externalinvoke;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.model.model.HolidayInfoModel;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by fan.bai on 2017/1/23.
 * 测试CdhClientHelper
 */
public class CdhClientHelperIntegrationTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private CdhClientHelper cdhClientHelper;

    @Test
    public void testGetHolidaysForTwoYears() throws ExternalInvocationFailedException {
        Calendar now = Calendar.getInstance();
        List<HolidayInfoModel> holidaysForTwoYears = cdhClientHelper.getHolidaysForTwoYears(now.get(Calendar.YEAR),now.get(Calendar.MONTH));
        Assert.assertTrue("无法获取节假日数据",holidaysForTwoYears.size() > 100);
    }



}
