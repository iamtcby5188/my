package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * Created by Administrator on 2016/12/28.
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.HISTORY_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/ssr_Timer_data.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class QuoteApplicationTimerServiceUnitTest extends AbstractBabQuoteIntegrationTest {

    @Autowired
    private SSRQuoteQueryService SSRQuoteQueryService;

    @Autowired
    private SSRQuoteManagementService SSRQuoteManagementService;

    @Autowired
    private SSCQuoteQueryService SSCQuoteQueryService;

    @Autowired
    private SSCQuoteManagementService SSCQuoteManagementService;

    @Autowired
    private NPCQuoteQueryService NPCQuoteQueryService;

    @Autowired
    private NPCQuoteManagementService NPCQuoteManagementService;


    @Test
    public void invokeQuotesAchieveService() {
        /*QueryQuotesParameterModel queryQuotesParameterModel=new QueryQuotesParameterModel();
        queryQuotesParameterModel.setExpiredQuotesDate(new Date());
        queryQuotesParameterModel.setPaging(false);

        //SSR数据导出
        List<SSRQuoteModel> ssrQuoteModels = SSRQuoteQueryService.retrieveQuotesByCondition(queryQuotesParameterModel);
        if(ssrQuoteModels!=null && ssrQuoteModels.size()>0){
            for(SSRQuoteModel ssrQuoteModel:ssrQuoteModels){
                SSRQuoteManagementService.importToHistory(ssrQuoteModel);
            }
        }

        //SSC数据导出
        List<SSCQuoteModel> sscQuoteModels = SSCQuoteQueryService.retrieveQuotesByCondition(queryQuotesParameterModel);
        if(sscQuoteModels!=null && sscQuoteModels.size()>0){
            for(SSCQuoteModel sscQuoteModel:sscQuoteModels){
                SSCQuoteManagementService.importToHistory(sscQuoteModel);
            }
        }

        //NPC数据导出
        List<NPCQuoteModel> npcQuoteModels = NPCQuoteQueryService.retrieveQuotesByCondition(queryQuotesParameterModel);
        if(npcQuoteModels!=null && npcQuoteModels.size()>0){
            for(NPCQuoteModel npcQuoteModel:npcQuoteModels){
                NPCQuoteManagementService.importToHistory(npcQuoteModel);
            }
        }*/
    }



}
