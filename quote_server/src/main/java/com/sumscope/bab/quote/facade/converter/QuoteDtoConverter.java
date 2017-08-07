package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.quote.model.dto.AbstractQuoteDto;
import com.sumscope.bab.quote.commons.enums.Direction;
import com.sumscope.bab.quote.model.dto.IAMCompanyReferenceDto;
import com.sumscope.bab.quote.model.dto.IAMUserReferenceDto;
import com.sumscope.bab.quote.model.dto.QuoteAdditionalInfoDto;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.commons.util.StringPriceUtil;
import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.model.dto.AbstractCountryQuoteDto;
import com.sumscope.bab.quote.model.model.AbstractCountryQuoteModel;
import com.sumscope.bab.quote.model.model.QuoteAdditionalInfoModel;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserContactInfoRetDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserRetDTO;
import com.sumscope.optimus.commons.exceptions.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换报价主表Dto与model。由于各类型的报价明细使用抽象父类，使用map类私有方法进行抽象父类的Dto与model的转换。并使用相应的报价明细转换器对明细数据进行转换
 * 本抽象类不对dto进行验证，由各之类自行完成
 */
public abstract class QuoteDtoConverter {
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientHelperWithCache;

    /**
     * 映射抽象父类的字段
     */
    AbstractQuoteDto mapAbstractModelToDto(AbstractQuoteDto targetDto, AbstractQuoteModel sourceModel) {
        if (sourceModel == null) {
            return null;
        }
        BeanUtils.copyProperties(sourceModel, targetDto);
        mapQuoteCompany(targetDto, sourceModel);
        mapContact(targetDto, sourceModel);
        targetDto.setContainsAdditionalInfo(sourceModel.getAdditionalInfo() != null);
        mapAdditionalInfo(targetDto, sourceModel);
        return targetDto;
    }

    private void mapAdditionalInfo(AbstractQuoteDto targetDto, AbstractQuoteModel sourceModel) {
        if (sourceModel.getAdditionalInfo() != null) {
            QuoteAdditionalInfoDto info = new QuoteAdditionalInfoDto();
            QuoteAdditionalInfoModel additionalInfo = sourceModel.getAdditionalInfo();
            BeanUtils.copyProperties(additionalInfo,info);
            info.setCompanyType(additionalInfo.getAcceptingHouseType());
            targetDto.setAdditionalInfo(info);
            targetDto.setContainsAdditionalInfo(true);
        }
    }

    private void mapContact(AbstractQuoteDto targetDto, AbstractQuoteModel sourceModel) {
        if (sourceModel.getContactId() != null) {
            IAMUserReferenceDto userReferenceDto = new IAMUserReferenceDto();
            UserContactInfoRetDTO userContactByID = edmHttpClientHelperWithCache.getUserContactInfoRetDTO(Utils.validateStr(sourceModel.getContactId()));
            UserRetDTO userByID = edmHttpClientHelperWithCache.getUserRetDTO(Utils.validateStr(sourceModel.getContactId()));
            userReferenceDto.setID(Utils.validateStr(sourceModel.getContactId()));
            if (userContactByID!=null && userContactByID.getQq() != null && !"".equals(userContactByID.getQq())) {
                userReferenceDto.setQq(userContactByID.getQq().contains(";") ?
                        Utils.validateStr(userContactByID.getQq().replace(";", "")) : Utils.validateStr(userContactByID.getQq()));
            }
            userReferenceDto.setMobileTel(Utils.validateStr(UserMobileTelConverter.getUserMobileTel(userContactByID)));
            userReferenceDto.setName(userByID!=null ? Utils.validateStr(userByID.getName()) : null);
            targetDto.setContactDto(userReferenceDto);
        }
    }

    private void mapQuoteCompany(AbstractQuoteDto targetDto, AbstractQuoteModel sourceModel) {
        String quoteCompanyId = sourceModel.getQuoteCompanyId();
        if (quoteCompanyId != null) {
            IAMCompanyReferenceDto companyReferenceDto = new IAMCompanyReferenceDto();
            IdbFinancialCompanyAndIdDTO companyInfo = edmHttpClientHelperWithCache.getIdbFinancialCompanyAndIdDTO(Utils.validateStr(quoteCompanyId));
            companyReferenceDto.setID(quoteCompanyId);
            companyReferenceDto.setName(companyInfo != null ? companyInfo.getName() : null);
            companyReferenceDto.setProvince(companyInfo != null ? companyInfo.getCityName() : null);
            targetDto.setQuoteCompanyDto(companyReferenceDto);
        }
    }

    AbstractCountryQuoteDto mapAbstractCountryModel2AbstractDto(AbstractCountryQuoteDto dto, AbstractCountryQuoteModel model) {
        dto.setGgPrice(StringPriceUtil.price2String(model.getGgPrice()));
        dto.setCsPrice(StringPriceUtil.price2String(model.getCsPrice()));
        dto.setNsPrice(StringPriceUtil.price2String(model.getNsPrice()));
        dto.setNxPrice(StringPriceUtil.price2String(model.getNxPrice()));
        dto.setNhPrice(StringPriceUtil.price2String(model.getNhPrice()));
        dto.setCzPrice(StringPriceUtil.price2String(model.getCzPrice()));
        dto.setWzPrice(StringPriceUtil.price2String(model.getWzPrice()));
        dto.setCwPrice(StringPriceUtil.price2String(model.getCwPrice()));

        return dto;
    }

