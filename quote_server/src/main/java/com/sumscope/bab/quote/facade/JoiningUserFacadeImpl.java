package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.facade.converter.JoiningUserModelConverter;
import com.sumscope.bab.quote.model.dto.UserJoiningRelationDto;
import com.sumscope.bab.quote.model.model.JoiningUserModel;
import com.sumscope.bab.quote.service.UserJoiningService;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/9.
 * 实例类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/joinUser", produces = MediaType.APPLICATION_JSON_VALUE)
public class JoiningUserFacadeImpl extends AbstractPerformanceLogFacade implements JoiningUserFacade {

    @Autowired
    private JoiningUserModelConverter joiningUserModelConverter;

    @Autowired
    private UserJoiningService userJoiningService;

    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Override
    @RequestMapping(value = "/setUserJoinRelation", method = RequestMethod.POST)
    public void setUserJoiningRelation(HttpServletRequest request, HttpServletResponse response,@RequestBody UserJoiningRelationDto dto) {
        performWithExceptionCatch(response,()->{
            //用户必须有直贴管理业务才能够设置子账户
            iamEntitlementCheck.checkValidUserWithSSRManagement(request);
            JoiningUserModel joiningUserModel = joiningUserModelConverter.convertToModel(dto);
            userJoiningService.setUserJoiningRelations(joiningUserModel,dto.isDelJoinUser());
            return joiningUserModelConverter.convertJoiningUserModelToDto(joiningUserModel);
        });
    }

    @Override
    @RequestMapping(value = "/getUserJoinInfo", method = RequestMethod.POST)
    public void getUserAvailableContacts(HttpServletRequest request, HttpServletResponse response, @RequestBody String userId) {
        performWithExceptionCatch(response,()->{
            iamEntitlementCheck.checkValidUserWithSSRManagement(request);
            AccessTokenResultDto accessTokenResult = iamEntitlementCheck.getAccessTokenResult(request);
            //从client中获取用户，传入用户userId
            List<JoiningUserModel> userJoiningRelation = userJoiningService.getUserJoiningRelation(userId);
            List<JoiningUserModel> userRelation = userJoiningService.getJoiningUserRelation(userId);
            return  joiningUserModelConverter.convertJoiningUserModelToDtos(userJoiningRelation,userRelation,accessTokenResult);
        });
    }

}
