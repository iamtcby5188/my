package com.sumscope.bab.quote.externalinvoke;

import com.sumscope.bab.quote.commons.enums.BABQuoteUserRights;
import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.model.dto.LoginUserDto;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import com.sumscope.iam.emclient.EmHttpClientWithCache;
import com.sumscope.iam.emclient.model.EmFunctionDto;
import com.sumscope.iam.emclient.model.EmPermissionsResponseDto;
import com.sumscope.iam.iamclient.EntitlementFailedException;
import com.sumscope.iam.iamclient.IamHttpClientWithCache;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan.bai on 2016/12/29.
 * 用于向IAM系统进行用户权限验证的跨系统调用的服务。
 */
@Component
public class IAMEntitlementCheck {
    private static final String TOKEN = "token";
    private static final String USERID = "userid";
    private static final String USERNAME = "username";
    @Autowired
    private IamHttpClientWithCache iamHttpClientWithCache;

    @Autowired
    private EmHttpClientWithCache emHttpClientWithCache;

    @Autowired
    private PermissionCheckHelperWithCache permissionCheckHelperWithCache;

    /**
     * 根据Token检查用户是否是合法用户。调用时对应的请求Head中需要保存username 和 token
     * 若不合法则抛出验证异常
     * @param servletRequest 请求对象
     */
    public void checkValidUser(HttpServletRequest servletRequest) {
        checkToken(servletRequest);
    }

    private void checkToken(HttpServletRequest servletRequest)  {
        String username = getUserNameFromRequest(servletRequest);
        String token = getTokenFromRequest(servletRequest);
        Boolean valid = checkTokenByUsernameAndToken(username,token);
        thorwAuthorizeExceptionIfInvalid(valid,BABQuoteUserRights.VALID_SUMSCOPE_USER.getErrorMsg());
    }

