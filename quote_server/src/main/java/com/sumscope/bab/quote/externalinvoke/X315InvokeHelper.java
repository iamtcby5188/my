package com.sumscope.bab.quote.externalinvoke;

import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.x315.client.X315HttpClient;
import com.sumscope.x315.client.model.dto.X315CompanySearchResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by shaoxu.wang on 2017/1/4.
 * X315黄页系统客户端封装类，避免系统内部程序直接调用对应的HTTP客户端。
 */
@Component
public class X315InvokeHelper {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private X315HttpClient x315HttpClient;

    /**
     * 从黄页系统根据关键字获取机构列表
     * @param name 机构名称关键字
     * @return 机构数据列表
     */
    public X315CompanySearchResponseDto getCompany(String name) {
        try {
            return x315HttpClient.searchCompanyByKeywords(name);
        } catch (ExternalInvocationFailedException e) {
            LogStashFormatUtil.logError(logger,"调用X315服务错误！" + e);
            return null;
        }
    }
}
