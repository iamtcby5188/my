package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.*;
import com.sumscope.bab.quote.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.quote.facade.converter.UserMobileTelConverter;
import com.sumscope.bab.quote.facade.converter.WEBParameterEnumConverter;
import com.sumscope.bab.quote.model.dto.*;
import com.sumscope.bab.quote.model.model.JoiningUserModel;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.facade.converter.JoiningUserModelConverter;
import com.sumscope.bab.quote.service.UserJoiningService;
import com.sumscope.iam.edmclient.edmsource.dto.IamProvinceAndIdDTO;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserContactInfoRetDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserRetDTO;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 为了不在Facade的实现类中写入太多业务代码，抽取出FacadeService用于业务处理。
 */
@Component
public class ApplicationFacadeService {

    @Autowired
    private WEBParameterEnumConverter webParameterEnumConverter;
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientWithCache;
    @Autowired
    private UserJoiningService userJoiningService;
    @Autowired
    private JoiningUserModelConverter joiningUserModelConverter;
    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    /**
     * 获取直贴报价大厅初始化数据
     * @param sign BABInitDto对象
     * @return AppInitialDataDto 系统初始化dto
     */
    public AppInitialDataDto getSSRQuoteViewInitData(BABInitDto sign) {
        AppInitialDataDto appInitialDataDto = new AppInitialDataDto();
        setFilterDtoList(sign, appInitialDataDto);//设置List<FilterDto>
        return appInitialDataDto;
    }

    /**
     * SSRQuoteManagementInitData 管理页面初始化
     * @return AppInitialDataDto 系统初始化dto
     */
    public AppInitialDataDto getSSRQuoteInitData(AccessTokenResultDto accessTokenResult){
        AppInitialDataDto appInitialDataDto=new AppInitialDataDto();
        List<FilterDto> list=new ArrayList<>();
        setWEBParameterEnumDtoForBABQuoteStatus(list);
        setWeBParameterEnumDtoForWebBillType(list);//票据类型
        setWeBParameterEnumDtoForProvince(list);//交易地点
        setWEBParameterEnumDtoForBabQuotePriceType(list);//承兑行类别
        setWEBParameterEnumDtoFoBABAcceptingCompanyType(list);//商票承兑行类别
        setWEBParameterEnumDtoForDirection(list);//报价方向
        appInitialDataDto.setParameterList(list);
        tittleInitDto(appInitialDataDto,accessTokenResult);
        return appInitialDataDto;
    }

    /**
     * NPCQuoteManagementInitData 页面初始化
     * @return AppInitialDataDto 系统初始化dto
     */
    public AppInitialDataDto getNPCQuoteInitData(AccessTokenResultDto accessTokenResult){
        AppInitialDataDto appInitialDataDto=new AppInitialDataDto();
        List<FilterDto> list=new ArrayList<>();
        setWeBParameterEnumDtoForBABBillMedium(list);//票据类型
        setWEBParameterEnumDtoForWebBABTradeType(list);
        setWeBParameterEnumDtoForProvince(list);//交易地点
        setWEBParameterEnumDtoForBabQuotePriceType(list);//承兑行类别
        appInitialDataDto.setParameterList(list);
        tittleInitDto(appInitialDataDto,accessTokenResult);
        return appInitialDataDto;
    }

    /**
     * SSCQuoteManagementInitData 管理页面初始化
     * @return AppInitialDataDto 系统初始化dto
     */
    public AppInitialDataDto getSSCQuoteInitData(AccessTokenResultDto accessTokenResult){
        AppInitialDataDto appInitialDataDto=new AppInitialDataDto();
        List<FilterDto> list= new ArrayList<>();
        setWeBParameterEnumDtoForWebSSCBillType(list);//票据类型 全国直贴价格
        setWeBParameterEnumDtoForProvince(list);//交易地点
        setWEBParameterEnumDtoForBabQuotePriceType(list);//承兑行类别
        setWEBParameterEnumDtoFoBABAcceptingCompanyType(list);//商票承兑行类别
        setWEBParameterEnumDtoForGuarantee(list);//保函
        appInitialDataDto.setParameterList(list);
        tittleInitDto(appInitialDataDto,accessTokenResult);
        return appInitialDataDto;
    }

