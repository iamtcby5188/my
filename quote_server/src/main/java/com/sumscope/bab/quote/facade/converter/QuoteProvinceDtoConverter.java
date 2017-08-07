package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.quote.model.dto.QuoteProvinceDto;
import com.sumscope.iam.edmclient.edmsource.dto.IamProvinceAndIdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by shaoxu.wang on 2017/1/16.
 */
@Component
public class QuoteProvinceDtoConverter {
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientHelperWithCache;

    public QuoteProvinceDto convertToQuoteProvinceDto(String provinceCode) {
        QuoteProvinceDto provinceDto = new QuoteProvinceDto();
        provinceDto.setProvinceCode(Utils.validateStr(provinceCode));
        IamProvinceAndIdDTO iamProvinceAndIdDTOByCode = edmHttpClientHelperWithCache.getIamProvinceAndIdDTO(Utils.validateStr(provinceCode));
        provinceDto.setProvinceName(iamProvinceAndIdDTOByCode!=null ? iamProvinceAndIdDTOByCode.getName() : null);

        return provinceDto;
    }
}
