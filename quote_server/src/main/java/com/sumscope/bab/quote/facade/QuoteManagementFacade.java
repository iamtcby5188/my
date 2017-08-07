package com.sumscope.bab.quote.facade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import com.sumscope.bab.quote.model.dto.NPCQuoteDto;
import com.sumscope.bab.quote.model.dto.SSCQuoteDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.quote.model.dto.QuoteStatusChangeDto;

public interface QuoteManagementFacade {

	/**
	 *  批量插入一批新报价。 
	 * 
	 */
	void insertNewSSRQuotes(HttpServletRequest request, HttpServletResponse response, List<SSRQuoteDto> dtos);

	/**
	 *  用户根据当前报价进行报价修改。调用该方法前提是报价单已存在并且客户端拥有报价单ID。 
	 * 
	 */
	void updateOneSSRQuote(HttpServletRequest request, HttpServletResponse response, SSRQuoteDto dto);

	/**
	 *  设置报价单状态，调用该方法前提是报价单已存在并且客户端拥有报价单ID。 
	 *  未对单号进行校验,若为非法ID则不会起作用 
	 * 
	 */
	void setSSRQuoteStatus(HttpServletRequest request, HttpServletResponse response, QuoteStatusChangeDto dto);

	/**
	 *  批量插入一批新报价。 
	 * 
	 */
	void insertNewSSCQuotes(HttpServletRequest request, HttpServletResponse response, List<SSCQuoteDto> dtos);

	/**
	 *  用户根据当前报价进行报价修改。调用该方法前提是报价单已存在并且客户端拥有报价单ID。 
	 * 
	 */
	void updateOneSSCQuote(HttpServletRequest request, HttpServletResponse response, SSCQuoteDto dto);

	/**
	 *  设置报价单状态，调用该方法前提是报价单已存在并且客户端拥有报价单ID。 
	 *  未对单号进行校验,若为非法ID则不会起作用 
	 * 
	 */
	void setSSCQuoteStatus(HttpServletRequest request, HttpServletResponse response, QuoteStatusChangeDto dto);

	/**
	 *  批量插入一批新报价。 
	 * 
	 */
	void insertNewNPCQuotes(HttpServletRequest request, HttpServletResponse response, List<NPCQuoteDto> dtos);

	/**
	 *  用户根据当前报价进行报价修改。调用该方法前提是报价单已存在并且客户端拥有报价单ID。 
	 * 
	 */
	void updateOneNPCQuote(HttpServletRequest request, HttpServletResponse response, NPCQuoteDto dto);

	/**
	 *  设置报价单状态，调用该方法前提是报价单已存在并且客户端拥有报价单ID。 
	 *  未对单号进行校验,若为非法ID则不会起作用 
	 * 
	 */
	void setNPCQuoteStatus(HttpServletRequest request, HttpServletResponse response, QuoteStatusChangeDto dto);

	/**
	 *  检验重复提交
	 *
	 */
	void getDataToken(HttpServletRequest request, HttpServletResponse response);
}