    boolean priceInvalid(AbstractCountryQuoteDto dto) {
        //非商票时, 8种价格不能同时为空
        boolean bNoSP = !BABBillType.CMB.equals(dto.getBillType());
        return bNoSP && isGgCsNsNxAllEmpty(dto) && isNhCzWzCwAllEmpty(dto);
    }

    private boolean isNhCzWzCwAllEmpty(AbstractCountryQuoteDto dto) {
        return StringUtils.isEmpty(dto.getNhPrice()) && StringUtils.isEmpty(dto.getCzPrice())
                && StringUtils.isEmpty(dto.getWzPrice()) && StringUtils.isEmpty(dto.getCwPrice());
    }

    private boolean isGgCsNsNxAllEmpty(AbstractCountryQuoteDto dto) {
        return StringUtils.isEmpty(dto.getGgPrice()) && StringUtils.isEmpty(dto.getCsPrice())
                && StringUtils.isEmpty(dto.getNsPrice()) && StringUtils.isEmpty(dto.getNxPrice());
    }

    /**
     * 映射抽象父类的字段
     */
    AbstractQuoteModel mapAbstractDtoToModel(AbstractQuoteModel model, AbstractQuoteDto dto) {
        if (dto == null) {
            return null;
        }
        if(dto.getQuoteCompanyDto() == null && dto.getContactDto() == null && dto.getAdditionalInfo()==null){
            ValidationUtil.throwExceptionWithDetails(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING,
                    "QuoteCompanyDto、ContactDto，AdditionalInfo", "QuoteCompanyDto、ContactDto，AdditionalInfo不能同时为空！"));
        }
        BeanUtils.copyProperties(dto, model);

        if (dto.getDirection() == null) {
            model.setDirection(Direction.UDF);
        }
        if (dto.getQuoteCompanyDto() != null) {
            model.setQuoteCompanyId(Utils.validateStr(dto.getQuoteCompanyDto().getID()));
        }
        if (dto.getContactDto() != null) {
            model.setContactId(Utils.validateStr(dto.getContactDto().getID()));
        }
        if (dto.isContainsAdditionalInfo()) {
            QuoteAdditionalInfoModel infoModel = new QuoteAdditionalInfoModel();
            if (dto.getId() != null) {
                infoModel.setQuoteId(Utils.validateStr(dto.getId()));
            }
            setInvalidsAdditionalInfo(dto);
            infoModel.setQuoteCompanyName(Utils.validateStr(dto.getAdditionalInfo().getQuoteCompanyName()));
            infoModel.setContactName(Utils.validateStr(dto.getAdditionalInfo().getContactName()));
            infoModel.setContactTelephone(Utils.validateStr(dto.getAdditionalInfo().getContactTelephone()));
            infoModel.setAcceptingHouseName(dto.getAdditionalInfo().getAcceptingHouseName());
            infoModel.setAcceptingHouseType(dto.getAdditionalInfo().getCompanyType());
            model.setAdditionalInfo(infoModel);
        }

        model.setId(Utils.validateStr(model.getId()));
        model.setMemo(Utils.validateStr(model.getMemo()));
        model.setQuoteCompanyId(Utils.validateStr(model.getQuoteCompanyId()));
        model.setContactId(Utils.validateStr(model.getContactId()));
        model.setOperatorId(Utils.validateStr(model.getOperatorId()));

        return model;
    }

    private void setInvalidsAdditionalInfo(AbstractQuoteDto dto) {
        List<ValidationExceptionDetails> list = new ArrayList<>();
        if(dto.getAdditionalInfo().getQuoteCompanyName()==null){
            list.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "quoteCompanyName", "报价方不能空！"));
        }
        if(dto.getAdditionalInfo().getContactName()==null){
            list.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "contactName", "联系人不能空！"));
        }
        if(dto.getAdditionalInfo().getContactTelephone()==null){
            list.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "contactTelephone", "联系方式不能空！"));
        }
        if(list.size()>0){
            throw new ValidationException(list);
        }
    }




    AbstractCountryQuoteModel mapAbstractCountryDto2AbstractModel(AbstractCountryQuoteModel model, AbstractCountryQuoteDto dto) {
        model.setGgPrice(StringPriceUtil.string2Price(dto.getGgPrice()));
        model.setCsPrice(StringPriceUtil.string2Price(dto.getCsPrice()));
        model.setNsPrice(StringPriceUtil.string2Price(dto.getNsPrice()));
        model.setNxPrice(StringPriceUtil.string2Price(dto.getNxPrice()));
        model.setNhPrice(StringPriceUtil.string2Price(dto.getNhPrice()));
        model.setCzPrice(StringPriceUtil.string2Price(dto.getCzPrice()));
        model.setWzPrice(StringPriceUtil.string2Price(dto.getWzPrice()));
        model.setCwPrice(StringPriceUtil.string2Price(dto.getCwPrice()));

        return model;
    }

    public void doPriceInvalid(AbstractCountryQuoteDto dto) {
        if (priceInvalid(dto)) {
            //非商票时, 8种价格不能同时为空
            ValidationUtil.throwExceptionWithDetails(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "priceError", "非商票时, 8种价格不能同时为空!"));
        }
    }
}
