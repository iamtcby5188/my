package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.FlushCacheEnum;
import com.sumscope.bab.quote.dao.QuoteDao;
import com.sumscope.bab.quote.dao.SSRQuoteDao;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SSRQuoteManagementServiceImpl extends AbstractQuoteManagementServiceImpl<SSRQuoteModel> implements SSRQuoteManagementService,SSRQuoteManagementTransactionalService {
    @Autowired
    private SSRQuoteDao ssrQuoteDao;

    @Override
    protected QuoteDao getQuoteMainDao() {
        return ssrQuoteDao;
    }

    @Autowired
    protected String concreteQuoteModelClassName() {
        return SSRQuoteModel.class.getSimpleName();
    }

    @Override
    protected FlushCacheEnum getFluchCacheTarget() {
        return FlushCacheEnum.SSR_QUOTE_FLUSH;
    }

}
