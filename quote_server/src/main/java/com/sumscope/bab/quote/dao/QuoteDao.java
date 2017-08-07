package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;

import java.util.Date;
import java.util.List;

/**
 * 针对bab_quote_main表进行增删改查的Dao接口。该Dao层使用二级缓存
 */
public interface QuoteDao<T extends AbstractQuoteModel> {

	/**
	 * 获取当前所有有效报价单所包含的机构ID列表。既所有拥有有效报价的机构
	 */
	List<String> retrieveCurrentQuotesCompanyIds();
	/**
	 * 获取当前所有有效报价单所包含的机构名称列表。既所有拥有有效报价的机构
	 */
	List<String> retrieveCurrentQuotesCompanyNames();
	/**
	 * 根据参数查询报价单
	 * @param parameter 参数
	 * @return 结果
	 */
	List<T> retrieveQuotesByCondition(QueryQuotesParameterModel parameter);

	/**
	 * 根据参数查询报价单
	 * @param parameter 参数
	 * @return 结果
	 */
	List<T> retrieveHistoryQuotesByCondition(QueryQuotesParameterModel parameter);

	/**
	 * 根据ID列表查询对应报价单
	 * @param ids 报价单ID列表
	 * @return 对应报价单
	 */
	List<T> retrieveQuoteByIDs(List<String> ids);

	/**
	 * 将新增报价单数据列表写入数据库
	 * @param models 新增报价单列表
	 */
	void insertNewQuotes(List<T> models);

	/**
	 * 更新一个报价单
	 * @param model 报价单
	 */
	void updateOneQuote(T model);

	/**
	 * 更新多个报价单状态
	 * @param ids 报价单ids
	 * @param status 状态
	 * @param updateTime 更新时间
	 */
	void updateQuotesStatus(List<String> ids, BABQuoteStatus status, Date updateTime);

	/**
	 * 将数据导入历史库。与insertNewQuotes实现一样的功能，只是使用历史库的链接。
	 */
	void importToHistory(AbstractQuoteModel model);

	/**
	 * 根据ID列表从业务数据库物理删除对应Quote表数据。
	 */
	void deleteQuoteByIds(List<String> ids);

	/**
	 * 根据ID从历史数据库删除对应报价单数据
	 */
	void deleteQuoteFromHistoryById(String id);

	/**
	 * 刷新缓存
	 */
	void flushDaoCache();
}
