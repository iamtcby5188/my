package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsParameterModel;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;

/**
 * 提供对报价价格统计信息进行数据库读写功能的接口
 * 本接口使用应用程序默认缓存，因为不需要根据insert或者delete实时清空缓存。业务逻辑决定
 * 对于数据的更新只会每天一次且在非工作时间，因此查询缓存一定会失效。
 */
@CacheConfig(cacheNames={Constant.DEFAULT_CACHING_NAME})
public interface QuotePriceTrendsDao {

    /**
     * 删除某一个报价日的所有价格趋势数据
     *
     * @param quoteDate 报价日
     */
    void deleteAllPriceTrendsForDate(Date quoteDate);

    /**
     * 新增报价价格趋势数据列表写入数据库
     *
     * @param models 价格趋势数据列表
     */
    void insertPriceTrends(List<QuotePriceTrendsModel> models);

    /**
     * 根据条件读取历史报价价格统计信息
     */
    @Cacheable
    List<QuotePriceTrendsModel> searchPriceTrends(QuotePriceTrendsParameterModel parameterModel);

}
