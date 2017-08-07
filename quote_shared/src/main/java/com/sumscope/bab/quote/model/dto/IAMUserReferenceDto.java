package com.sumscope.bab.quote.model.dto;


/**
 * IAM用户相关Dto
 */
public class IAMUserReferenceDto {

	/**
	 * IAM系统用户ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 手机
	 */
	private String mobileTel;

	/**
	 * qq号码
	 */
	private String qq;

	private String userName;

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileTel() {
		return mobileTel;
	}

	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
