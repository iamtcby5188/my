package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.model.model.PriceMarginModel;

import java.util.List;
import java.util.Map;

public interface PriceMarginAnalysisService {

	List<PriceMarginModel> calculatePriceMargin(BABBillMedium billMedium, List<QuotePriceTrendsModel> currentPriceTrends);

	Map getAndCalculatePriceMargins(BABBillMedium billMedium);

}