    /**
     * 当前用户登陆信息
     * @param dto 用户token信息
     */
    public AppInitialDataDto loginUser(AccessTokenResultDto dto){
        AppInitialDataDto appInitialDataDto = new AppInitialDataDto();
        doInitialData(appInitialDataDto, getLoginUserDto(dto), getUserCompanyReferenceDto(dto.getUserId()), dto.getUserId(), dto);
        return appInitialDataDto;
    }
    private void doInitialData(AppInitialDataDto appInitialDataDto, LoginUserDto loginUserDto, IAMCompanyReferenceDto userCompanyReferenceDto, String id, AccessTokenResultDto accessTokenResultDto) {
        appInitialDataDto.setCurrentUser(loginUserDto);//从黄页系统中获取当前用户
        appInitialDataDto.setCurrentCompany(userCompanyReferenceDto);//从IAMClient中获得
        setJoiningUserModel(appInitialDataDto, id);//把子账户返回给web
        setUserWithFunctionsToInitDto(accessTokenResultDto.getUserId(), accessTokenResultDto.getAccess_token(), appInitialDataDto);
    }

    private void setFilterDtoList(BABInitDto sign, AppInitialDataDto appInitialDataDto) {
        List<FilterDto> list = new ArrayList<>();
        setWEBParameterEnumDtoForBabQuotePriceType(list);//承兑行类别
        if (BABQuoteType.SSC == sign.getBabQuoteType()) {
            setWeBParameterEnumDtoForWebSSCBillType(list);//票据类型 全国直贴价格
            setWEBParameterEnumDtoForGuarantee(list);//保函
            setWeBParameterEnumDtoForProvince(list);
            setWeBParameterEnumDtoForProvince(list);//交易地点
        } else if (BABQuoteType.NPC == sign.getBabQuoteType()) {
            setWeBParameterEnumDtoForBABBillMedium(list);//票据类型 全国转贴价格
            setWEBParameterEnumDtoForWebBABTradeType(list);
        } else {
            setWeBParameterEnumDtoForWebBillType(list);//票据类型
            setWEBParameterEnumDtoForDirection(list);//报价方向
            setWEBParameterEnumDtoFoBABAcceptingCompanyType(list);
            setWEBParameterEnumDtoForWebQuoteAmountCondition(list);//票面金额
            setWEBParameterEnumDtoForWebDueDateRange(list);//剩余期限
            setWeBParameterEnumDtoForProvince(list);//交易地点
        }
        appInitialDataDto.setParameterList(list);
    }

    private void setUserWithFunctionsToInitDto(String userId, String token, AppInitialDataDto appInitialDataDto) {
        Map<String, Object> userRightsInfoMap = new HashMap<>();
        userRightsInfoMap.put(Constant.USER_TOKEN, token);
        appInitialDataDto.getInfo().putAll(userRightsInfoMap);
//        TODO：应该key为“PermittedFunctions”，value为List，这样返回前端，前端程序比较难以找到对应的功能权限位置
        List<String> userPermittedFunctions = iamEntitlementCheck.getUserPermittedFunctions(userId);
        for (String code : userPermittedFunctions) {
            appInitialDataDto.getInfo().put(code, "true");
        }
    }