    public boolean checkTokenByUsernameAndToken(String username, String token){
        Boolean valid;
        try {
            valid = iamHttpClientWithCache.checkTokenWithCache(username, token);
        } catch (ExternalInvocationFailedException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,e);
        }
        return valid;
    }

    private String getTokenFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(TOKEN);
    }

    public String getUserIdFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(USERID);
    }


    private String getUserNameFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(USERNAME);
    }


    /**
     * 根据Token检查用户是否是合法用户并且有全国直贴报价报价权限，该功能权限仅对经过审核的用户开放。
     * 调用时对应的请求Head中需要保存username,token和userID
     * 一般情况下前端Web将控制，无此权限的用户将无法触发相应功能，服务端做双保险，避免错误调用。
     * 若不合法则抛出验证异常
     * @param servletRequest 请求对象
     */
    public void checkValidUserWithSSRManagement(HttpServletRequest servletRequest) {
        checkToken(servletRequest);
        checkUserFunctionRights(servletRequest,BABQuoteUserRights.SSR_MANAGEMENT);
    }
    /**
     * 根据Token检查用户是否是合法用户并且有全国转贴报价浏览权限，该功能权限仅对非企业用户开放。
     * 一般情况下前端Web将控制，无此权限的用户将无法触发相应功能，服务端做双保险，避免错误调用。
     * 调用时对应的请求Head中需要保存username,token和userID
     * 若不合法则抛出验证异常
     * @param servletRequest 请求对象
     */
    public void checkValidUserWithNPCView(HttpServletRequest servletRequest) {
        checkToken(servletRequest);
        checkUserFunctionRights(servletRequest,BABQuoteUserRights.NPC_VIEW);
    }
    /**
     * 根据Token检查用户是否是合法用户并且有全国转贴报价权限，该功能权限仅对经过审核的非企业用户开放。
     * 一般情况下前端Web将控制，无此权限的用户将无法触发相应功能，服务端做双保险，避免错误调用。
     * 调用时对应的请求Head中需要保存username,token和userID
     * 若不合法则抛出验证异常
     * @param servletRequest 请求对象
     */
    public void checkValidUserWithNPCManagement(HttpServletRequest servletRequest){
        checkToken(servletRequest);
        checkUserFunctionRights(servletRequest,BABQuoteUserRights.NPC_MANAGEMENT);
    }
    /**
     * 根据Token检查用户是否是合法用户并且有批量报价导入权限，该功能权限仅对少数超级用户开放。
     * 一般情况下前端Web将控制，无此权限的用户将无法触发相应功能，服务端做双保险，避免错误调用。
     * 调用时对应的请求Head中需要保存username,token和userID
     * 若不合法则抛出验证异常
     * @param servletRequest 请求对象
     */
    public AccessTokenResultDto checkValidUserWithBatchImport(HttpServletRequest servletRequest) {
        AccessTokenResultDto accessTokenResult = getAccessTokenResult(servletRequest);
        checkUserFunctionRights(servletRequest,BABQuoteUserRights.BATCH_IMPORT);
        return accessTokenResult;
    }

    private void checkUserFunctionRights(HttpServletRequest servletRequest, BABQuoteUserRights functionRight) {
        EmPermissionsResponseDto userPermissionsWithCache ;
        try {
            userPermissionsWithCache = emHttpClientWithCache.getUserPermissionsWithCache(getUserIdFromRequest(servletRequest));
        } catch (ExternalInvocationFailedException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,e);
        }
        EmFunctionDto validPermission = permissionCheckHelperWithCache.checkSpecifiedPermissions(functionRight.getFunctionCode(), userPermissionsWithCache);
        boolean permit = false;
        if(validPermission != null && "1".equals(validPermission.getPermValue())){
            permit = true;
        }
        thorwAuthorizeExceptionIfInvalid(permit,functionRight.getErrorMsg());
    }

    private void thorwAuthorizeExceptionIfInvalid(Boolean valid,String msg) {
        if(!valid){
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,msg);
        }
    }

    /**
     * 用户根据用户名，密码进行登录。登录成功获取token
     * @param username 用户名
     * @param password MD5加密后的密码
     * @return 登录信息，若不成功直接抛出登录异常
     */
    public AccessTokenResultDto loginUser(String username, String password) {
        try {
            return iamHttpClientWithCache.loginWithUsernameAndPassword(username,password);
        } catch (EntitlementFailedException | ExternalInvocationFailedException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,e);
        }
    }

    /**
     * 获取某用户的可用BAB权限列表。可能为一个空的列表，但不会返回null
     * @param userId 用户ID
     * @return 该用户合法的BAB功能权限code列表
     */
    public List<String> getUserPermittedFunctions(String userId){
        List<String> results = new ArrayList<>();
        if(StringUtils.isBlank(userId)){
            return results;
        }
        try {
            EmPermissionsResponseDto userPermissions = emHttpClientWithCache.getUserPermissionsWithCache(userId);
            if(userPermissions != null){
                for(BABQuoteUserRights right: BABQuoteUserRights.values()){
                    EmFunctionDto emFunctionDto = permissionCheckHelperWithCache.checkSpecifiedPermissions(right.getFunctionCode(), userPermissions);
                    if(emFunctionDto!=null && "1".equals(emFunctionDto.getPermValue())){
                        results.add(right.getFunctionCode());
                    }
                }
            }
        } catch (ExternalInvocationFailedException e) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.AUTHORIZE_INVALID,e);
        }
        return results;
    }

    /**
     *首次登陆,获取用户Token信息 非首次登陆，直接验证header中用户信息
     * @param request 非首次登陆，直接验证header中用户信息
     * @param user 判断是否是从登陆，传给后台的是字段，而非首次是从header中取用户信息
     * @return AccessTokenResultDto 信息
     */
    public AccessTokenResultDto getAccessTokenResultDto(HttpServletRequest request,LoginUserDto user) {
        AccessTokenResultDto accessTokenResultDto;
        if(user==null || StringUtils.isBlank(user.getUserName())){
            //非首次登陆，直接验证header中用户信息
            accessTokenResultDto=getAccessTokenResult(request);
        }else{
            //首次登陆,获取用户Token信息
            accessTokenResultDto = loginUser(user.getUserName(), user.getPassword());
        }
        return accessTokenResultDto;
    }

    public AccessTokenResultDto getAccessTokenResult(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String userId = getUserIdFromRequest(request);
        String username = getUserNameFromRequest(request);
        boolean valid = checkTokenByUsernameAndToken(username, token);
        thorwAuthorizeExceptionIfInvalid(valid,BABQuoteUserRights.VALID_SUMSCOPE_USER.getErrorMsg());
        AccessTokenResultDto accessTokenResultDto=new AccessTokenResultDto();
        accessTokenResultDto.setAccess_token(token);
        accessTokenResultDto.setUserId(userId);
        return accessTokenResultDto;
    }

    public String checkValidUserName(HttpServletRequest request, String name) {
        checkValidUser(request);
        return Utils.validateStr(name);
    }
}
