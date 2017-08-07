package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;

import java.util.List;

/**
 * 承兑行机构Dao接口,本接口定义使用缓存，缓存应在系统启动时初始化。
 */
public interface AcceptingCompanyDao {
    /**
     * 获取所有当前承兑行列表
     */
    List<AcceptingCompanyModel> retrieveCompanies();

    /**
     * 根据ID列表获取对应数据
     */
    List<AcceptingCompanyModel> retrieveCompaniesByIds(List<String> ids);

    /**
     * 新增承兑行写入数据库
     */
    void insertNewAcceptingCompany(AcceptingCompanyModel model);

    /**
     * 更新数据库
     */
    void updateAcceptingCompany(AcceptingCompanyModel model);

    /**
     * 根据ID列表删除数据库数据
     */
    void deleteAcceptingCompanies(List<String> ids);


}
