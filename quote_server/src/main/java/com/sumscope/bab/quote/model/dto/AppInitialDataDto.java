package com.sumscope.bab.quote.model.dto;

import java.util.List;
import java.util.Map;

/**
 * 系统初始化dto
 */
public class AppInitialDataDto {

	private List<FilterDto> parameterList;

	private LoginUserDto currentUser;

	private IAMCompanyReferenceDto currentCompany;

	/**
	 * 附加的信息。例如一些权限信息等，根据Key值区分
	 */
	private Map<String ,Object> info;

	/**
	 * 第一次打开页面时进行搜索的结果
	 */
//	private List<AbstractQuoteDto> data;

	public List<FilterDto> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<FilterDto> parameterList) {
		this.parameterList = parameterList;
	}

	public LoginUserDto getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(LoginUserDto currentUser) {
		this.currentUser = currentUser;
	}

	public IAMCompanyReferenceDto getCurrentCompany() {
		return currentCompany;
	}

	public void setCurrentCompany(IAMCompanyReferenceDto currentCompany) {
		this.currentCompany = currentCompany;
	}

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}

//	public List<AbstractQuoteDto> getData() {
//		return data;
//	}
//
//	public void setData(List<AbstractQuoteDto> data) {
//		this.data = data;
//	}
}
