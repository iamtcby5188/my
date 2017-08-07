package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.InsertGroup;
import com.sumscope.bab.quote.commons.UpdateGroup;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.Direction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

public abstract class AbstractQuoteDto {

	/**
	 * 报价单主键ID
	 */
	@Null(groups = InsertGroup.class)
	@NotNull(groups = UpdateGroup.class)
	private String id;

	/**
	 * 方向
	 */
	//@NotNull
	private Direction direction;

	/**
	 * 银行票据、商业票据
	 */
	@NotNull
	private BABBillType billType;

	/**
	 * 纸票or电票
	 */
	@NotNull
	private BABBillMedium billMedium;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 报价单机构信息，从IAM系统获取。新增报价时设置ID即可，需验证合法性
	 */
	private IAMCompanyReferenceDto quoteCompanyDto;

	/**
	 * 联系人，由IAM系统提供详细信息。报价时提供ID即可，需验证合法性
	 */
	private IAMUserReferenceDto contactDto;

	/**
	 * 报价操作人ID，应为当前报价用户ID，报价时置空，从api调用接口获取当前用户信息写入
	 */
	private String operatorId;

	/**
	 * 生成日期。报价时置空
	 */
	private Date createDate;

	/**
	 * 最后更新日期，报价时置空
	 */
	private Date lastUpdateDate;

	/**
	 * 生效日期，报价时必要信息。需验证合法性
	 */
	//@Future
	private Date effectiveDate;

	/**
	 * 过期日期，报价时可置空，置空时默认为生效日期当天晚上
	 */
	private Date expiredDate;

	/**
	 * 报价单状态。新增报价时可置空。
	 */
	private BABQuoteStatus quoteStatus;

	/**
	 * 是否使用附加信息。既报价机构，联系人等由字符串信息提供而不用ID形式使用已经存在的机构、用户信息。本字段为true时，quoteCompanyName,contactName,contactTelphone字段才生效
	 * 在报价时,如果使用AdditionalInfo字段,本字段必须设置为true
	 */
	private boolean containsAdditionalInfo;

	/**
	 * 报价附加信息。既报价机构，联系人等由字符串信息提供而不用ID形式使用已经存在的机构、用户信息。
	 */
	private QuoteAdditionalInfoDto additionalInfo;
	/**
	 * 标记是否是历史数据 true为是历史库数据
	 */
	private boolean fromHistory=false;

	/**
	 * 只读标记,前台用来判断是否可以更新报价
     */
    private  boolean readOnly = false;

	/**
	 * 设置报价token
	 */
	private String quoteToken;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public BABBillType getBillType() {
		return billType;
	}

	public void setBillType(BABBillType billType) {
		this.billType = billType;
	}

	public BABBillMedium getBillMedium() {
		return billMedium;
	}

	public void setBillMedium(BABBillMedium billMedium) {
		this.billMedium = billMedium;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public IAMCompanyReferenceDto getQuoteCompanyDto() {
		return quoteCompanyDto;
	}

	public void setQuoteCompanyDto(IAMCompanyReferenceDto quoteCompanyDto) {
		this.quoteCompanyDto = quoteCompanyDto;
	}

	public IAMUserReferenceDto getContactDto() {
		return contactDto;
	}

	public void setContactDto(IAMUserReferenceDto contactDto) {
		this.contactDto = contactDto;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public BABQuoteStatus getQuoteStatus() {
		return quoteStatus;
	}

	public void setQuoteStatus(BABQuoteStatus quoteStatus) {
		this.quoteStatus = quoteStatus;
	}

	public boolean isContainsAdditionalInfo() {
		return containsAdditionalInfo;
	}

	public void setContainsAdditionalInfo(boolean containsAdditionalInfo) {
		this.containsAdditionalInfo = containsAdditionalInfo;
	}

	public QuoteAdditionalInfoDto getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(QuoteAdditionalInfoDto additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public boolean isFromHistory() {
		return fromHistory;
	}

	public void setFromHistory(boolean fromHistory) {
		this.fromHistory = fromHistory;
	}

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

	public String getQuoteToken() {
		return quoteToken;
	}

	public void setQuoteToken(String quoteToken) {
		this.quoteToken = quoteToken;
	}
}
