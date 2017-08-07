package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.model.dto.AnalysisParameterDto;
import com.sumscope.bab.quote.model.dto.DatePeriodDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 利差分析页面Facade接口，只有注册的金融类企业（拥有BAB.QUOTE.NPC.MANAGEMENT权限）可以获取这个接口提供的数据。
 */
public interface PriceMarginAnalysisFacade {

	/**
	 * 获取初始化数据。
	 */
	void getInitResponse(HttpServletRequest request, HttpServletResponse response, AnalysisParameterDto analysisParameterDto);

	/**
	 *获取价差分析的页面的直转利差，直贴价格和转贴价格  且供web端每五分钟调用一次
	 */
	void getInitAnalysisPrice(HttpServletRequest request, HttpServletResponse response, AnalysisParameterDto analysisParameterDto);

	/**
	 * 获取直转贴价格走势数据，当用户更改了直转贴走势图的日期拖动条时触发。
	 */
	void getPriceTrendsHistory(HttpServletRequest request, HttpServletResponse response, DatePeriodDto datePeriod);

	/**
	 * 获取公开市场净投放走势数据，当用户更改了走势图的日期拖动条时触发。
	 */
	void getNetVolumeHistory(HttpServletRequest request, HttpServletResponse response, DatePeriodDto datePeriod);

}
