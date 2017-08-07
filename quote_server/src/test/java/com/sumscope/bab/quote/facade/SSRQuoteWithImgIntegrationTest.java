package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.model.dto.SSRQuoteImgDto;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * Created by fan.bai on 2017/2/3.
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"}, config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"}, config = @SqlConfig(dataSource = Constant.HISTORY_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/ssr_init_data.sql"}, config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class SSRQuoteWithImgIntegrationTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private QuoteQueryFacade quoteQueryFacade;

    private IAMEntitlementCheck iamEntitlementCheck = mock(IAMEntitlementCheck.class);

    @Test
    public void testGetQuoteWithImg(){
        QuoteQueryFacadeImpl facade = (QuoteQueryFacadeImpl)quoteQueryFacade;
        SSRQuoteImgDto ssrQuoteImgDto = facade.doGetSSRImage("4");
        System.out.print("sdfsdf");

    }
}
