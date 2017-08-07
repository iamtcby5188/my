package com.sumscope.bab.quote.model.dto;

/**
 * 由前端发起用户权限认证调用。例如在打开报价管理页面之前先检查用户是否拥有报价管理的权限。本类用户调用时的参数传递。
 */
public class IAMUserRightsCheckDto {

	/**
	 * 用户的token
	 */
	private String token;

	/**
	 * 用户权限对应的字符串
	 */
	private String userRights;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserRights() {
		return userRights;
	}

	public void setUserRights(String userRights) {
		this.userRights = userRights;
	}
}
