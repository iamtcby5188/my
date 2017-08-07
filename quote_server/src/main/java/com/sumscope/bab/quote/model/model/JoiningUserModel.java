package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.BABJoiningDisplayMode;

import java.io.Serializable;

/**
 * 用户链接-代报价权限数据模型
 */
public class JoiningUserModel implements Serializable {

	/**
	 * 主用户（操作人）ID
	 * 
	 */
	private String userId;

	/**
	 * 子用户ID
	 */
	private String joinUserId;

	/**
	 * 链接显示方式
	 */
	private BABJoiningDisplayMode joinMode;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJoinUserId() {
		return joinUserId;
	}

	public void setJoinUserId(String joinUserId) {
		this.joinUserId = joinUserId;
	}

	public BABJoiningDisplayMode getJoinMode() {
		return joinMode;
	}

	public void setJoinMode(BABJoiningDisplayMode joinMode) {
		this.joinMode = joinMode;
	}
}
