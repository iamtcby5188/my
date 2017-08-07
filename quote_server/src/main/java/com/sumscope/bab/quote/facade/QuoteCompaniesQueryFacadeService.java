package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.quote.facade.converter.IAMCompanyDtoConverter;
import com.sumscope.bab.quote.model.dto.IAMCompanyReferenceDto;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 根据机构名称查询所有符合名称模糊查询的报价机构列表，该类负责实际的facade层业务处理，以减少facade类的代码量并区分功能。
 */
@Component
public class QuoteCompaniesQueryFacadeService {
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientHelperWithCache;

    @Autowired
    private IAMCompanyDtoConverter iamCompanyDtoConverter;

    /**
     * 查询所有拥有有效报价的机构ID, 只返回包含companyName字段的机构
     */
    public List<IAMCompanyReferenceDto> queryQuoteCompanies(List<String> companyIDs, String companyName,List<String> companyNames) {
        List<IAMCompanyReferenceDto> result = new ArrayList<>();
        int count = 0;
        for (String id : companyIDs) {
            IdbFinancialCompanyAndIdDTO companyAndIdDTO = edmHttpClientHelperWithCache.getIdbFinancialCompanyAndIdDTO(Utils.validateStr(id));
            if(companyAndIdDTO==null){
                continue;
            }
            if (isDtoMatched(companyName, companyAndIdDTO)) {
                result.add(iamCompanyDtoConverter.convertToReferenceDto(companyAndIdDTO));
                count++;
            }
            if (count >= Constant.ACCEPTING_COMPANY_NUM) {
                break;
            }
        }
        return result;
    }

    private boolean isDtoMatched(String companyName, IdbFinancialCompanyAndIdDTO companyAndIdDTO) {
        return companyAndIdDTO != null
                && isFieldsMatched(companyName, companyAndIdDTO);
    }

    private boolean isFieldsMatched(String companyName, IdbFinancialCompanyAndIdDTO companyAndIdDTO) {
        return containsString(companyAndIdDTO.getPinyin(),companyName)
         || containsString(companyAndIdDTO.getPinyinFull(),companyName)
         || containsString(companyAndIdDTO.getName(),companyName)
         || containsString(companyAndIdDTO.getFullName(),companyName);
    }

    private boolean containsString(String source, String target){
        return source!=null && source.contains(target);
    }
}
