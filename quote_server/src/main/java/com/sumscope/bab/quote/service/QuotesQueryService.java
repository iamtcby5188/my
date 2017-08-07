package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;

import java.util.List;

/**
 * 报价查询抽象接口，该接口定义了查询的实际方法。方法返回值由泛型定义。各子类根据泛型约束定义了实际的方法返回值。
 */
public interface QuotesQueryService<T extends AbstractQuoteModel> {

	/**
	 * 获取所有当前报价单对应的报价机构ID列表
	 * @return 对应机构ID列表
	 */
	List<String> retrieveCurrentQuotesCompanies();

	/**
	 * 获取所有当前报价单对应的报价机构名称列表
	 * @return 对应机构名称列表
	 */
	List<String> retrieveCurrentQuotesCompaniesNames();

	/**
	 * 根据参数查询报价单
	 * @param parameter 参数
	 * @return 报价单数据模型
	 */
	List<T> retrieveQuotesByCondition(QueryQuotesParameterModel parameter);

	/**
	 * 根据参数查询报价单
	 * @param parameter 参数
	 * @return 报价单数据模型
	 */
	List<T> retrieveHistoryQuotesByCondition(QueryQuotesParameterModel parameter);

	List<T> retrieveQuoteByIDs(List<String> ids);

	void flushCache();
}
