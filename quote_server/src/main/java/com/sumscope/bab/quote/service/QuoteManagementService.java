package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;

import java.util.List;

/**
 * 报价单更新服务，该服务仅用于service层的相互调用，且不做事务控制
 */
interface QuoteManagementService<T extends AbstractQuoteModel> {

	/**
	 * 批量新增新报价。报价单新增后的状态为“草案”。新增功能将对创建日期，最后更新日期，生效日期（如果未设置）过期日期（如果未设置），报价单状态等字段进行设置。
     * 需要发布变更至总线
	 */
	List<String> insertNewQuotes(List<T> models);

	/**
	 * 更新某一张报价单。该功能将先读取现存数据并比较，对于没有变化的报价单将不做处理。如果报价单有变化则更新报价单并设置最后更新日期。
     * 需要发布变更至总线
	 */
	T updateQuote(T model);

	/**
	 * 对某一组报价单设置状态。状态设置完成后将返回新的数据。该数据需要发布到系统总线上。
	 */
	void setQuoteStatus(List<String> idList, BABQuoteStatus quoteStatus);


	/**
	 * 将数据导入历史数据库
	 * @param model
	 */
	void importToHistory(T model);

	/**
	 * 根据ID列表从业务数据库物理删除报价单，包括从属数据。
	 * 需要发布变更至总线
	 */
	void deleteQuoteByIds(List<String> ids);

//	TODO：增加insertQuotesInTransaction, updateQuoteInTransaction,deleteQuotesByIdsInTransaction，setQuoteStatusInTransaction方法
//	TODO：这些方法都标注为事务控制，并由facade层进行调用，替换现有的没有事务控制的方法。


}
