package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.dao.QuoteDao;
import com.sumscope.bab.quote.dao.SSRQuoteDao;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SSRQuoteQueryServiceImpl extends AbstractQuoteQueryServiceImpl<SSRQuoteModel> implements SSRQuoteQueryService {

	@Autowired
	private SSRQuoteDao ssrQuoteDao;

	protected QuoteDao getQuoteMainDao() {
		return ssrQuoteDao;
	}

	@Override
	protected SSRQuoteModel getModelInstance() {
		return new SSRQuoteModel();
	}
}
