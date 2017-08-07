package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/20.
 */
@Component
public class AcceptingCompanyDaoImpl implements AcceptingCompanyDao {
    private static final String RETRIEVE_ALL = "com.sumscope.bab.quote.mapping.AcceptingCompanyMapper.retrieveAll";
    private static final String RETRIEVE_BY_IDS = "com.sumscope.bab.quote.mapping.AcceptingCompanyMapper.retrieveByIDs";
    private static final String INSERT = "com.sumscope.bab.quote.mapping.AcceptingCompanyMapper.insert";
    private static final String UPDATE = "com.sumscope.bab.quote.mapping.AcceptingCompanyMapper.update";
    private static final String DELETES = "com.sumscope.bab.quote.mapping.AcceptingCompanyMapper.deletes";

    @Autowired
    @Qualifier(value = Constant.BUSINESS_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<AcceptingCompanyModel> retrieveCompanies() {
        return sqlSessionTemplate.selectList(RETRIEVE_ALL);
    }

    @Override
    public List<AcceptingCompanyModel> retrieveCompaniesByIds(List<String> ids) {
        return sqlSessionTemplate.selectList(RETRIEVE_BY_IDS, ids);
    }

    @Override
    public void insertNewAcceptingCompany(AcceptingCompanyModel model) {
        sqlSessionTemplate.insert(INSERT, model);
    }

    @Override
    public void updateAcceptingCompany(AcceptingCompanyModel model) {
        sqlSessionTemplate.update(UPDATE, model);
    }

    @Override
    public void deleteAcceptingCompanies(List<String> ids) {
        sqlSessionTemplate.delete(DELETES, ids);
    }

}
