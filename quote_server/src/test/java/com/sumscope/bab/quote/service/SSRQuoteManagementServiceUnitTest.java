package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.Direction;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/29.
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.HISTORY_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/ssr_init_data.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class SSRQuoteManagementServiceUnitTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private SSRQuoteManagementService ssrQuoteManagementService;

    @Autowired
    private SSRQuoteQueryService ssrQuoteQueryService;

    @Test
    public void insertNewQuotesTest() {
        SSRQuoteModel model = getModel();
        String id = new String("5");
        model.setId(id);

        List<SSRQuoteModel> models = new ArrayList<>();
        models.add(model);

        List<String> ids = new ArrayList<>();
        ids.add(id);
        List<SSRQuoteModel> quotes = ssrQuoteQueryService.retrieveQuoteByIDs(ids);
        Assert.assertTrue("insert raw data error", quotes.size() == 0);

        ssrQuoteManagementService.insertNewQuotes(models);


        //@notice 总线未被初始化,无法后续测试

/*        quotes = ssrQuoteQueryService.retrieveQuoteByIDs(ids);
        Assert.assertTrue("insert data error", quotes.size() == 1);*/
    }

    @Test
    public void updateQuoteTest() {
        /*SSRQuoteModel model = getModel();
        String id = new String("4");
        model.setId(id);

        List<String> ids = new ArrayList<>();
        ids.add(id);
        List<SSRQuoteModel> quotes = ssrQuoteQueryService.retrieveQuoteByIDs(ids);
        Assert.assertTrue("update raw data error", quotes.size() == 1);

        ssrQuoteManagementService.updateQuote(model);

        quotes = ssrQuoteQueryService.retrieveQuoteByIDs(ids);
        Assert.assertTrue("update data error", quotes.size() == 1 && quotes.get(0).getProvinceCode() == "NJ");
  */  }

    @Test
    public void setQuoteStatusTest() {
        List<String> ids = new ArrayList<>();

        //ssrQuoteManagementService.setQuoteStatus();
    }

    @Test
    public void importToHistoryTest() {

    }

    @Test
    public void deleteQuoteByIdsTest() {

    }

    private SSRQuoteModel getModel() {
        SSRQuoteModel model = new SSRQuoteModel();
        model.setDirection(Direction.IN);
        model.setBillType(BABBillType.BKB);
        model.setBillMedium(BABBillMedium.ELE);
        model.setMemo("insertTest");
        model.setQuoteCompanyId("ff80818144fdf16701452b46e6627725");
        model.setContactId("ff8081814a03e02f014a08eb1eaa004e");
        model.setOperatorId("currentUser");
        model.setQuoteStatus(BABQuoteStatus.CAL);
        model.setProvinceCode("NJ01");
        model.setQuotePriceType(BABQuotePriceType.GG);
        model.setPrice(new BigDecimal(4.44));
        model.setAmount(new BigDecimal(55500000));

        return model;
    }
}
