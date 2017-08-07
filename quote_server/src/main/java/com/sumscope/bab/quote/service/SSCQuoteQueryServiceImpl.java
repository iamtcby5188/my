package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.dao.SSCQuoteDao;
import com.sumscope.bab.quote.model.model.SSCQuoteModel;
import com.sumscope.bab.quote.dao.QuoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SSCQuoteQueryServiceImpl extends AbstractQuoteQueryServiceImpl<SSCQuoteModel> implements SSCQuoteQueryService {

	@Autowired
	private SSCQuoteDao sscQuoteDao;

	@Override
	protected QuoteDao getQuoteMainDao() {
		return sscQuoteDao;
	}

	@Override
	protected SSCQuoteModel getModelInstance() {
		return new SSCQuoteModel();
	}
}
