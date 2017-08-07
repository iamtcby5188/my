package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.commons.enums.Direction;
import com.sumscope.bab.quote.commons.model.AmountWrapper;
import com.sumscope.bab.quote.commons.model.DueDateWrapper;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/26.
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.HISTORY_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/ssr_init_data.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class SSRQuoteQueryServiceUnitTest extends AbstractBabQuoteIntegrationTest {

    @Autowired
    private SSRQuoteQueryService service;

    @Test
    public void retrieveQuotesByConditionTest() {
        QueryQuotesParameterModel parameterModel = new QueryQuotesParameterModel();
        parameterModel.setBillType(BABBillType.BKB);
        parameterModel.setDirection(Direction.IN);
        parameterModel.setBillMedium(BABBillMedium.PAP);

        List<AmountWrapper> amountWrappers = new ArrayList<>();
        AmountWrapper amountWrapper = new AmountWrapper();
        amountWrapper.setAmountLow(new BigDecimal(0));
        amountWrapper.setAmountHigh(new BigDecimal(100000000));
        amountWrappers.add(amountWrapper);
        parameterModel.setAmountList(amountWrappers);

        List<DueDateWrapper> dueDateWrappers = new ArrayList<>();
        DueDateWrapper dueDateWrapper = new DueDateWrapper();
        dueDateWrapper.setDueDateBegin(new Date(2016-1900, 1, 1));
        dueDateWrapper.setDueDateEnd(new Date(2018-1900, 1, 1));
        dueDateWrappers.add(dueDateWrapper);
        parameterModel.setDueDateWrapperList(dueDateWrappers);

        List<String> proCodes = new ArrayList<>();
        proCodes.add("SH01");
        proCodes.add("GD01");
        parameterModel.setProvinceCodes(proCodes);

        parameterModel.setOrderByPriceType(null);
        parameterModel.setOderSeq(null);

        parameterModel.setPaging(true);
        parameterModel.setPageNumber(0);
        parameterModel.setPageSize(50);

        parameterModel.setEffectiveQuotesDate(new Date(2016-1900, 12, 26));
        parameterModel.setExpiredQuotesDate(new Date(2017-1900, 12, 12));

        List<BABQuoteStatus> statuses = new ArrayList<>();
        statuses.add(BABQuoteStatus.DFT);
        statuses.add(BABQuoteStatus.DSB);
        parameterModel.setQuoteStatusList(statuses);

        parameterModel.setTradeType(null);
        parameterModel.setMinor(false);

        List<SSRQuoteModel> quoteModels = service.retrieveQuotesByCondition(parameterModel);
        if (quoteModels.isEmpty()) {
            return;
        }

        //Assert.assertTrue("retrieveQuotesByCondition error", quoteModels.size() == 3);
    }

    @Test
    public void retrieveCurrentQuotesCompaniesTest() {
        List<String> ids = service.retrieveCurrentQuotesCompanies();
        Assert.assertTrue("retrieveCurrentQuotesCompanies error", ids.size() == 2);
    }
}
