package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/28.
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/quote/accepting_company_init_data.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class AcceptingCompanyServiceUnitTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private AcceptingCompanyService acceptingCompanyService;

    @Autowired
    @Qualifier(Constant.DEFAULT_CACHING_NAME)
    private CacheManager appCacheManager;

    @Test
    public void searchCompaniesByNameOrPYTest() {
        //删除缓存，否则可能受到其他测试影响
        Cache cache = appCacheManager.getCache(Constant.DEFAULT_CACHING_NAME);
        cache.evict("AcceptingCompanyInterCacheServiceImpl.retrieveCompanies");
        List<AcceptingCompanyModel> models = acceptingCompanyService.searchCompaniesByNameOrPY("zgy");
        Assert.assertTrue("searchCompaniesByNameOrPYTest error, 无法找到对应机构:" + models.size(),models.size() == 1);
    }

    @Test
    public void insertNewAcceptingCompanyTest() {
        String id = new String("10007");
        AcceptingCompanyModel companyModel = getCompanyModel();
        companyModel.setId("10007");

        AcceptingCompanyModel oldCompanyModel = acceptingCompanyService.getCompanyById(id);
        Assert.assertTrue("insert raw data error", oldCompanyModel == null);

        acceptingCompanyService.insertNewAcceptingCompany(companyModel);

        oldCompanyModel = acceptingCompanyService.getCompanyById(id);
        Assert.assertTrue("insertNewAcceptingCompany error", oldCompanyModel != null);
    }

    @Test
    public void updateAcceptingCompanyTest() {
/*        String id = new String("Um4wQms3NXRPR1EwU2RlaGF5d3Evdz09");
        AcceptingCompanyModel companyModel = getCompanyModel();
        companyModel.setId(id);

        AcceptingCompanyModel oldCompanyModel = acceptingCompanyService.getCompanyById(id);
        Assert.assertTrue("update raw data error", oldCompanyModel.getLastSynDateTime().getTime() == 1483200000000l);

        acceptingCompanyService.updateAcceptingCompany(companyModel);

        oldCompanyModel = acceptingCompanyService.getCompanyById(id);
        Assert.assertTrue("updateAcceptingCompany error", oldCompanyModel.getLastSynDateTime().getTime() > 1483200000000l);*/
    }

    @Test
    public void deleteAcceptingCompaniesTest() {
/*        String id = new String("UGdpT2pIbnhZOXYzVHorRTdEVzlLQT09");

        AcceptingCompanyModel companyModel = acceptingCompanyService.getCompanyById(id);
        Assert.assertTrue("delete raw data error", companyModel != null);

        List<String> ids = new ArrayList<>();
        ids.add(id);
        acceptingCompanyService.deleteAcceptingCompanies(ids);

        companyModel = acceptingCompanyService.getCompanyById(id);
        Assert.assertTrue("delete AcceptingCompanies error", companyModel == null);*/
    }

    private AcceptingCompanyModel getCompanyModel() {
        AcceptingCompanyModel companyModel = new AcceptingCompanyModel();
        companyModel.setCompanyType(BABAcceptingCompanyType.CET);
        companyModel.setIamCompanyID("companyID7");
        companyModel.setCompanyName("companyName7");
        companyModel.setAddress("Shenzhen");
        companyModel.setManager("manager7");
        companyModel.setRegistrationNumber("7");
        companyModel.setCompanyNamePY("PY7");
        companyModel.setCompanyNamePinYin("PinYin7");

        return companyModel;
    }
}
