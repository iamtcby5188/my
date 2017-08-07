package com.sumscope.bab.quote.wsandlocalmessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by fan.bai on 2016/7/29.
 * 用于启动本地监听器的管理类，通过Configuration类的@Bean方法启动
 */
public class LocalMessageBusManager {
    @Value("${application.messagebus.url}")
    private String host;

    @Value("${application.messagebus.port}")
    private int port;

    @Value("${application.messagebus.quote_persistence_topic}")
    private String quotePersistenceTopic;

    @Value("${application.messagebus.flush_cache_topic}")
    private String flushCacheTopic;


    @Autowired
    private QuoteChangeListener quoteChangeListener;

    @Autowired
    private FlushCacheListener flushCacheListener;


    public void init(){
        quoteChangeListener.start(host,port,quotePersistenceTopic);
        flushCacheListener.start(host,port,flushCacheTopic);
    }
}
