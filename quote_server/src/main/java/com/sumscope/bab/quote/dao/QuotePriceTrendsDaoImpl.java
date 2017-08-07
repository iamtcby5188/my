package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsParameterModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fan.bai on 2016/12/9.
 * 接口实现类
 */
@Component
public class QuotePriceTrendsDaoImpl implements QuotePriceTrendsDao {
    private static final String SEARCH_PRICE_TRENDS = "com.sumscope.bab.quote.mapping.QuotePriceTrendsMapper.SearchPriceTrendsByConditions";
    private static final String DELETE_PRICE_TRENDS_BY_DATE = "com.sumscope.bab.quote.mapping.QuotePriceTrendsMapper.DeletePriceTrendsByDate";
    private static final String INSERT_PRICE_TRENDS = "com.sumscope.bab.quote.mapping.QuotePriceTrendsMapper.InsertPriceTrends";

    @Autowired
    @Qualifier(value = Constant.BUSINESS_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void deleteAllPriceTrendsForDate(Date quoteDate) {
        Date beginningDate = QuoteDateUtils.getBeginingTimeOfDate(quoteDate);
        Date latestDate = QuoteDateUtils.getLatestTimeOfDate(quoteDate);
        Map<String,Date> map = new HashMap<>();
        map.put("beginingDate",beginningDate);
        map.put("latestDate",latestDate);
        sqlSessionTemplate.delete(DELETE_PRICE_TRENDS_BY_DATE,map);

    }

    @Override
    public void insertPriceTrends(List<QuotePriceTrendsModel> models) {
        sqlSessionTemplate.insert(INSERT_PRICE_TRENDS,models);

    }

    @Override
    public List<QuotePriceTrendsModel> searchPriceTrends(QuotePriceTrendsParameterModel parameterModel) {
        return sqlSessionTemplate.selectList(SEARCH_PRICE_TRENDS, parameterModel);
    }
}
