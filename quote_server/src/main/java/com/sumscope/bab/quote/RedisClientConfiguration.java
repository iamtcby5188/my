package com.sumscope.bab.quote;

import com.sumscope.optimus.commons.redis.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2017/3/6.
 * 操作redis 相关RedisClient配置
 */
@Configuration
public class RedisClientConfiguration {

    @Value("${application.redis.host}")
    private String host;

    @Value("${application.redis.port}")
    private int port;

    @Bean
    public RedisClient createRedisClient() {
        RedisClient redisClient = new RedisClient(host, port, true);
        redisClient.open();
        return redisClient;
    }

}
