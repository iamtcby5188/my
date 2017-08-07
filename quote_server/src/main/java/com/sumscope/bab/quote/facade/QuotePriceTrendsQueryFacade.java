package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.model.dto.QuotePriceTrendsParameterDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 报价价格统计相关Facade接口。
 */
public interface QuotePriceTrendsQueryFacade {

	/**
	 * 根据票据报价类型获取当前价格统计信息
	 * quoteType可取范围为SSC，NPC。
	 */
	void searchCurrentPriceTrendCalculation(HttpServletRequest request, HttpServletResponse response, QuotePriceTrendsParameterDto parameterDto);

	/**
	 * 根据报价类型和日期范围搜索所有历史报价价格统计信息
	 */
	void searchPriceTrends(HttpServletRequest request, HttpServletResponse response, QuotePriceTrendsParameterDto parameterDto);

}
