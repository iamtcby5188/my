package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.FlushCacheEnum;
import com.sumscope.bab.quote.dao.SSCQuoteDao;
import com.sumscope.bab.quote.dao.QuoteDao;
import com.sumscope.bab.quote.model.model.SSCQuoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SSCQuoteManagementServiceImpl extends AbstractQuoteManagementServiceImpl<SSCQuoteModel> implements SSCQuoteManagementService,SSCQuoteManagementTransactionalService {
    @Autowired
    private SSCQuoteDao sscQuoteDao;


    @Override
    protected QuoteDao getQuoteMainDao() {
        return sscQuoteDao;
    }

    @Autowired
    protected String concreteQuoteModelClassName() {
        return SSCQuoteModel.class.getSimpleName();
    }

    @Override
    protected FlushCacheEnum getFluchCacheTarget() {
        return FlushCacheEnum.SSC_QUOTE_FLUSH;
    }
}
