package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import org.springframework.stereotype.Component;

/**
 * Created by shaoxu.wang on 2016/12/15.
 * SSR Dao实例
 */
@Component
public class SSRQuoteDaoImpl extends AbstractQuoteDaoImpl<SSRQuoteModel> implements SSRQuoteDao {
    private static final String RETRIEVE_COMPANY_IDS = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.retrieveCompanyIDs";
    private static final String RETRIEVE_COMPANY_NAMES = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.retrieveCompanyNames";
    private static final String RETRIEVE_BY_CONDITION = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.retrieveByCondition";
    private static final String RETRIEVE_BY_IDS = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.retrieveByIDs";
    private static final String INSERT = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.insert";
    private static final String UPDATE = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.update";
    private static final String UPDATE_STATUS = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.updateStatus";
    private static final String IMPORT_HISTORY = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.insertHistory";
    private static final String DELETE_BY_IDS = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.deleteByIDs";
    private static final String DELETE_HISTORY_BY_ID = "com.sumscope.bab.quote.mapping.SSRQuoteMapper.deleteHistoryByID";

    @Override
    protected String getRetrieveCurrentQuotesCompanyNamesString() {
        return RETRIEVE_COMPANY_NAMES;
    }

    @Override
    protected String getRetrieveCurrentQuotesCompanyIdsString() {
        return RETRIEVE_COMPANY_IDS;
    }
    @Override
    protected String getRetrieveQuotesByConditionsString() {
        return RETRIEVE_BY_CONDITION;
    }


    @Override
    protected String getRetrieveQuoteByIDsString() {
        return RETRIEVE_BY_IDS;
    }

    @Override
    protected String getInsertNewQuotesString() {
        return INSERT;
    }

    @Override
    protected String getUpdateOneQuoteString() {
        return UPDATE;
    }

    @Override
    protected String getUpdateQuotesStatusString() {
        return UPDATE_STATUS;
    }


    @Override
    protected String getImportToHistoryString() {
        return IMPORT_HISTORY;
    }

    @Override
    protected String getDeleteQuoteByIdsString() {
        return DELETE_BY_IDS;
    }

    @Override
    protected String getDeleteQuoteFromHistoryByIdString() {
        return DELETE_HISTORY_BY_ID;
    }


}
