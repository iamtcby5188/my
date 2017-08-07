package com.sumscope.bab.quote.iamclient;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import com.sumscope.iam.emclient.EmHttpClientWithCache;
import com.sumscope.iam.emclient.model.EmPermissionsResponseDto;
import com.sumscope.iam.iamclient.EntitlementFailedException;
import com.sumscope.iam.iamclient.IamHttpClientWithCache;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by fan.bai on 2016/12/28.
 * IAM客户端测试
 */
public class IamHttpClientTest extends AbstractBabQuoteIntegrationTest {
    @Autowired
    private EmHttpClientWithCache emHttpClientWithCache;

    @Autowired
    private IamHttpClientWithCache iamHttpClientWithCache;

    private static final String USER_ID = "1c497085d6d511e48ec3000c29a13c19"; //roomtest185

    private static final String WINDELY_03 = "windely03";

    private static final String PASSWORD = "e10adc3949ba59abbe56e057f20f883e";

    /**
     * 测试EM客户端是否可以进行成功的远程调用
     * 测试缓存是否被正确使用
     */
    @Test
    public void testEmClientGetPermissionsWithCache(){
        try {
            EmPermissionsResponseDto permissionsResponseDto = emHttpClientWithCache.getUserPermissionsWithCache(USER_ID);
            Assert.assertTrue("获取权限列表失败！","OK".equals(permissionsResponseDto.getMeta().getErrCode()));
            EmPermissionsResponseDto permissionsResponseDto1 = emHttpClientWithCache.getUserPermissionsWithCache(USER_ID);
            //使用缓存后，返回值应该是第一次调用的结果，因此返回的实例对象是同一个，使用==进行实例对象相等检查
            Assert.assertTrue("缓存失效！",permissionsResponseDto == permissionsResponseDto1);
            EmPermissionsResponseDto permissionsResponseDto2 = emHttpClientWithCache.getUserPermissionsWithCache(USER_ID);
            Assert.assertTrue("缓存失效！",permissionsResponseDto1 == permissionsResponseDto2);

        } catch (ExternalInvocationFailedException e) {
            Assert.assertTrue("调用远程EM服务失败！",true);
        }
    }

    @Test
    public void testIamClientLogin(){
        try {
            AccessTokenResultDto accessTokenResultDto = iamHttpClientWithCache.loginWithUsernameAndPassword(WINDELY_03, PASSWORD);
            Assert.assertTrue("获取令牌失败！",accessTokenResultDto.getAccess_token() != null);
        } catch (EntitlementFailedException e) {
            Assert.assertTrue("用户login失败",true);
        } catch (ExternalInvocationFailedException e) {
            Assert.assertTrue("调用远程ACM服务失败！",true);
        }
    }

    @Test
    public void testIamClientCheckTokenWithCache(){
        AccessTokenResultDto accessTokenResultDto = null;
        try {
            accessTokenResultDto  = iamHttpClientWithCache.loginWithUsernameAndPassword(WINDELY_03, PASSWORD);
            Assert.assertTrue("获取令牌失败！",accessTokenResultDto.getAccess_token() != null);
        } catch (EntitlementFailedException e) {
            Assert.assertTrue("用户login失败",true);
        } catch (ExternalInvocationFailedException e) {
            Assert.assertTrue("调用远程ACM服务失败！",true);
        }
        if(accessTokenResultDto != null){
            try {
                Boolean valid = iamHttpClientWithCache.checkTokenWithCache(WINDELY_03, accessTokenResultDto.getAccess_token());
                Assert.assertTrue("验证Token失败！",valid);
                Boolean valid2 = iamHttpClientWithCache.checkTokenWithCache(WINDELY_03, accessTokenResultDto.getAccess_token());
                Assert.assertTrue("验证Token使用缓存失败！",valid2 == valid);

                valid = iamHttpClientWithCache.checkTokenWithCache(WINDELY_03, "sdfasdfasdfasdfasdfasdf");
                Assert.assertTrue("验证无效Token失败！",!valid);

                valid2 = iamHttpClientWithCache.checkTokenWithCache(WINDELY_03, "sdfasdfasdfasdfasdfasdf");
                Assert.assertTrue("验证无效Token使用缓存失败！",valid2 == valid);

            } catch (ExternalInvocationFailedException e) {
                Assert.assertTrue("调用远程ACM服务失败！",true);
            }
        }
    }
}
