package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.facade.ApplicationFacadeService;
import com.sumscope.bab.quote.model.dto.AppInitialDataDto;
import com.sumscope.bab.quote.model.dto.BABInitDto;
import com.sumscope.bab.quote.model.dto.LoginUserDto;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * Created by Administrator on 2016/12/23.
 * 页面初始化 和管理页面初始化
 */
@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class ApplicationFacadeServiceTest  extends AbstractBabQuoteIntegrationTest {

    @Autowired
    private ApplicationFacadeService applicationFacadeService;
    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;
    @Test
    public void initTest(){
        BABInitDto testModel=new BABInitDto();
        testModel.setBabQuoteType(BABQuoteType.SSR);
        LoginUserDto user = getLoginUserDto();
        testModel.setUser(user);
        AppInitialDataDto ssrQuoteViewInitData = applicationFacadeService.getSSRQuoteViewInitData(testModel);
        Assert.assertTrue("初始化页面失败！",(ssrQuoteViewInitData!=null && ssrQuoteViewInitData.getParameterList()!=null));
    }

    @Test
    public void login(){
        LoginUserDto user = getLoginUserDto();
        AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.loginUser(user.getUserName(), user.getPassword());
        Assert.assertTrue("用户登陆失败！",(accessTokenResultDto!=null&&accessTokenResultDto.getAccess_token()!=null));
    }

    private LoginUserDto getLoginUserDto() {
        LoginUserDto user=new LoginUserDto();
        user.setUserName("windely03");
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        return user;
    }

    @Test
    public void SSRManagement(){
        BABInitDto babInitDto=new BABInitDto();
        babInitDto.setBabQuoteType(BABQuoteType.SSC);
        LoginUserDto user = getLoginUserDto();
        AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.getAccessTokenResultDto(null, user);
        AppInitialDataDto ssrQuoteInitData = applicationFacadeService.getSSRQuoteInitData(accessTokenResultDto);
        Assert.assertTrue("直贴业务管理页面初始化失败！",ssrQuoteInitData!=null);
    }

    @Test
    public void SSCManagement(){
        BABInitDto babInitDto=new BABInitDto();
        babInitDto.setBabQuoteType(BABQuoteType.SSC);
        LoginUserDto user = getLoginUserDto();
        AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.getAccessTokenResultDto(null, user);
        AppInitialDataDto sscQuoteInitData = applicationFacadeService.getSSCQuoteInitData(accessTokenResultDto);
        Assert.assertTrue("直贴价格管理页面初始化失败！",sscQuoteInitData!=null);
    }

    @Test
    public void NPCManagement(){
        BABInitDto babInitDto=new BABInitDto();
        babInitDto.setBabQuoteType(BABQuoteType.SSC);
        LoginUserDto user = getLoginUserDto();
        AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.getAccessTokenResultDto(null, user);
        AppInitialDataDto npcQuoteInitData = applicationFacadeService.getNPCQuoteInitData(accessTokenResultDto);
        Assert.assertTrue("转贴价格管理页面初始化失败！",npcQuoteInitData!=null);
    }

}
