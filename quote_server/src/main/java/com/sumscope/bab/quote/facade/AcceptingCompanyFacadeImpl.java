package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.externalinvoke.X315InvokeHelper;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.facade.converter.AcceptingCompanyDtoConverter;
import com.sumscope.bab.quote.model.dto.AcceptingCompanyDto;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;
import com.sumscope.bab.quote.service.AcceptingCompanyService;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import com.sumscope.x315.client.model.dto.X315CompanySearchResponseDto;
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
@RequestMapping(value = "/AcceptingCompany", produces = MediaType.APPLICATION_JSON_VALUE)
public class AcceptingCompanyFacadeImpl extends AbstractPerformanceLogFacade implements AcceptingCompanyFacade {
    @Autowired
    private AcceptingCompanyDtoConverter acceptingCompanyDtoConverter;

    @Autowired
    private AcceptingCompanyService acceptingCompanyService;

    @Autowired
    private X315InvokeHelper x315InvokeHelper;

    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Override
    @RequestMapping(value = "/searchByNameOrPY", method = RequestMethod.POST)
    public void searchAcceptingCompaniesByNameOrPY(HttpServletRequest request, HttpServletResponse response, @RequestBody String name) {
        performWithExceptionCatch(response, () -> querySearchAcceptingCompaniesByNameOrPY(request,name));
    }
    private List<AcceptingCompanyDto> querySearchAcceptingCompaniesByNameOrPY(HttpServletRequest request, String name)  {
        name = iamEntitlementCheck.checkValidUserName(request, name);
        List<AcceptingCompanyModel> models = acceptingCompanyService.searchCompaniesByNameOrPY(name);

        return acceptingCompanyDtoConverter.convertToDtos(models);
    }



    @Override
    @RequestMapping(value = "/searchByNameOrPYFromYellow", method = RequestMethod.POST)
    public void searchExternalYellowPageCompaniesByName(HttpServletRequest request, HttpServletResponse response, @RequestBody String name) {
        performWithExceptionCatch(response, () -> querySearchAcceptingCompaniesByNameOrPYFromYellow(request,name));
    }

    private List<AcceptingCompanyDto> querySearchAcceptingCompaniesByNameOrPYFromYellow(HttpServletRequest request, String name) {
        name = iamEntitlementCheck.checkValidUserName(request, name);
        X315CompanySearchResponseDto responseDto = x315InvokeHelper.getCompany(name);

        return acceptingCompanyDtoConverter.convertX315ToDtos(responseDto);
    }
}
