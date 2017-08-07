package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.util.StringPriceUtil;
import com.sumscope.bab.quote.externalinvoke.RedisCheckHelper;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;
import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import com.sumscope.bab.quote.service.AcceptingCompanyService;
import com.sumscope.optimus.commons.exceptions.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaoxu.wang on 2016/12/12.
 * SSR报价单模型转换器
 */
@Component
public class SSRQuoteDtoConverter extends QuoteDtoConverter {
    @Autowired
    private AcceptingCompanyDtoConverter acceptingCompanyDtoConverter;

    @Autowired
    private AcceptingCompanyService acceptingCompanyService;

    @Autowired
    private QuoteProvinceDtoConverter quoteProvinceDtoConverter;

    @Autowired
    private RedisCheckHelper redisUtils;

    /**
     * 转换model置SSRQuoteDto，使用mapAbstractModelToDto映射共享字段。
     */
    public SSRQuoteDto convertToSSRQuoteDto(SSRQuoteModel model) {
        //利率 金额若是电议、不限 sql默认值设置为1000000000000.00，为避免返回给web页面端展示
        if(model.getPrice()!=null && model.getPrice().intValue()==new BigDecimal("1000000000000.00").intValue()){
            model.setPrice(null);
        }
        if(model.getAmount()!=null && model.getAmount().intValue()==new BigDecimal("1000000000000.00").intValue()){
            model.setAmount(null);
        }
        SSRQuoteDto dto = (SSRQuoteDto)super.mapAbstractModelToDto(new SSRQuoteDto(), model);

        if (model.getProvinceCode() != null) {
            dto.setQuoteProvinces(quoteProvinceDtoConverter.convertToQuoteProvinceDto(model.getProvinceCode()));
        }

        dto.setQuotePriceType(model.getQuotePriceType());

        if (model.getAcceptingHouseId() != null) {
            AcceptingCompanyModel companyModel = acceptingCompanyService.getCompanyById(Utils.validateStr(model.getAcceptingHouseId()));
            dto.setAcceptingHouse(acceptingCompanyDtoConverter.convertToDto(companyModel));
        }

        //为了节省网络带宽，图片信息不字节通过报价Dto传递，而是在用户点击图标时单独获取。
        dto.setBase64Img(null);
        if(StringUtils.isNotBlank(model.getBase64Img())){
            dto.setContainsImg(true);
        }
        dto.setDueDate(model.getDueDate());
        dto.setPrice(StringPriceUtil.price2String(model.getPrice()));
        dto.setAmount(model.getAmount());

        return dto;
    }

    public List<SSRQuoteDto> convertToSSRQuoteDtos(List<SSRQuoteModel> models) {
        if (models == null) {
            return null;
        }

        List<SSRQuoteDto> lst = new ArrayList<>();
        for (SSRQuoteModel model : models) {
            SSRQuoteDto dto = convertToSSRQuoteDto(model);
            if (dto != null) {
                lst.add(dto);
            }
        }

        return lst;
    }

    public List<SSRQuoteDto> mergeList(List<SSRQuoteModel> models, List<SSRQuoteModel> oldModels) {
        List<SSRQuoteDto> ssrQuoteDto = convertToSSRQuoteDtos(models);
        List<SSRQuoteDto> oldSSRQuoteDto= convertToSSRQuoteDtos(oldModels);
        //添加标记是否是历史数据
        for(SSRQuoteDto dto:oldSSRQuoteDto){
            dto.setFromHistory(true);
        }
        if (ssrQuoteDto == null || ssrQuoteDto.isEmpty()) {
            ssrQuoteDto = oldSSRQuoteDto;
        }
        else {
            ssrQuoteDto.addAll(oldSSRQuoteDto);
        }
        return ssrQuoteDto;
    }

    public SSRQuoteModel convertToSSRQuoteModel(SSRQuoteDto dto) {
        ValidationUtil.validateModel(dto);
        redisUtils.checkTokenModelDto(Utils.validateStr(dto.getQuoteToken()));

        validateDto(dto);

        SSRQuoteModel model = (SSRQuoteModel)super.mapAbstractDtoToModel(new SSRQuoteModel(), dto);

        if (dto.getQuoteProvinces() != null) {
            model.setProvinceCode(Utils.validateStr(dto.getQuoteProvinces().getProvinceCode()));
        }
        if (BABBillType.CMB.equals(dto.getBillType())){
            model.setQuotePriceType(dto.getQuotePriceType()!=null ? dto.getQuotePriceType(): BABQuotePriceType.YBH);
        }
        if (BABBillType.BKB.equals(dto.getBillType())){
            model.setQuotePriceType(dto.getQuotePriceType());
        }

        if (dto.getAcceptingHouse() != null) {
            model.setAcceptingHouseId(Utils.validateStr(dto.getAcceptingHouse().getId()));
        }

        model.setDueDate(dto.getDueDate());
        model.setPrice(StringPriceUtil.string2Price(dto.getPrice()));
        model.setAmount(dto.getAmount());

        model.setProvinceCode(Utils.validateStr(model.getProvinceCode()));
        model.setAcceptingHouseId(Utils.validateStr(model.getAcceptingHouseId()));

        model.setBase64Img(dto.getBase64Img());

        return model;
    }

    private void validateDto(SSRQuoteDto dto) {
        List<ValidationExceptionDetails> eDetailsList = new ArrayList<>();

        BABBillType billType = dto.getBillType();
        if (BABBillType.BKB.equals(billType) && (dto.getQuotePriceType() == null)) {
            eDetailsList.add(new ValidationExceptionDetails
                    (GeneralValidationErrorType.DATA_MISSING, "quotePriceType", "银票时报价价格类型为空!"));
        }

        if (BABBillType.CMB.equals(dto.getBillType()) && ((dto.getAcceptingHouse() == null ||
                StringUtils.isBlank(dto.getAcceptingHouse().getId())) && dto.getAdditionalInfo().getAcceptingHouseName()==null)) {
            eDetailsList.add(new ValidationExceptionDetails
                    (GeneralValidationErrorType.DATA_MISSING, "acceptingHouse", "商票时acceptingHouse为空!"));
        }

        if(eDetailsList.size()>0){
            throw new ValidationException(eDetailsList);
        }
    }

    public List<SSRQuoteModel> convertToSSRQuoteModes(List<SSRQuoteDto> dtos) {
        if (dtos == null) {
            return null;
        }

        List<SSRQuoteModel> lst = new ArrayList<>();
        for (SSRQuoteDto dto : dtos) {
            SSRQuoteModel model = convertToSSRQuoteModel(dto);
            if (model != null) {
                lst.add(model);
            }
        }

        return lst;
    }

}
