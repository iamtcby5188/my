package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.model.dto.IAMCompanyReferenceDto;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import org.springframework.stereotype.Component;
/**
 *  CompanyDto的转换器 
 * 
 */
@Component
public class IAMCompanyDtoConverter {
	/**
	 * 转换至CompanyDto类型
	 */
	public IAMCompanyReferenceDto convertToReferenceDto(IdbFinancialCompanyAndIdDTO companyAndIdDTO) {
		if (companyAndIdDTO == null) {
			return null;
		}
		IAMCompanyReferenceDto companyReferenceDto = new IAMCompanyReferenceDto();
		companyReferenceDto.setID(companyAndIdDTO.getId());
		companyReferenceDto.setName(companyAndIdDTO.getName());
		companyReferenceDto.setProvince(companyAndIdDTO.getCityName());
		return companyReferenceDto;
	}
}
