package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.externalinvoke.RedisCheckHelper;
import com.sumscope.bab.quote.facade.converter.*;
import com.sumscope.bab.quote.model.dto.*;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.model.model.NPCQuoteModel;
import com.sumscope.bab.quote.model.model.SSCQuoteModel;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import com.sumscope.bab.quote.service.*;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/9.
 * 实现类
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/quoteMng", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuoteManagementFacadeImpl extends AbstractPerformanceLogFacade implements QuoteManagementFacade,QuoteManagementInterSystemInnerFacade {
    @Autowired
    private SSRQuoteDtoConverter ssrQuoteDtoConverter;

    @Autowired
    private SSCQuoteDtoConverter sscQuoteDtoConverter;

    @Autowired
    private NPCQuoteDtoConverter npcQuoteDtoConverter;

    @Autowired
    private AcceptingCompanyDtoConverter acceptingCompanyDtoConverter;

    @Autowired
    private SSRQuoteManagementTransactionalService ssrQuoteManagementService;

    @Autowired
    private SSCQuoteManagementTransactionalService sscQuoteManagementService;

    @Autowired
    private NPCQuoteManagementTransactionalService npcQuoteManagementService;

    @Autowired
    private AcceptingCompanyService acceptingCompanyService;

    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Autowired
    private RedisCheckHelper redis;

    @Override
    @RequestMapping(value = "/insertSSR", method = RequestMethod.POST)
    public void insertNewSSRQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody List<SSRQuoteDto> dtos) {
        performWithExceptionCatch(response, () -> {
            iamEntitlementCheck.checkValidUserWithSSRManagement(request);
            return doInsertNewSSRQuotes(dtos);
        });
    }

    @Override
    public List<SSRQuoteDto> doInsertNewSSRQuotes(List<SSRQuoteDto> dtos) {
        //更新本地承兑行
        acceptingCompanyService.updateInfos(acceptingCompanyDtoConverter.convertToModels(dtos));
        List<SSRQuoteModel> models = ssrQuoteDtoConverter.convertToSSRQuoteModes(dtos);
        ssrQuoteManagementService.insertNewQuotesInTransaction(models);
        return dtos;
    }

    @Override
    @RequestMapping(value = "/updateSSR", method = RequestMethod.POST)
    public void updateOneSSRQuote(HttpServletRequest request, HttpServletResponse response, @RequestBody SSRQuoteDto dto) {
        performWithExceptionCatch(response, () -> updateOneSSRQuote(request,dto));
    }
    private Boolean updateOneSSRQuote(HttpServletRequest request, SSRQuoteDto dto) {
        iamEntitlementCheck.checkValidUserWithSSRManagement(request);

        SSRQuoteModel model = ssrQuoteDtoConverter.convertToSSRQuoteModel(dto);
        SSRQuoteModel resModel = ssrQuoteManagementService.updateQuoteInTransaction(model);

        return resModel != null;
    }

    @Override
    @RequestMapping(value = "/setSSRStatus", method = RequestMethod.POST)
    public void setSSRQuoteStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody QuoteStatusChangeDto dto) {
        performWithExceptionCatch(response, () -> querySetSSRQuoteStatus(request,dto));
    }
    private Boolean querySetSSRQuoteStatus(HttpServletRequest request, QuoteStatusChangeDto dto) {
        return geBooleanOfStatus(request, dto,BABQuoteType.SSR);
    }

    private Boolean geBooleanOfStatus(HttpServletRequest request, QuoteStatusChangeDto dto,BABQuoteType quoteType) {
        iamEntitlementCheck.checkValidUserWithSSRManagement(request);
        if (dto != null) {
            getService(quoteType).setQuoteStatusInTransaction(dto.getIds(), dto.getStatus());
        }

        return true;
    }

    @Override
    @RequestMapping(value = "/insertSSC", method = RequestMethod.POST)
    public void insertNewSSCQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody List<SSCQuoteDto> dtos) {
        performWithExceptionCatch(response, () -> insertNewSSCQuotes(request,dtos));
    }
    private int insertNewSSCQuotes(HttpServletRequest request, List<SSCQuoteDto> dtos) {
        iamEntitlementCheck.checkValidUserWithSSRManagement(request);

        List<SSCQuoteModel> models = sscQuoteDtoConverter.convertToSSCQuoteModes(dtos);
        sscQuoteManagementService.insertNewQuotesInTransaction(models);

        return models != null ? models.size() : 0;
    }

    @Override
    @RequestMapping(value = "/updateSSC", method = RequestMethod.POST)
    public void updateOneSSCQuote(HttpServletRequest request, HttpServletResponse response, @RequestBody SSCQuoteDto dto) {
        performWithExceptionCatch(response, () -> updateOneSSCQuote(request,dto));
    }
    private Boolean updateOneSSCQuote(HttpServletRequest request, SSCQuoteDto dto) {
        iamEntitlementCheck.checkValidUserWithSSRManagement(request);
        SSCQuoteModel model = sscQuoteDtoConverter.convertToSSCQuoteModel(dto);
        SSCQuoteModel resModel = sscQuoteManagementService.updateQuoteInTransaction(model);

        return resModel != null ;
    }

    @Override
    @RequestMapping(value = "/setSSCStatus", method = RequestMethod.POST)
    public void setSSCQuoteStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody QuoteStatusChangeDto dto) {
        performWithExceptionCatch(response, () -> querySetSSCQuoteStatus(request,dto));
    }
    private Boolean querySetSSCQuoteStatus(HttpServletRequest request, QuoteStatusChangeDto dto) {
        return geBooleanOfStatus(request, dto,BABQuoteType.SSC);
    }

    @Override
    @RequestMapping(value = "/insertNPC", method = RequestMethod.POST)
    public void insertNewNPCQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody List<NPCQuoteDto> dtos) {
        performWithExceptionCatch(response, () -> insertNewNPCQuotes(request,dtos));
    }
    private int insertNewNPCQuotes(HttpServletRequest request, List<NPCQuoteDto> dtos) {
        iamEntitlementCheck.checkValidUserWithNPCManagement(request);

        List<NPCQuoteModel> models = npcQuoteDtoConverter.convertToNPCQuoteModes(dtos);
        npcQuoteManagementService.insertNewQuotesInTransaction(models);

        return models != null ? models.size() : 0;
    }

    @Override
    @RequestMapping(value = "/updateNPC", method = RequestMethod.POST)
    public void updateOneNPCQuote(HttpServletRequest request, HttpServletResponse response, @RequestBody NPCQuoteDto dto) {
        performWithExceptionCatch(response, () -> updateOneNPCQuote(request,dto));
    }
    private Boolean updateOneNPCQuote(HttpServletRequest request, NPCQuoteDto dto) {
        iamEntitlementCheck.checkValidUserWithNPCManagement(request);
        NPCQuoteModel model = npcQuoteDtoConverter.convertToNPCQuoteModel(dto);
        NPCQuoteModel resModel = npcQuoteManagementService.updateQuoteInTransaction(model);

        return resModel != null;
    }

    @Override
    @RequestMapping(value = "/setNPCStatus", method = RequestMethod.POST)
    public void setNPCQuoteStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody QuoteStatusChangeDto dto) {
        performWithExceptionCatch(response, () -> querySetNPCQuoteStatus(request,dto));
    }

    @Override
    @RequestMapping(value = "/getDataToken", method = RequestMethod.POST)
    public void getDataToken(HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response, () -> redis.getToken());
    }

    private Boolean querySetNPCQuoteStatus(HttpServletRequest request, QuoteStatusChangeDto dto) {
        iamEntitlementCheck.checkValidUserWithNPCManagement(request);
        if (dto != null) {
            npcQuoteManagementService.setQuoteStatusInTransaction(dto.getIds(), dto.getStatus());
        }

        return true;
    }


    private QuoteManagementTransactionalService getService(BABQuoteType quoteType) {
        switch (quoteType) {
            case SSR:
                return ssrQuoteManagementService;
            case SSC:
                return sscQuoteManagementService;
            case NPC:
                return npcQuoteManagementService;
            default:
                return ssrQuoteManagementService;
        }
    }
}
