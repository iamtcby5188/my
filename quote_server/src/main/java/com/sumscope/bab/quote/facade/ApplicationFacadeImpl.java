package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.model.dto.BABInitDto;
import com.sumscope.bab.quote.model.dto.LoginUserDto;
import com.sumscope.bab.quote.service.MockTestDataService;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/9.
 * 实例类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/init", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationFacadeImpl extends AbstractPerformanceLogFacade implements ApplicationFacade {

    @Autowired
    private ApplicationFacadeService applicationFacadeService;

    @Autowired
    private MockTestDataService mockTestDataService;
    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Override
    @RequestMapping(value = "/initData", method = RequestMethod.POST)
    public void getQuoteViewInitData(HttpServletRequest request, HttpServletResponse response, @RequestBody BABInitDto sign) {
        performWithExceptionCatch(response, () -> {
            //web端传SSR/SSC/NPC 或者不传，不传默认返回初始化全国直贴大厅
            // 传对应的值表示在具体所在的某一个页面，返回对应的页面数据
//            AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.getAccessTokenResultDto(request, sign.getUser());
            iamEntitlementCheck.checkValidUser(request);
            return applicationFacadeService.getSSRQuoteViewInitData(sign);
        });
    }

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void loginUser(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginUserDto user) {
        performWithExceptionCatch(response,()->{
            AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.getAccessTokenResultDto(request, user);
            return applicationFacadeService.loginUser(accessTokenResultDto);
        });
    }

    @Override
    @RequestMapping(value = "/SSRInitData", method = RequestMethod.POST)
    public void getSSRQuoteManagementInitData(HttpServletRequest request, HttpServletResponse response,@RequestBody BABInitDto sign) {
        performWithExceptionCatch(response,()->{
            //SSR管理页面初始化
            iamEntitlementCheck.checkValidUserWithSSRManagement(request);
            AccessTokenResultDto accessTokenResult = iamEntitlementCheck.getAccessTokenResult(request);
            return applicationFacadeService.getSSRQuoteInitData(accessTokenResult);
        });

    }

    @Override
    @RequestMapping(value = "/SSCInitData", method = RequestMethod.POST)
    public void getSSCQuoteManagementInitData(HttpServletRequest request, HttpServletResponse response,@RequestBody BABInitDto sign) {
        performWithExceptionCatch(response,()->{
            //SSC管理页面初始化
            iamEntitlementCheck.checkValidUserWithSSRManagement(request);
            AccessTokenResultDto accessTokenResult = iamEntitlementCheck.getAccessTokenResult(request);
            return applicationFacadeService.getSSCQuoteInitData(accessTokenResult);
        });
    }

    @Override
    @RequestMapping(value = "/NPCInitData", method = RequestMethod.POST)
    public void getNPCQuoteManagementInitData(HttpServletRequest request, HttpServletResponse response,@RequestBody BABInitDto sign) {
        performWithExceptionCatch(response,()->{
            //NPC管理页面初始化
            iamEntitlementCheck.checkValidUserWithNPCManagement(request);
            AccessTokenResultDto accessTokenResult = iamEntitlementCheck.getAccessTokenResult(request);
            return applicationFacadeService.getNPCQuoteInitData(accessTokenResult);
        });
    }


    @Override
    @RequestMapping(value = "/MockDataCreation", method = RequestMethod.POST)
    public void generateMockTestData(HttpServletRequest request, HttpServletResponse response, @RequestBody Map number) {
        performWithExceptionCatch(response, () -> generateMockTestData(number));
    }

    private String generateMockTestData(Map number) {
//        TODO:增加根据配置文件是否允许生成mock数据判断，生产环境不允许使用该方法
        Integer num = (Integer)number.get("number");
        if(num == 0){
            return "" + mockTestDataService.generateMockTestData(num);
        }
        if(num == 1){
            return mockTestDataService.getCache();
        }
        if(num == 2){
            String msg = (String)number.get("message");
            mockTestDataService.updateCache(msg);
            return msg;
        }
        if(num == 3){
            mockTestDataService.showCache();
            return "show";
        }
        if(num == 100){
            return "" + mockTestDataService.generateMockTestData(num);
        }
        if(num == 1000){
            return "" + mockTestDataService.generateMockTestData(num);
        }
        return "number 没有设置";

    }

}
