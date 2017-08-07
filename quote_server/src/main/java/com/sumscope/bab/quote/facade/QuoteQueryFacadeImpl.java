package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.facade.converter.*;
import com.sumscope.bab.quote.model.dto.*;
import com.sumscope.bab.quote.model.model.NPCQuoteModel;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;
import com.sumscope.bab.quote.model.model.SSCQuoteModel;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import com.sumscope.bab.quote.model.dto.NPCQuoteDto;
import com.sumscope.bab.quote.model.dto.QueryQuoteParameterDto;
import com.sumscope.bab.quote.model.dto.SSCQuoteDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteImgDto;
import com.sumscope.bab.quote.service.NPCQuoteQueryService;
import com.sumscope.bab.quote.service.SSCQuoteQueryService;
import com.sumscope.bab.quote.service.SSRQuoteQueryService;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Administrator on 2016/12/9.
 * 实现类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/quoteQuery", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuoteQueryFacadeImpl extends AbstractPerformanceLogFacade implements QuoteQueryFacade {
    @Autowired
    private QueryQuotesParameterDtoConverter queryQuotesParameterDtoConverter;

    @Autowired
    private SSRQuoteDtoConverter ssrQuoteDtoConverter;

    @Autowired
    private SSCQuoteDtoConverter sscQuoteDtoConverter;

    @Autowired
    private NPCQuoteDtoConverter npcQuoteDtoConverter;

    @Autowired
    private SSRQuoteQueryService ssrQuoteQueryService;

    @Autowired
    private SSCQuoteQueryService sscQuoteQueryService;

    @Autowired
    private NPCQuoteQueryService npcQuoteQueryService;

    @Autowired
    private QuoteCompaniesQueryFacadeService quoteCompaniesQueryFacadeService;

    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Autowired
    private SSRQuoteImgDtoConverter ssrQuoteImgDtoConverter;

    @Override
    @RequestMapping(value = "/searchSSRCompanies", method = RequestMethod.POST)
    public void searchSSRQuoteCompanies(HttpServletRequest request, HttpServletResponse response, @RequestBody String companyName) {
        performWithExceptionCatch(response, () -> querySearchQuotesByCompany(request, companyName, BABQuoteType.SSR));
    }

    @Override
    @RequestMapping(value = "/searchSSCCompanies", method = RequestMethod.POST)
    public void searchSSCQuoteCompanies(HttpServletRequest request, HttpServletResponse response, @RequestBody String companyName) {
        performWithExceptionCatch(response, () -> querySearchQuotesByCompany(request, companyName, BABQuoteType.SSC));
    }

    @Override
    @RequestMapping(value = "/searchNPCCompanies", method = RequestMethod.POST)
    public void searchNPCQuoteCompanies(HttpServletRequest request, HttpServletResponse response, @RequestBody String companyName) {
        performWithExceptionCatch(response, () -> querySearchQuotesByCompany(request, companyName, BABQuoteType.NPC));
    }

    private List<IAMCompanyReferenceDto> querySearchQuotesByCompany(HttpServletRequest request, String companyName, BABQuoteType type) {
        iamEntitlementCheck.checkValidUser(request);
        List<String> companyIDs = new ArrayList<>();
        List<String> companyNames = new ArrayList<>();
        switch (type) {
            case SSR:
                companyIDs = ssrQuoteQueryService.retrieveCurrentQuotesCompanies();
                break;
            case SSC:
                companyIDs = sscQuoteQueryService.retrieveCurrentQuotesCompanies();
                break;
            case NPC:
                companyIDs = npcQuoteQueryService.retrieveCurrentQuotesCompanies();
                break;
        }
        return quoteCompaniesQueryFacadeService.queryQuoteCompanies(companyIDs, companyName,companyNames);
    }

    @Override
    @RequestMapping(value = "/searchSSRByParam", method = RequestMethod.POST)
    public void searchSSRQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody QueryQuoteParameterDto parameterDto) {
        performWithExceptionCatch(response, () -> querySearchSSRQuotesByParam(request, parameterDto));
    }

    private List<SSRQuoteDto> querySearchSSRQuotesByParam(HttpServletRequest request, QueryQuoteParameterDto parameterDto) {
        QueryQuotesParameterModel parameterModel = getParameterModel(request, parameterDto,new Date());
        queryQuotesParameterDtoConverter.getQueryQuotesParameterModel(parameterModel);//商票时
        List<SSRQuoteModel> models = ssrQuoteQueryService.retrieveQuotesByCondition(parameterModel);
        return ssrQuoteDtoConverter.convertToSSRQuoteDtos(models);
    }


    @Override
    @RequestMapping(value = "/querySSRManagerQuotesByParam", method = RequestMethod.POST)
    public void searchSSRManagerQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody QueryQuoteParameterDto parameterDto) {
        performWithExceptionCatch(response, () -> querySSRManagerQuotesByParam(request, parameterDto));
    }

    private List<SSRQuoteDto> querySSRManagerQuotesByParam(HttpServletRequest request, QueryQuoteParameterDto parameterDto) {
        AccessTokenResultDto accessTokenResult = iamEntitlementCheck.getAccessTokenResult(request);
        parameterDto.setUserId(parameterDto!=null && parameterDto.getUserId()==null ? accessTokenResult.getUserId() : null);
        QueryQuotesParameterModel parameterModel = getParameterModel(request, parameterDto,null);
        //判断是否是 已撤销的数据，若是已撤销，默认返回给web端七天的数据
        if(parameterDto.getQuoteStatusList()!=null && parameterDto.getQuoteStatusList().size()>0){
            if(parameterDto.getQuoteStatusList().get(0) == BABQuoteStatus.CAL){
                parameterModel.setCreateTimeBegin(QuoteDateUtils.getLastWeekTime());
                parameterModel.setCreateTimeEnd(QuoteDateUtils.getLatestTimeOfDate(new Date()));
                return getSsrQuoteDtos(parameterModel);
            }
            if ((parameterModel.getCreateTimeBegin() != null || parameterModel.getCreateTimeEnd() != null)|| (parameterModel.getLastUpdateTimeBegin()!=null || parameterModel.getLastUpdateTimeEnd()!=null)) {
                return getSsrQuoteDtos(parameterModel);
            } else {
                return querySearchSSRQuotesByParam(request, parameterDto);
            }
        }
       return null;

    }

    private List<SSRQuoteDto> getSsrQuoteDtos(QueryQuotesParameterModel parameterModel) {
        List<SSRQuoteModel> models = ssrQuoteQueryService.retrieveQuotesByCondition(parameterModel);
        List<SSRQuoteModel> oldModels = ssrQuoteQueryService.retrieveHistoryQuotesByCondition(parameterModel);
        return ssrQuoteDtoConverter.mergeList(models, oldModels);
    }

    @Override
    @RequestMapping(value = "/searchSSCByParam", method = RequestMethod.POST)
    public void searchSSCQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody QueryQuoteParameterDto parameterDto) {
        performWithExceptionCatch(response, () -> querySearchSSCQuotesByParam(request, parameterDto));
    }

    private List<SSCQuoteDto> querySearchSSCQuotesByParam(HttpServletRequest request, QueryQuoteParameterDto parameterDto) {
        QueryQuotesParameterModel parameterModel = getParameterModel(request, parameterDto,new Date());
        List<SSCQuoteModel> models = sscQuoteQueryService.retrieveQuotesByCondition(parameterModel);
        return sscQuoteDtoConverter.convertToSSCQuoteDtos(models);
    }

    @Override
    @RequestMapping(value = "/querySSCManagerQuotesByParam", method = RequestMethod.POST)
    public void querySSCManagerQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody QueryQuoteParameterDto parameterDto) {
        performWithExceptionCatch(response, () -> querySSCManagerQuotesByParam(request, parameterDto));
    }

    private List<SSCQuoteDto> querySSCManagerQuotesByParam(HttpServletRequest request, QueryQuoteParameterDto parameterDto) {
        AccessTokenResultDto accessTokenResult = iamEntitlementCheck.getAccessTokenResult(request);
        parameterDto.setUserId(parameterDto!=null && parameterDto.getUserId()==null ? accessTokenResult.getUserId() : null);
        QueryQuotesParameterModel parameterModel = getParameterModel(request, parameterDto,null);
        if (parameterModel.getCreateTimeBegin() != null || parameterModel.getCreateTimeEnd() != null) {
            List<SSCQuoteModel> models = sscQuoteQueryService.retrieveQuotesByCondition(parameterModel);
            List<SSCQuoteModel> oldModels = sscQuoteQueryService.retrieveHistoryQuotesByCondition(parameterModel);
            return sscQuoteDtoConverter.mergeList(models, oldModels);
        } else {
            return querySearchSSCQuotesByParam(request, parameterDto);
        }
    }

    @Override
    @RequestMapping(value = "/searchNPCByParam", method = RequestMethod.POST)
    public void searchNPCQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody QueryQuoteParameterDto parameterDto) {
        performWithExceptionCatch(response, () -> querySearchNPCQuotesByParam(request, parameterDto));
    }



    private List<NPCQuoteDto> querySearchNPCQuotesByParam(HttpServletRequest request, QueryQuoteParameterDto parameterDto) {
        iamEntitlementCheck.checkValidUserWithNPCView(request);
        QueryQuotesParameterModel parameterModel = getParameterModel(request, parameterDto,new Date());
        List<NPCQuoteModel> models = npcQuoteQueryService.retrieveQuotesByCondition(parameterModel);
        return npcQuoteDtoConverter.convertToNPCQuoteDtos(models);
    }

    @Override
    @RequestMapping(value = "/queryNPCManagerQuotesByParam", method = RequestMethod.POST)
    public void queryNPCManagerQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody QueryQuoteParameterDto parameterDto) {
        performWithExceptionCatch(response, () -> queryNPCManagerQuotesByParam(request, parameterDto));
    }

    private List<NPCQuoteDto> queryNPCManagerQuotesByParam(HttpServletRequest request, QueryQuoteParameterDto parameterDto) {
        AccessTokenResultDto accessTokenResult = iamEntitlementCheck.getAccessTokenResult(request);
        parameterDto.setUserId(parameterDto!=null && parameterDto.getUserId()==null ? accessTokenResult.getUserId() : null);
        QueryQuotesParameterModel parameterModel = getParameterModel(request, parameterDto,null);
        if (parameterModel.getCreateTimeBegin() != null || parameterModel.getCreateTimeEnd() != null) {
            List<NPCQuoteModel> models = npcQuoteQueryService.retrieveQuotesByCondition(parameterModel);
            List<NPCQuoteModel> oldModels = npcQuoteQueryService.retrieveHistoryQuotesByCondition(parameterModel);

            return npcQuoteDtoConverter.mergeList(models, oldModels);
        } else {
            return querySearchNPCQuotesByParam(request, parameterDto);
        }
    }

    private QueryQuotesParameterModel getParameterModel(HttpServletRequest request, QueryQuoteParameterDto parameterDto,Date expiredQuotesDate) {
        iamEntitlementCheck.checkValidUser(request);
        iamEntitlementCheck.getUserIdFromRequest(request);
        return queryQuotesParameterDtoConverter.convertToModel(parameterDto, expiredQuotesDate);
    }

    @Override
    @RequestMapping(value = "/getSSRImage", method = RequestMethod.POST)
    public void getSSRImage(HttpServletRequest request, HttpServletResponse response, @RequestBody String quoteId) {
        performWithExceptionCatch(response, () -> {
            iamEntitlementCheck.checkValidUser(request);
            return doGetSSRImage(quoteId);
        });
    }

    SSRQuoteImgDto doGetSSRImage(String quoteId) {
        List<String> idList = Collections.singletonList(quoteId);
        List<SSRQuoteModel> ssrQuoteModels = ssrQuoteQueryService.retrieveQuoteByIDs(idList);
        if (!CollectionsUtil.isEmptyOrNullCollection(ssrQuoteModels)) {
            SSRQuoteModel ssrQuoteModel = ssrQuoteModels.get(0);
            return ssrQuoteImgDtoConverter.convertToDto(ssrQuoteModel);
        }
        return new SSRQuoteImgDto();

    }
}
