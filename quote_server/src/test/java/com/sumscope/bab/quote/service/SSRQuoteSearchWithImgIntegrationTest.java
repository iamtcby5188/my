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

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by fan.bai on 2017/2/3.
 * Test
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"}, config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"}, config = @SqlConfig(dataSource = Constant.HISTORY_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/ssr_init_data.sql"}, config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class SSRQuoteSearchWithImgIntegrationTest extends AbstractBabQuoteIntegrationTest {

    private static final String THISISTESTBASE = "THISISTESTBASE";

    @Autowired
    private SSRQuoteQueryService ssrQuoteQueryService;

    @Autowired
    private SSRQuoteManagementService ssrQuoteManagementService;

    @Test
    public void testGetQuoteWithImage() {
        List<SSRQuoteModel> ssrQuoteModels = ssrQuoteQueryService.retrieveQuoteByIDs(Collections.singletonList("4"));
        Assert.assertTrue("获取Base64图片数据失败！", ssrQuoteModels.get(0).getBase64Img().equals(THISISTESTBASE));
    }

    @Test
    public void testUpdateWithoutImg() {
        List<SSRQuoteModel> ssrQuoteModels = ssrQuoteQueryService.retrieveQuoteByIDs(Collections.singletonList("4"));
        SSRQuoteModel ssrQuoteModel = ssrQuoteModels.get(0);
        ssrQuoteModel.setBase64Img(null);
        ssrQuoteManagementService.updateQuote(ssrQuoteModel);

        List<SSRQuoteModel> ssrQuoteModelsNew = ssrQuoteQueryService.retrieveQuoteByIDs(Collections.singletonList("4"));
        SSRQuoteModel ssrQuoteModelNew = ssrQuoteModelsNew.get(0);
        Assert.assertTrue("图片数据为空时不应更新字段数据！", ssrQuoteModelNew.getBase64Img().equals(THISISTESTBASE));
    }

    @Test
    public void testUpdateWithImg() {
        List<SSRQuoteModel> ssrQuoteModels = ssrQuoteQueryService.retrieveQuoteByIDs(Collections.singletonList("3"));
        SSRQuoteModel ssrQuoteModel = ssrQuoteModels.get(0);
        String newS = "SOMETHINGNEW";
        ssrQuoteModel.setBase64Img(newS);
        ssrQuoteManagementService.updateQuote(ssrQuoteModel);

        List<SSRQuoteModel> ssrQuoteModelsNew = ssrQuoteQueryService.retrieveQuoteByIDs(Collections.singletonList("3"));
        SSRQuoteModel ssrQuoteModelNew = ssrQuoteModelsNew.get(0);
        Assert.assertTrue("图片数据更新失败！", ssrQuoteModelNew.getBase64Img().equals(newS));
    }

    @Test
    public void testInsertQuoteWithImage() {
        SSRQuoteModel model = new SSRQuoteModel();
        String base = "NEWBASE!";
        model.setBase64Img(base);
        model.setQuotePriceType(BABQuotePriceType.CS);
        model.setBillMedium(BABBillMedium.ELE);
        model.setBillType(BABBillType.BKB);
        model.setDirection(Direction.OUT);
        model.setQuoteStatus(BABQuoteStatus.DFT);
        model.setQuoteCompanyId("402880f034219aed0134219d57bb0171");
        model.setOperatorId("ff8081814a03e02f014a08eb1eaa004e");
        model.setContactId("ff8081814a03e02f014a08eb1eaa004e");
        model.setExpiredDate(new Date());

        List<String> strings = ssrQuoteManagementService.insertNewQuotes(Collections.singletonList(model));

        List<SSRQuoteModel> ssrQuoteModels = ssrQuoteQueryService.retrieveQuoteByIDs(strings);
        Assert.assertTrue("新增SSR报价单图片数据失败！", ssrQuoteModels.get(0).getBase64Img().equals(base));

    }
}
