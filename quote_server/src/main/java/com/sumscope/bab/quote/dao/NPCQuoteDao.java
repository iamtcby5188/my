package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.model.model.NPCQuoteModel;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;


/**
 * NPC报价Dao,本接口定义使用缓存，缓存应在系统启动时初始化。
 */
@CacheConfig(cacheNames={Constant.NPC_QUOTE_DAO_CACHE})
public interface NPCQuoteDao extends QuoteDao<NPCQuoteModel> {
    @Override
    @Cacheable
    List<String> retrieveCurrentQuotesCompanyIds();

    @Override
    @Cacheable
    List<String> retrieveCurrentQuotesCompanyNames();

    @Override
    @Cacheable
    List<NPCQuoteModel> retrieveQuotesByCondition(QueryQuotesParameterModel parameter);

    @Override
    @Cacheable
    List<NPCQuoteModel> retrieveQuoteByIDs(List<String> ids);

    @Override
    @CacheEvict(allEntries = true)
    void insertNewQuotes(List<NPCQuoteModel> models);

    @Override
    @CacheEvict(allEntries = true)
    void updateOneQuote(NPCQuoteModel model);

    @Override
    @CacheEvict(allEntries = true)
    void updateQuotesStatus(List<String> ids, BABQuoteStatus status, Date updateTime);

    @Override
    void importToHistory(AbstractQuoteModel model);

    @Override
    @CacheEvict(allEntries = true)
    void deleteQuoteByIds(List<String> ids);

    @Override
    void deleteQuoteFromHistoryById(String id);

    @Override
    //从历史数据库读取数据可以不考虑写入造成的缓存失效问题，可以使用默认缓存区域，其每小数会失效一次。
    @Cacheable(Constant.DEFAULT_CACHING_NAME)
    List<NPCQuoteModel> retrieveHistoryQuotesByCondition(QueryQuotesParameterModel parameter);

    @Override
    @CacheEvict(allEntries = true)
    void flushDaoCache();
}
