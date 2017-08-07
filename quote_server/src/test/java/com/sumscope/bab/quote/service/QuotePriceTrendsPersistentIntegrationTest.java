package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fan.bai on 2016/12/26.
 * 测试报价统计的删除及插入功能
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"}, config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/pricetrends/ini_data.sql"}, config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class QuotePriceTrendsPersistentIntegrationTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private QuotePriceTrendsSearchAndCalculationService quotePriceTrendsSearchAndCalculationService;

    /**
     * 测试价格记录保存是否正确。先删除已经有的数据，再插入新增数据。注意，已有数据中的1号数据是6.7日记录，本次删除
     * 不会删除
     */
    @Test
    public void testPersistent() {
        Calendar instance = Calendar.getInstance();
        instance.set(2016, Calendar.JUNE, 8);
        Date calculationTimeOfDate = QuoteDateUtils.getCalculationTimeOfDate(instance.getTime());
        List<QuotePriceTrendsModel> trends = new ArrayList<>();

        QuotePriceTrendsModel model = new QuotePriceTrendsModel();
        model.setPriceAvg(new BigDecimal(2.557));
        model.setPriceMax(new BigDecimal(4.787));
        model.setPriceMin(new BigDecimal(2.007));
        model.setId("2"); //"1"号ID的记录是6月7日的，本次不删除
        model.setQuotePriceType(BABQuotePriceType.CS);
        model.setBillMedium(BABBillMedium.ELE);
        model.setBillType(BABBillType.BKB);
        model.setQuoteType(BABQuoteType.SSC);
        model.setMinorFlag(true);
        model.setQuoteDate(calculationTimeOfDate);
        model.setCreateDatetime(calculationTimeOfDate);
        trends.add(model);

        model = new QuotePriceTrendsModel();
        model.setPriceAvg(new BigDecimal(4.557));
        model.setPriceMax(new BigDecimal(5.787));
        model.setPriceMin(new BigDecimal(4.007));
        model.setId("3");
        model.setQuotePriceType(BABQuotePriceType.GG);
        model.setBillMedium(BABBillMedium.PAP);
        model.setBillType(BABBillType.CMB);
        model.setQuoteType(BABQuoteType.NPC);
        model.setQuoteDate(calculationTimeOfDate);
        model.setCreateDatetime(calculationTimeOfDate);
        trends.add(model);

        quotePriceTrendsSearchAndCalculationService.persistentPriceTrends(calculationTimeOfDate, trends);

        RowMapper rm = new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                int numberOfRows = resultSet.getInt(1);
                Assert.assertTrue("保存统计记录出错！", numberOfRows == 3);
                return numberOfRows;
            }
        };

        getBusinessJdbcTemplate().query("SELECT count(*) FROM quote_price_trend", rm);
    }

}
