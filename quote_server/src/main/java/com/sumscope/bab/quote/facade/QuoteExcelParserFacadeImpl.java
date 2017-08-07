package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.enums.BABQuoteType;
import com.sumscope.bab.quote.model.dto.ExcelFileDto;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.facade.AbstractExceptionCatchedFacadeImpl;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/1/23.
 * excel导入接口
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/quoteExcel", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuoteExcelParserFacadeImpl extends AbstractPerformanceLogFacade implements QuoteExcelParserFacade {

    @Autowired
    private QuoteExcelParserFacadeService quoteExcelService;

    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Override
    @RequestMapping(value = "/importExcelSSR", method = RequestMethod.POST)
    public void parserSSRQuotes(HttpServletRequest request, HttpServletResponse response,@RequestBody ExcelFileDto excelFileDto) {
        parserExcelQuotes(request,response, excelFileDto, BABQuoteType.SSR);
    }

    @Override
    @RequestMapping(value = "/importExcelSSC", method = RequestMethod.POST)
    public void parserSSCQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody ExcelFileDto excelFileDto) {
        parserExcelQuotes(request,response, excelFileDto, BABQuoteType.SSC);
    }

    @Override
    @RequestMapping(value = "/importExcelNPC", method = RequestMethod.POST)
    public void parserNPCQuotes(HttpServletRequest request, HttpServletResponse response, @RequestBody ExcelFileDto excelFileDto) {
        parserExcelQuotes(request,response, excelFileDto, BABQuoteType.NPC);
    }

    private void parserExcelQuotes(HttpServletRequest request,HttpServletResponse response,ExcelFileDto excelFileDto,BABQuoteType quoteType) {
        performWithExceptionCatch(response, () -> {
            AccessTokenResultDto accessTokenResultDto = iamEntitlementCheck.checkValidUserWithBatchImport(request);
            if (excelFileDto == null) {
                throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "文件接收失败!!!");
            }
            return quoteExcelService.parserQuotes(excelFileDto, quoteType,accessTokenResultDto.getUserId());
        });
    }
}
