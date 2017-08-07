package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.util.Pinyin4jUtil;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;
import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.model.dto.AcceptingCompanyDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.x315.client.model.dto.X315CompanyGeneralInfoDto;
import com.sumscope.x315.client.model.dto.X315CompanySearchResponseDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型转换器
 */
@Component
public class AcceptingCompanyDtoConverter {
	public List<AcceptingCompanyModel> convertToModels(List<SSRQuoteDto> ssrQuoteDtos) {
        if (ssrQuoteDtos == null) {
            return null;
        }

        List<AcceptingCompanyModel> result = new ArrayList<>();
        for (SSRQuoteDto dto : ssrQuoteDtos) {
            if (dto == null || dto.getAcceptingHouse() == null || dto.getAcceptingHouse().getId() == null) {
                continue;
            }

			AcceptingCompanyModel acceptingCompanyModel = new AcceptingCompanyModel();
            BeanUtils.copyProperties(dto.getAcceptingHouse(), acceptingCompanyModel);

            acceptingCompanyModel.setId(Utils.validateStr(acceptingCompanyModel.getId()));
            acceptingCompanyModel.setIamCompanyID(Utils.validateStr(acceptingCompanyModel.getIamCompanyID()));
            acceptingCompanyModel.setCompanyName(Utils.validateStr(acceptingCompanyModel.getCompanyName()));
            acceptingCompanyModel.setAddress(Utils.validateStr(acceptingCompanyModel.getAddress()));
            acceptingCompanyModel.setManager(Utils.validateStr(acceptingCompanyModel.getManager()));
            acceptingCompanyModel.setRegistrationNumber(Utils.validateStr(acceptingCompanyModel.getRegistrationNumber()));
            acceptingCompanyModel.setCompanyNamePY(Utils.validateStr(acceptingCompanyModel.getCompanyNamePY()));
            acceptingCompanyModel.setCompanyNamePinYin(Utils.validateStr(acceptingCompanyModel.getCompanyNamePinYin()));

            result.add(acceptingCompanyModel);
		}

		return result;
	}

	/**
	 * 转换本地承兑行机构数据model置dto。dto的fromExternal字段设置为false。在报价单确认时更新本地数据库的最后使用时间字段。
	 */
	public AcceptingCompanyDto convertToDto(AcceptingCompanyModel model) {
        ValidationUtil.validateModel(model);

		AcceptingCompanyDto dto = new AcceptingCompanyDto();
		BeanUtils.copyProperties(model, dto);
		dto.setFromExternal(false);

		return dto;
	}

    /**
     * @param models
     * @return 一次性最多返回10条
     */
    public List<AcceptingCompanyDto> convertToDtos(List<AcceptingCompanyModel> models) {
        if (models == null) {
            return null;
        }

        List<AcceptingCompanyDto> result = new ArrayList<>();

        int count = 0;
        for (AcceptingCompanyModel model : models) {
            AcceptingCompanyDto dto = convertToDto(model);
            if (dto != null) {
                result.add(dto);
                count++;
            }

            if (count >= Constant.ACCEPTING_COMPANY_NUM) {
                break;
            }
        }

        return result;
    }

	public List<AcceptingCompanyDto> convertX315ToDtos(X315CompanySearchResponseDto x315CompanySearchResponseDto) {
        if (x315CompanySearchResponseDto == null || x315CompanySearchResponseDto.getOrgs() == null) {
            return null;
        }

        List<AcceptingCompanyDto> result = new ArrayList<>();
        for (X315CompanyGeneralInfoDto infoDto : x315CompanySearchResponseDto.getOrgs()) {
            AcceptingCompanyDto companyDto = new AcceptingCompanyDto();
            companyDto.setId(infoDto.getId());
            companyDto.setCompanyType(BABAcceptingCompanyType.CET);
            companyDto.setCompanyName(infoDto.getMc());
            companyDto.setAddress(infoDto.getXxdz());
            companyDto.setManager(infoDto.getFzr());
            companyDto.setRegistrationNumber(infoDto.getGszch());
            companyDto.setCompanyNamePY(Pinyin4jUtil.getPinYinHeadChar(infoDto.getMc()));
            companyDto.setCompanyNamePinYin(Pinyin4jUtil.getPinYin(infoDto.getMc()));
            companyDto.setFromExternal(true);

            result.add(companyDto);
        }
        return result;
    }
}
