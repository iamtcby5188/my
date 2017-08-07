package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.model.dto.QueryQuoteParameterDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

interface QuoteQueryFacade {

	/**
	 * 提供Web端根据输入机构名称模糊查询所有报价机构功能，针对直贴报价大厅
	 */
	void searchSSRQuoteCompanies(HttpServletRequest request, HttpServletResponse response, String companyName);

	/**
	 * 提供Web端根据输入机构名称模糊查询所有报价机构功能，针对全国直贴
	 */
	void searchSSCQuoteCompanies(HttpServletRequest request, HttpServletResponse response, String companyName);

	/**
	 * 提供Web端根据输入机构名称模糊查询所有报价机构功能，针对全国转贴
	 */
	void searchNPCQuoteCompanies(HttpServletRequest request, HttpServletResponse response, String companyName);

	/**
	 * 根据查询条件查询所有有效报价，默认按最后更新日期排序，针对直贴报价大厅。默认条件为已生效尚未过过期日期，并报价状态为已发布状态的报价。
	 */
	void searchSSRQuotes(HttpServletRequest request, HttpServletResponse response, QueryQuoteParameterDto parameterDto);

    /**
     * 根据查询条件查询所有有效报价，默认按最后更新日期排序，针对直贴报价大厅。默认条件为已生效尚未过过期日期，并报价状态为已发布状态的报价。
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param parameterDto
     */
    void searchSSRManagerQuotes(HttpServletRequest request, HttpServletResponse response, QueryQuoteParameterDto parameterDto);

	/**
	 * 根据查询条件查询所有有效报价，默认按最后更新日期排序，针对全国直贴。默认条件为已生效尚未过过期日期，并报价状态为已发布状态的报价。
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param parameterDto
	 */
	void searchSSCQuotes(HttpServletRequest request, HttpServletResponse response, QueryQuoteParameterDto parameterDto);

    /**
     * 根据查询条件查询所有报价(包括历史数据)，默认按最后更新日期排序，针对全国直贴。默认条件为已生效尚未过过期日期，并报价状态为已发布状态的报价。
     * 此方法用于即查询报价又返回价格走势图
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param parameterDto
     */
    void querySSCManagerQuotes(HttpServletRequest request, HttpServletResponse response, QueryQuoteParameterDto parameterDto);

	/**
	 * 根据查询条件查询所有有效报价，默认按最后更新日期排序，针对全国转贴。默认条件为已生效尚未过期，并报价状态为已发布状态的报价。
	 */
	void searchNPCQuotes(HttpServletRequest request, HttpServletResponse response, QueryQuoteParameterDto parameterDto);

    /**
     * 根据查询条件查询所有报价(包括历史数据)，默认按最后更新日期排序，针对全国转贴。默认条件为已生效尚未过期，并报价状态为已发布状态的报价。
     * 此方法用于即查询报价又返回价格走势图
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param parameterDto
     */
    void queryNPCManagerQuotes(HttpServletRequest request, HttpServletResponse response, QueryQuoteParameterDto parameterDto);

	/**
	 * 获取SSR报价单对应票据图片数据。如果ID不正确返回null
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param quoteId SSR报价单ID
	 */
	void getSSRImage(HttpServletRequest request, HttpServletResponse response, String quoteId);
}
