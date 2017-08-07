package com.sumscope.bab.quote;

import com.sumscope.bab.quote.externalinvoke.FlushCacheMsgSender;
import com.sumscope.bab.quote.externalinvoke.HttpClientsMsgListenerManager;
import com.sumscope.bab.quote.externalinvoke.QuotePersistenceMsgSender;
import com.sumscope.bab.quote.wsandlocalmessage.LocalMessageBusManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by fan.bai on 2016/4/28.
 * 总线监听及信息发布相关配置
 */
@Configuration
public class MessageBusConfiguration implements EnvironmentAware {

    @Value("${application.messagebus.url}")
    private String host;

    @Value("${application.messagebus.port}")
    private int port;

    @Value("${application.messagebus.quote_persistence_topic}")
    private String quotePersistenceTopic;

    @Value("${application.messagebus.flush_cache_topic}")
    private String flushCacheTopic;


    @Override
    public void setEnvironment(Environment env) {

    }

    // 本地总线监听器初始化
    @Bean(initMethod = "init")
    public LocalMessageBusManager localMessageBusManager(){
        return new LocalMessageBusManager();
    }
    //client总线监听器初始化
    @Bean(initMethod = "start")
    public HttpClientsMsgListenerManager gatewayinvokeBusManager(){
        return new HttpClientsMsgListenerManager();
    }

    @Bean
    public QuotePersistenceMsgSender quotePersistenceMsgSender(){
        return new QuotePersistenceMsgSender(host,port,quotePersistenceTopic);
    }

    @Bean
    public FlushCacheMsgSender flushCacheMsgSender(){
        return new FlushCacheMsgSender(host,port,flushCacheTopic);
    }
}
