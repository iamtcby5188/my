package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABJoiningDisplayMode;
import com.sumscope.bab.quote.commons.enums.BABJoiningTarget;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 设置用户关联关系时用于数据传递的dto。
 */
public class UserJoiningRelationDto {

	private String currentUserId;


	/**
	 * 子用户信息，客户需要填入子用户名及密码。系统验证成功后写入数据库。
	 */
	@NotNull
	@Valid
	private LoginUserDto joiningUserDto;

	private BABJoiningDisplayMode joiningMode;

	private BABJoiningTarget joingTarget;

	/**
	 * 判断是否是解除关联
	 */
	private boolean delJoinUser;

	public LoginUserDto getJoiningUserDto() {
		return joiningUserDto;
	}

	public void setJoiningUserDto(LoginUserDto joiningUserDto) {
		this.joiningUserDto = joiningUserDto;
	}

	public BABJoiningDisplayMode getJoiningMode() {
		return joiningMode;
	}

	public void setJoiningMode(BABJoiningDisplayMode joiningMode) {
		this.joiningMode = joiningMode;
	}

	public BABJoiningTarget getJoingTarget() {
		return joingTarget;
	}

	public void setJoingTarget(BABJoiningTarget joingTarget) {
		this.joingTarget = joingTarget;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public boolean isDelJoinUser() {
		return delJoinUser;
	}

	public void setDelJoinUser(boolean delJoinUser) {
		this.delJoinUser = delJoinUser;
	}
}
