package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.enums.EditMode;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;

import java.util.List;


/**
 * 为了支持AcceptingCompanyService中对于名称和拼音方式查询的快速响应，我们使用一次性从数据库中读取所有机构并缓存，
 * 再根据名称或名称拼音依次搜索的方式查询数据。这种方式比从数据库中按名称或拼音进行模糊查询的方式要快的多。代价是占用一定内存。
 * 如果本地缓存的机构在3万以下，这种方式的查询可以提供低于10毫秒的响应时间。
 * 本类既缓存类，缓存在一定时间，比如2小时内，有效。过期通过Dao读取全部数据。在承兑行机构新增或修改删除时会向本地总线发送消息，
 * 消息监听器将调用更新缓存命令，这样在2小时内即时发生了本地数据库修改也无需重新读取数据。
 * 
 */
public interface AcceptingCompanyInterCacheService {

	/**
	 * 获取所有承兑行列表。该方法使用缓存。获取的数据作为参数进行按名称模糊查询
	 */
	List<AcceptingCompanyModel> retrieveCompanies();
	/**
	 * 当承兑行机构发生本地新增或修改时，监听器将调用本方法进行缓存更新。
	 */
	void updateCache(AcceptingCompanyModel model, EditMode editMode);

}
