package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.dao.NPCQuoteDao;
import com.sumscope.bab.quote.dao.QuoteDao;
import com.sumscope.bab.quote.model.model.NPCQuoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NPCQuoteQueryServiceImpl extends AbstractQuoteQueryServiceImpl<NPCQuoteModel> implements NPCQuoteQueryService {

	@Autowired
	private NPCQuoteDao npcQuoteDao;

	@Override
	protected QuoteDao getQuoteMainDao() {
		return npcQuoteDao;
	}

	@Override
	protected NPCQuoteModel getModelInstance() {
		return new NPCQuoteModel();
	}
}
