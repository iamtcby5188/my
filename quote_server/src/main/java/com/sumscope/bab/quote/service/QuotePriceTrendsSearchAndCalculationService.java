package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsParameterModel;

import java.util.Date;
import java.util.List;

/**
 * 根据当前有效有效报价单进行价格统计信息计算与结果存储的的服务
 */
public interface QuotePriceTrendsSearchAndCalculationService {

	/**
	 * 根据报价日期计算当前价格统计信息。进行计算的报价单必须是仍有效且价格在合理范围内的报价单。
	 * 该方法不做数据缓存，将执行相对较长时间，因此不建议由Facade层直接调用
	 * 
	 */
	List<QuotePriceTrendsModel> calculateCurrentPriceTrends(Date quoteDate);

	/**
	 * 将计算得出的报价统计信息写入数据库。
     * 先根据日期删除重复数据，再新增当前结果，以支持一天多次生成报价单
     * 本方法处于事务控制
	 */
	void persistentPriceTrends(Date calculationDate,List<QuotePriceTrendsModel> trends);

	/**
	 * 根据条件查询所有写入数据库的价格统计信息。
	 */
	List<QuotePriceTrendsModel> searchPriceTrends(QuotePriceTrendsParameterModel parameter);

}
