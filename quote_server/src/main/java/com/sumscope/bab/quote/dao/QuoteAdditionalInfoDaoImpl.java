package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.quote.model.model.QuoteAdditionalInfoModel;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/19.
 * 接口实例类
 */
@Component
public class QuoteAdditionalInfoDaoImpl implements QuoteAdditionalInfoDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String DELETE_BY_QUOTEIDS = "com.sumscope.bab.quote.mapping.AdditionInfoMapper.deleteByQuoteIDs";
    private static final String DELETE_BY_QUOTEID = "com.sumscope.bab.quote.mapping.AdditionInfoMapper.deleteByQuoteID";
    private static final String INSERTS = "com.sumscope.bab.quote.mapping.AdditionInfoMapper.inserts";
    private static final String RETRIEVE_BY_IDS = "com.sumscope.bab.quote.mapping.AdditionInfoMapper.retrieveByIDs";
    private static final String IMPORT_HISTORY = "com.sumscope.bab.quote.mapping.AdditionInfoMapper.insertHistory";

    @Autowired
    @Qualifier(value = Constant.BUSINESS_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    @Qualifier(value = Constant.HISTORY_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate hisSqlSessionTemplate;

    @Override
    public void deleteAllInfoByQuoteId(List<String> quoteMainIds) {
        sqlSessionTemplate.delete(DELETE_BY_QUOTEIDS, quoteMainIds);
    }

    @Override
    public void insertNewInfos(List<QuoteAdditionalInfoModel> models) {
        if (models != null && !models.isEmpty()) {
            sqlSessionTemplate.insert(INSERTS, models);
        }
    }

    @Override
    public List<QuoteAdditionalInfoModel> retrieveAdditionalInfoByIds(List<String> ids) {
        LogStashFormatUtil.logDebug(logger,"retrieveAdditionalInfoByIds被调用，进行数据库查询。");
        return sqlSessionTemplate.selectList(RETRIEVE_BY_IDS, ids);
    }

    @Override
    public void importToHistory(QuoteAdditionalInfoModel model) {
        if(model != null){
            hisSqlSessionTemplate.insert(IMPORT_HISTORY, model);
        }
    }

    @Override
    public void deleteInfosFromHistoryByQuoteId(String id) {
        sqlSessionTemplate.delete(DELETE_BY_QUOTEID, id);
    }

    @Override
    public void flushDaoCache() {

    }
}
