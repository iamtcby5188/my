package com.sumscope.bab.quote.facade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AcceptingCompanyFacade {

	/**
	 * 从本地库中搜索所有符合名称或拼音的的承兑行
	 */
	void searchAcceptingCompaniesByNameOrPY(HttpServletRequest request, HttpServletResponse response, String name);

    /**
     * 从黄页系统获取对应的承兑行
     */
    void searchExternalYellowPageCompaniesByName(HttpServletRequest request, HttpServletResponse response, String name);
}