    private void setJoiningUserModel(AppInitialDataDto appInitialDataDto, String userId) {
        List<JoiningUserModel> joiningUserModelList = userJoiningService.getUserJoiningRelation(userId);//用户链接-代报价权限数据模型 子账户信息
        setCurrentUserToList(userId, joiningUserModelList);// 把当前当前用户也返回给web端
        List<AvailableContactDto> list = joiningUserModelConverter.convertJoiningUserModelToDto(joiningUserModelList);
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.JOIN_USER, list);
        appInitialDataDto.setInfo(map);//获取权限信息
    }

    /**
     *JoiningUser把当前当前用户也一并返回给web端
     */
    private void setCurrentUserToList(String userId, List<JoiningUserModel> joiningUserModelList) {
        JoiningUserModel model = new JoiningUserModel();
        model.setUserId(userId);
        model.setJoinUserId(userId);
        model.setJoinMode(BABJoiningDisplayMode.CTR);
        joiningUserModelList.add(model);
    }

    private void tittleInitDto(AppInitialDataDto appInitialDataDto,AccessTokenResultDto accessTokenResultDto) {
        LoginUserDto user = new LoginUserDto();
        user.setID(accessTokenResultDto.getUserId());
        doInitialData(appInitialDataDto, getLoginUserDto(accessTokenResultDto), getUserCompanyReferenceDto(user.getID()), user.getID(), accessTokenResultDto);
    }

    /**
     * 交易模式
     */
    private List<FilterDto> setWEBParameterEnumDtoForWebBABTradeType(List<FilterDto> list) {
        List<WEBParameterEnumDto> babTradeTypeDto = webParameterEnumConverter.convertToDtoList(BABTradeType.values());
        setFilterDto(list, babTradeTypeDto, WEBSearchParameterMode.SINGLE,BABConditionName.TransactionalModel.getDisplayName());
        return list;
    }

    //剩余期限
    private List<FilterDto> setWEBParameterEnumDtoForWebDueDateRange(List<FilterDto> list) {
        List<WEBParameterEnumDto> webDueDateRangeDto = webParameterEnumConverter.convertToDtoList(WEBDueDateRange.values());
        setFilterDto(list,webDueDateRangeDto,WEBSearchParameterMode.MULTIPLE,BABConditionName.RemainingTerm.getDisplayName());
        return list;
    }

    //票面金额
    private List<FilterDto> setWEBParameterEnumDtoForWebQuoteAmountCondition(List<FilterDto> list) {
        List<WEBParameterEnumDto> webQuoteAmountConditionDto = webParameterEnumConverter.convertToDtoList(WEBQuoteAmountCondition.values());
        setFilterDto(list,webQuoteAmountConditionDto,WEBSearchParameterMode.MULTIPLE,BABConditionName.ParValue.getDisplayName());
        return list;
    }

    //报价状态
    private List<FilterDto> setWEBParameterEnumDtoForBABQuoteStatus(List<FilterDto> list) {
        BABQuoteStatus[] babQuoteStatus = new BABQuoteStatus[2];
        int i = 0;
        for (BABQuoteStatus quoteStatus : BABQuoteStatus.values()) {
            if(quoteStatus==BABQuoteStatus.CAL||quoteStatus==BABQuoteStatus.DFT){
                babQuoteStatus[i] = quoteStatus;
                i++;
            }
        }
        List<WEBParameterEnumDto> webParameterEnumDto= webParameterEnumConverter.convertToDtoList(babQuoteStatus);
        setFilterDto(list, webParameterEnumDto,WEBSearchParameterMode.SINGLE,BABConditionName.BABQuoteStatus.getDisplayName());
        return list;
    }

    //承兑行类别
    public List<FilterDto> setWEBParameterEnumDtoForBabQuotePriceType(List<FilterDto> list) {
        BABQuotePriceType[] babQuotePriceTypes=new BABQuotePriceType[BABQuotePriceType.values().length-2];
        int i=0;
        for(BABQuotePriceType babQuotePriceType : BABQuotePriceType.values()){
            if(babQuotePriceType!=BABQuotePriceType.WBH && babQuotePriceType!=BABQuotePriceType.YBH){
                babQuotePriceTypes[i] = babQuotePriceType;
                i++;
            }
        }
        getFilterDtos(list, babQuotePriceTypes, WEBSearchParameterMode.MULTIPLE, BABConditionName.AcceptanceBankCategory.getDisplayName());
        return list;
    }
    //保函
    private List<FilterDto> setWEBParameterEnumDtoForGuarantee(List<FilterDto> list) {
        BABQuotePriceType[] babQuotePriceTypes=new BABQuotePriceType[2];
        babQuotePriceTypes[0] = BABQuotePriceType.WBH;
        babQuotePriceTypes[1] = BABQuotePriceType.YBH;
        return getFilterDtos(list, babQuotePriceTypes, WEBSearchParameterMode.SINGLE, BABConditionName.Guarantee.getDisplayName());
    }

    private List<FilterDto> getFilterDtos(List<FilterDto> list, BABQuotePriceType[] babQuotePriceTypes, WEBSearchParameterMode single, String displayName) {
        List<WEBParameterEnumDto> babQuotePriceTypeDto = webParameterEnumConverter.convertToDtoList(babQuotePriceTypes);
        setFilterDto(list, babQuotePriceTypeDto, single, displayName);
        return list;
    }

    //承兑方类别
    private List<FilterDto> setWEBParameterEnumDtoFoBABAcceptingCompanyType(List<FilterDto> list) {
        List<WEBParameterEnumDto> babQuotePriceTypeDto= webParameterEnumConverter.convertToDtoList(BABAcceptingCompanyType.listCMBTypes());
        setFilterDto(list,babQuotePriceTypeDto,WEBSearchParameterMode.MULTIPLE,BABConditionName.AcceptanceCategory.getDisplayName());
        return list;
    }
    //期限
    public List<FilterDto> setWEBParameterEnumDtoForShiborParameter(List<FilterDto> list) {
        ShiborParameter[] shibor = new ShiborParameter[ShiborParameter.values().length];
        int b = 0;
        for (ShiborParameter shiborParameter : ShiborParameter.values()) {
            if(shiborParameter!=ShiborParameter.SHIBOR_O_N){
                shibor[b] = shiborParameter;
            }else{
                shiborParameter.setCode(Constant.SHIBOR_O_N);
                shibor[b] = shiborParameter;
            }
            b++;
        }
        List<WEBParameterEnumDto> shiborDto= webParameterEnumConverter.convertToDtoList(shibor);
        setFilterDto(list,shiborDto,WEBSearchParameterMode.SINGLE,BABConditionName.DateTime.getDisplayName());
        return list;
    }

    //报价方向
    private List<FilterDto> setWEBParameterEnumDtoForDirection(List<FilterDto> list) {
        Direction[] directions = new Direction[Direction.values().length-1];
        int b = 0;
        for (Direction direction : Direction.values()) {
            if(direction!=Direction.UDF){
                directions[b] = direction;
                b++;
            }
        }
        List<WEBParameterEnumDto> directionDto = webParameterEnumConverter.convertToDtoList(directions);
        setFilterDto(list, directionDto,WEBSearchParameterMode.SINGLE,BABConditionName.Direction.getDisplayName());
        return list;
    }

    //票据类型 ----全国直贴交易报价大厅init
    private List<FilterDto> setWeBParameterEnumDtoForWebBillType(List<FilterDto> list) {
        List<WEBParameterEnumDto> webBillTypeDto = webParameterEnumConverter.convertToDtoList(WEBBillType.values());
        setFilterDto(list,webBillTypeDto,WEBSearchParameterMode.SINGLE,BABConditionName.TicketType.getDisplayName());
        return list;
    }

    //票据类型 ----全国直贴价格
    private List<FilterDto> setWeBParameterEnumDtoForWebSSCBillType(List<FilterDto> list) {
        List<WEBParameterEnumDto> webSSCBillTypeDto = webParameterEnumConverter.convertToDtoList(WebSSCBillType.values());
        setFilterDto(list,webSSCBillTypeDto,WEBSearchParameterMode.SINGLE,BABConditionName.TicketType.getDisplayName());
        return list;
    }

    //票据类型 ---全国转贴价格init
    private List<FilterDto> setWeBParameterEnumDtoForBABBillMedium(List<FilterDto> list) {
        List<WEBParameterEnumDto> babBillMediumDto = webParameterEnumConverter.convertToDtoList(BABBillMedium.values());
        setFilterDto(list,babBillMediumDto,WEBSearchParameterMode.SINGLE,BABConditionName.TicketType.getDisplayName());
        return list;
    }

    private void setFilterDto(List<FilterDto> list, List<WEBParameterEnumDto> babBillMediumDto,WEBSearchParameterMode mode,String conditionName) {
        FilterDto filterDto = new FilterDto();
        filterDto.setConditions(babBillMediumDto);
        filterDto.setConditionMode(mode);
        filterDto.setConditionName(conditionName);
        list.add(filterDto);
    }

    //交易地点
    private List<FilterDto> setWeBParameterEnumDtoForProvince(List<FilterDto> list) {
        List<IamProvinceAndIdDTO> cn = edmHttpClientWithCache.getIamProvinceAndIdDTOToList();
        List<WEBParameterEnumDto> webParameterEnumDto = new ArrayList<>();
        for (IamProvinceAndIdDTO province : cn) {
            ProvinceDto provinceDto=new ProvinceDto();
            BeanUtils.copyProperties(province,provinceDto);
            webParameterEnumDto.add(provinceDto);
        }
        setFilterDto(list,webParameterEnumDto,WEBSearchParameterMode.MULTIPLE,BABConditionName.TradingLocation.getDisplayName());
        return list;
    }

    //从 EDMClient获取IdbFinancialCompanyAndIdDTO 转为 IAMCompanyReferenceDto
     private IAMCompanyReferenceDto getUserCompanyReferenceDto(String userId) {
        IAMCompanyReferenceDto iamCompanyReference = new IAMCompanyReferenceDto();
        UserRetDTO userRetDTO = edmHttpClientWithCache.getUserRetDTO(userId);
        if(userRetDTO != null){
            IdbFinancialCompanyAndIdDTO idbFinancial = edmHttpClientWithCache.getIdbFinancialCompanyAndIdDTO(userRetDTO.getCompanyId());
            if(idbFinancial!=null){
                iamCompanyReference.setName(idbFinancial.getName());
                iamCompanyReference.setProvince(idbFinancial.getCityName());
                iamCompanyReference.setID(idbFinancial.getId());
                iamCompanyReference.setFullName(idbFinancial.getFullName());
                iamCompanyReference.setBankNature(idbFinancial.getBankNature());
            }
        }
        return iamCompanyReference;
    }

    //AccessTokenResultDto 转 LoginUserDto
    private LoginUserDto getLoginUserDto(AccessTokenResultDto dto) {
        UserContactInfoRetDTO usersContactDto = edmHttpClientWithCache.getUserContactInfoRetDTO(dto.getUserId());
        UserRetDTO userRetDto = edmHttpClientWithCache.getUserRetDTO(dto.getUserId());
        LoginUserDto user = new LoginUserDto();
        BeanUtils.copyProperties(userRetDto,user);
        user.setMobileTel(UserMobileTelConverter.getUserMobileTel(usersContactDto));
        if(usersContactDto!=null && usersContactDto.getQq()!=null && !"".equals(usersContactDto.getQq())){
            user.setQq(usersContactDto.getQq().contains(";") ? usersContactDto.getQq().replace(";","") : usersContactDto.getQq());
        }
        if(userRetDto!=null){
            user.setName(userRetDto.getName());
            user.setID(userRetDto.getId());
            user.setUserName(userRetDto.getUsername());
        }
        user.setTokenExpiredTime(dto.getExpires_in());
        return user;
    }
}
