package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.model.model.QuoteAdditionalInfoModel;
import com.sumscope.bab.quote.commons.Constant;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 附加信息数据Dao接口,本接口定义使用缓存，缓存应在系统启动时初始化。
 */
@CacheConfig(cacheNames = {Constant.QUOTE_ADDITION_INFO_DAO_CACHE})
public interface QuoteAdditionalInfoDao {

    /**
     * 在业务数据库根据报价单ID删除所有业务数据库附加表中所有所属数据
     */
    @CacheEvict(allEntries = true)
    void deleteAllInfoByQuoteId(List<String> quoteMainIds);

    /**
     * 在业务数据库插入新的附加信息
     */
    @CacheEvict(allEntries = true)
    void insertNewInfos(List<QuoteAdditionalInfoModel> models);

    /**
     * 在业务数据库根据报价单ID列表获取所属附加信息表
     */
    @Cacheable
    List<QuoteAdditionalInfoModel> retrieveAdditionalInfoByIds(List<String> ids);

    /**
     * 导入数据至历史库
     *
     * @param model 需要导入的报价单数据
     */
    void importToHistory(QuoteAdditionalInfoModel model);

    /**
     * 从历史数据库根据报价单ID删除所属info数据
     */
    @CacheEvict(allEntries = true)
    void deleteInfosFromHistoryByQuoteId(String id);

    /**
     * 缓存刷新
     */
    @CacheEvict(allEntries = true)
    void flushDaoCache();
}
