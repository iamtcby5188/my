package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.Constant;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by fan.bai on 2016/12/16.
 * 生产用于开发测试使用的模拟数据的服务。主要使用随机数方式生成报价单。
 */
public interface MockTestDataService {
    /**
     * 生成虚拟报价单数据用于功能开发及测试
     * @param numberOfData 希望生成的报价单数量
     * @return 实际生成的报价单数量
     */
    long generateMockTestData(long numberOfData);

//    以下三个方法仅用于测试
    @CachePut(value = Constant.DEFAULT_CACHING_NAME,key = "'Mock'")
    String updateCache(String msg);

    @Cacheable(value= Constant.DEFAULT_CACHING_NAME,key = "'Mock'")
    String getCache();

    String showCache();
}
