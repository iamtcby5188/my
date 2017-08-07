package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.FlushCacheEnum;
import com.sumscope.bab.quote.dao.NPCQuoteDao;
import com.sumscope.bab.quote.dao.QuoteDao;
import com.sumscope.bab.quote.model.model.NPCQuoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NPCQuoteManagementServiceImpl extends AbstractQuoteManagementServiceImpl<NPCQuoteModel> implements NPCQuoteManagementService,NPCQuoteManagementTransactionalService {
    @Autowired
    private NPCQuoteDao npcQuoteDao;

    @Override
    protected QuoteDao getQuoteMainDao() {
        return npcQuoteDao;
    }

    @Autowired
    protected String concreteQuoteModelClassName() {
        return NPCQuoteModel.class.getSimpleName();
    }

    @Override
    protected FlushCacheEnum getFluchCacheTarget() {
        return FlushCacheEnum.NPC_QUOTE_FLUSH;
    }
}
