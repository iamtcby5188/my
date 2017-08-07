package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.model.model.SSCQuoteModel;
import org.springframework.stereotype.Component;

/**
 * Created by shaoxu.wang on 2016/12/13.
 * SSCDao实例
 */
@Component
public class SSCQuoteDaoImpl extends AbstractQuoteDaoImpl<SSCQuoteModel> implements SSCQuoteDao {
    private static final String RETRIEVE_COMPANY_IDS = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.retrieveCompanyIDs";
    private static final String RETRIEVE_COMPANY_NAMES = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.retrieveCompanyNames";
    private static final String RETRIEVE_BY_CONDITION = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.retrieveByCondition";
    private static final String RETRIEVE_BY_IDS = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.retrieveByIDs";
    private static final String INSERT = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.insert";
    private static final String UPDATE = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.update";
    private static final String UPDATE_STATUS = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.updateStatus";
    private static final String IMPORT_HISTORY = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.insertHistory";
    private static final String DELETE_BY_IDS = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.deleteByIDs";
    private static final String DELETE_HISTORY_BY_ID = "com.sumscope.bab.quote.mapping.SSCQuoteMapper.deleteHistoryByID";

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

    protected String getUpdateQuotesStatusString() {
        return UPDATE_STATUS;
    }

    protected String getImportToHistoryString() {
        return IMPORT_HISTORY;
    }

    protected String getDeleteQuoteByIdsString() {
        return DELETE_BY_IDS;
    }

    protected String getDeleteQuoteFromHistoryByIdString() {
        return DELETE_HISTORY_BY_ID;
    }

}
