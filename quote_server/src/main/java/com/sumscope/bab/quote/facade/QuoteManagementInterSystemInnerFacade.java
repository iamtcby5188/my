package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.model.dto.SSRQuoteDto;

import java.util.List;

/**
 * Created by fan.bai on 2017/2/23.
 * 用于系统间功能调用的接口
 */
interface QuoteManagementInterSystemInnerFacade {
    /**
     * 新增一批报价
     * @param dtos 报价dto
     * @return 新增信息
     */
    List<SSRQuoteDto> doInsertNewSSRQuotes(List<SSRQuoteDto> dtos);
}
