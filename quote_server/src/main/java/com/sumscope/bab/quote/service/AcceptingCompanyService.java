package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;

import java.util.List;

public interface AcceptingCompanyService {

	/**
	 * 从参数的所有承兑行按迭代方式进行名称模糊查询
	 */
	List<AcceptingCompanyModel> searchCompaniesByNameOrPY(String name);

	/**
	 * 新增承兑行
	 */
	void insertNewAcceptingCompany(AcceptingCompanyModel model);

	/**
	 * 更新承兑行
	 */
	void updateAcceptingCompany(AcceptingCompanyModel model);

	/**
	 * 删除前先读取所有当前数据，在调用Dao一次性删除，最后将删除信息发布到总线上
	 */
	void deleteAcceptingCompanies(List<String> ids);

	/**
	 * 根据ID获取对应机构，使用缓存
	 */
	AcceptingCompanyModel getCompanyById(String id);

	void updateInfos(List<AcceptingCompanyModel> models);
}
