package com.sumscope.bab.quote.externalinvoke;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.util.UUIDUtils;
import com.sumscope.bab.quote.externalinvoke.RedisCheckHelper;
import com.sumscope.bab.quote.model.dto.TokenModelDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *redis 测试
 */
public class RedisTokenTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private RedisCheckHelper redisUtils;

    @Test
    public void redisTokenDtoTest(){
        String token = redisUtils.getToken();
        Assert.assertTrue("获取RedisToken失败！", token != null);
        redisUtils.destroyToken(token);
        String BTokenRedis= redisUtils.getTokenJedis(token);
        Assert.assertTrue("删除RedisToken失败！", BTokenRedis == null);

    }



}
