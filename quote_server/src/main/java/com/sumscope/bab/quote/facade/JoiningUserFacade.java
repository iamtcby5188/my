package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.model.dto.UserJoiningRelationDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JoiningUserFacade {

	/**
	 * 设置当前用户与指定用户的关联关系。关联关系设置成功后既可进行代报价。该方法需要验证当前用户是否拥有报价权限
	 */
	void setUserJoiningRelation(HttpServletRequest request, HttpServletResponse response, UserJoiningRelationDto dto) ;

	/**
	 * 获取当前用户的可用子账号列表。该方法需要验证当前用户是否拥有报价权限
	 */
	void getUserAvailableContacts(HttpServletRequest request, HttpServletResponse response,String userId) ;

}
