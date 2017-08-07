package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABJoiningDisplayMode;
import com.sumscope.bab.quote.commons.enums.BABJoiningTarget;

public class AvailableContactDto {

	/**
	 * 子用户Dto，包括用户名称等信息。
	 */
	private IAMUserReferenceDto joiningUserDto;

	/**
	 * 子机构信息Dto。子机构为子用户的所属机构。
	 */
	private IAMCompanyReferenceDto joiningCompanyDto;

	private BABJoiningDisplayMode joiningDisplayMode;

	private BABJoiningTarget babJoiningTarget;

	public IAMUserReferenceDto getJoiningUserDto() {
		return joiningUserDto;
	}

	public void setJoiningUserDto(IAMUserReferenceDto joiningUserDto) {
		this.joiningUserDto = joiningUserDto;
	}

	public IAMCompanyReferenceDto getJoiningCompanyDto() {
		return joiningCompanyDto;
	}

	public void setJoiningCompanyDto(IAMCompanyReferenceDto joiningCompanyDto) {
		this.joiningCompanyDto = joiningCompanyDto;
	}

	public BABJoiningDisplayMode getJoiningDisplayMode() {
		return joiningDisplayMode;
	}

	public void setJoiningDisplayMode(BABJoiningDisplayMode joiningDisplayMode) {
		this.joiningDisplayMode = joiningDisplayMode;
	}

	public BABJoiningTarget getBabJoiningTarget() {
		return babJoiningTarget;
	}

	public void setBabJoiningTarget(BABJoiningTarget babJoiningTarget) {
		this.babJoiningTarget = babJoiningTarget;
	}
}
