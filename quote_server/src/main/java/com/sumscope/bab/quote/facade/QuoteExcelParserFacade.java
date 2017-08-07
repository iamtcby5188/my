package com.sumscope.bab.quote.facade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sumscope.bab.quote.model.dto.ExcelFileDto;

/**
 * 为了支持批量导入，本接口用于将Excel文件解析为对应的报价单数据。不同的报价管理界面调用对应的解析方法获取报价单列表。如果Excel本身有错误，这返回成功解析的报价单列表的同时给出详细的错误信息。
 * 
 * 注意，返回的报价单将用于报价，本方法将设置报价单状态为已发布。
 * 
 */
public interface QuoteExcelParserFacade {

	/**
	 * 解析SSR报价单
	 */
	void parserSSRQuotes(HttpServletRequest request, HttpServletResponse response, ExcelFileDto excelFileDto);

	/**
	 * 解析SSC报价单
	 */
	void parserSSCQuotes(HttpServletRequest request, HttpServletResponse response, ExcelFileDto excelFileDto);

	/**
	 * 解析NPC报价单
	 */
	void parserNPCQuotes(HttpServletRequest request, HttpServletResponse response, ExcelFileDto excelFileDto);

}
