package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.commons.util.StringPriceUtil;
import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.quote.externalinvoke.RedisCheckHelper;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.dto.QuoteProvinceDto;
import com.sumscope.bab.quote.model.dto.SSCQuoteDto;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.model.model.SSCQuoteModel;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.exceptions.ValidationException;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/12.
 * SSC报价单模型转换器
 */
@Component
public class SSCQuoteDtoConverter extends QuoteDtoConverter {
    @Autowired
    private QuoteProvinceDtoConverter quoteProvinceDtoConverter;
    @Autowired
    private RedisCheckHelper redisUtils;
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientHelperWithCache;

    public SSCQuoteDto convertToSSCQuoteDto(SSCQuoteModel model) {
//        ValidationUtil.validateModel(model);

        SSCQuoteDto dto = (SSCQuoteDto)super.mapAbstractModelToDto(new SSCQuoteDto(), model);
        dto = (SSCQuoteDto)super.mapAbstractCountryModel2AbstractDto(dto, model);
        dto.setYbhPrice(StringPriceUtil.price2String(model.getYbhPrice()));
        dto.setWbhPrice(StringPriceUtil.price2String(model.getWbhPrice()));
        setQuoteProvinces(model, dto);

        return dto;
    }

    private void setQuoteProvinces(SSCQuoteModel model, SSCQuoteDto dto) {
        if (model.getProvinceCode() != null) {
            dto.setQuoteProvinces(quoteProvinceDtoConverter.convertToQuoteProvinceDto(model.getProvinceCode()));
        }
    }

    public List<SSCQuoteDto> convertToSSCQuoteDtos(List<SSCQuoteModel> models) {
        if (models == null) {
            return null;
        }

        List<SSCQuoteDto> lst = new ArrayList<>();
        for (SSCQuoteModel model : models) {
            SSCQuoteDto dto = convertToSSCQuoteDto(model);
            if (dto != null) {
                lst.add(dto);
            }
        }

        return lst;
    }

    public List<SSCQuoteDto> mergeList(List<SSCQuoteModel> models, List<SSCQuoteModel> oldModels) {
        List<SSCQuoteDto> sscQuoteDto = convertToSSCQuoteDtos(models);
        List<SSCQuoteDto> oldQuoteDto= convertToSSCQuoteDtos(oldModels);
        for (SSCQuoteDto dto:oldQuoteDto){
            dto.setFromHistory(true);
        }
        if (sscQuoteDto == null || sscQuoteDto.isEmpty()) {
            sscQuoteDto = oldQuoteDto;
        }
        else {
            sscQuoteDto.addAll(oldQuoteDto);
        }
        return sscQuoteDto;
    }

    public SSCQuoteModel convertToSSCQuoteModel(SSCQuoteDto dto) {
        ValidationUtil.validateModel(dto);
        redisUtils.checkTokenModelDto(Utils.validateStr(dto.getQuoteToken()));
        SSCQuoteModel model = (SSCQuoteModel)super.mapAbstractDtoToModel(new SSCQuoteModel(), dto);
        model = (SSCQuoteModel)super.mapAbstractCountryDto2AbstractModel(model, dto);
        model.setYbhPrice(StringPriceUtil.string2Price(dto.getYbhPrice()));
        model.setWbhPrice(StringPriceUtil.string2Price(dto.getWbhPrice()));

       //取当前报价人的地区信息
        if (dto.getQuoteCompanyDto() != null && dto.getQuoteCompanyDto().getID()!=null) {
            IdbFinancialCompanyAndIdDTO idbFinancialCompanyAndIdDTO = edmHttpClientHelperWithCache.getIdbFinancialCompanyAndIdDTO(Utils.validateStr(dto.getQuoteCompanyDto().getID()));
            QuoteProvinceDto provinceDto = getQuoteProvinceDto(idbFinancialCompanyAndIdDTO);
            dto.setQuoteProvinces(provinceDto);
        }

        doPriceInvalid(dto);

        if (priceInvalid2(dto)) {
            //商票时, 有保函价格和无保函价格不能同时为空
            List<ValidationExceptionDetails> detailsList = new ArrayList<>();
            detailsList.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "priceError", "商票时, 有保函价格和无保函价格不能同时为空!"));
            throw new ValidationException(detailsList);
        }

        if (dto.getQuoteProvinces() != null) {
            model.setProvinceCode(dto.getQuoteProvinces().getProvinceCode());
        }
        //只有未发布的状态才能更改生效日和到期日，其他则不更改
        if(dto.getQuoteStatus() == BABQuoteStatus.DFT && dto.getEffectiveDate()!=null){
            model.setExpiredDate(model.getExpiredDate()!=null ? model.getExpiredDate() : QuoteDateUtils.getExpiredTimeOfDate(dto.getEffectiveDate()));
        }
        return model;
    }

    private QuoteProvinceDto getQuoteProvinceDto(IdbFinancialCompanyAndIdDTO idbFinancialCompanyAndIdDTO) {
        QuoteProvinceDto provinceDto = new QuoteProvinceDto();
        if(idbFinancialCompanyAndIdDTO!=null){
            provinceDto.setProvinceCode(idbFinancialCompanyAndIdDTO.getCity());
            provinceDto.setProvinceName(idbFinancialCompanyAndIdDTO.getCityName());
        }
        return provinceDto;
    }

    private boolean priceInvalid2(SSCQuoteDto dto) {
        //商票时, 有保函价格和无保函价格不能同时为空
        boolean bSP = BABBillType.CMB.equals(dto.getBillType());
        return bSP && StringUtils.isEmpty(dto.getYbhPrice()) && StringUtils.isEmpty(dto.getWbhPrice());
    }

    public List<SSCQuoteModel> convertToSSCQuoteModes(List<SSCQuoteDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<SSCQuoteModel> lst = new ArrayList<>();
        for (SSCQuoteDto dto : dtos) {
            SSCQuoteModel model = convertToSSCQuoteModel(dto);
            if (model != null) {
                lst.add(model);
            }
        }

        return  lst;
    }
}
