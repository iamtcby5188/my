package com.sumscope.bab.quote.model.dto;


import javax.validation.constraints.NotNull;

/**
 * 报价区域Dto
 * 
 */
public class QuoteProvinceDto {

	@NotNull
	private String provinceCode;

	private String provinceName;

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
}
