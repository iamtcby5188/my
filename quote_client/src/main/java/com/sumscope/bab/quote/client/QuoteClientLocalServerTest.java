package com.sumscope.bab.quote.client;

import com.sumscope.bab.quote.commons.enums.*;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.dto.QuoteAdditionalInfoDto;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by fan.bai on 2017/2/23.
 * 本类用于在本地测试客户端接口.本类不能作为单元测试自动自行,仅提供由手工方式触发的测试.
 */
public class QuoteClientLocalServerTest {
    public static void main(String[] args) throws ExternalInvocationFailedException {
        BabQuoteHttpClient client = new BabQuoteHttpClient("localhost:8888");
        List<QuotePriceTrendsDto> quotePriceTrendsDtos = client.retrieveCurrentSSRPriceTrends();
        System.out.println("*************************************");
        System.out.println(quotePriceTrendsDtos);

        SSRQuoteDto quote = new SSRQuoteDto();
        quote.setQuotePriceType(BABQuotePriceType.CS);
        quote.setQuoteStatus(BABQuoteStatus.DSB);
        quote.setAmount(new BigDecimal(10000.29));
        quote.setPrice("2.59");
        quote.setBillMedium(BABBillMedium.ELE);
        quote.setBillType(BABBillType.BKB);
        quote.setDirection(Direction.IN);
        quote.setMemo("Created By Client Test!");
        quote.setAdditionalInfo(new QuoteAdditionalInfoDto());
        quote.setContainsAdditionalInfo(true);
        quote.setOperatorId("1c497085d6d511e48ec3000c29a13c19");//roomtest185
        quote.getAdditionalInfo().setAcceptingHouseName("客户端测试商户");
        quote.getAdditionalInfo().setCompanyType(BABAcceptingCompanyType.CET);
        quote.getAdditionalInfo().setContactName("客户端测试联系人");
        quote.getAdditionalInfo().setContactTelephone("112233");
        quote.getAdditionalInfo().setQuoteCompanyName("客户端测试机构");
        client.insertSSRQuotes(Collections.singletonList(quote));
    }
}
