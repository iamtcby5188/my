package com.sumscope.bab.quote.facade;


import com.sumscope.bab.quote.model.dto.SSRQuoteDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by fan.bai on 2017/2/23.
 * 票据报价模块将通过Rest方式向森浦内部第三方应用系统提供功能服务.本接口定义了所有对第三方开放的接口
 */
public interface QuoteInterSystemFacade {

    /**
     * 获取当前直贴报价价格统计信息,回复的价格统计信息以List<QuoteProvinceDto>方式写入response并返回调用方
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    void getCurrentSSRPriceTrends(HttpServletRequest request, HttpServletResponse response);

    /**
     *  批量插入一批新SSR报价。
     *
     */
    void insertNewSSRQuotes(HttpServletRequest request, HttpServletResponse response, List<SSRQuoteDto> dtos);
}
