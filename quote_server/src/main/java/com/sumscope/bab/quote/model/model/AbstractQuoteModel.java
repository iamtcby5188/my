package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.commons.enums.Direction;
import com.sumscope.bab.quote.commons.InsertGroup;
import com.sumscope.bab.quote.commons.UpdateGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 *  对应报价单的模型 
 *   
 * 
 */
public abstract class AbstractQuoteModel implements Serializable {

	/**
	 * 报价主表ID
	 */
	@NotNull
	@Length(max=32)
	private String id;

	/**
	 * 方向，非空
	 */
	@NotNull
	private Direction direction;

	/**
	 * 票据类型：银票、商票
	 * 非空
	 */
	@NotNull
	private BABBillType billType;

	/**
	 * 票据介质：纸票，电票
	 * 非空
	 */
	@NotNull
	private BABBillMedium billMedium;

	/**
	 * 备注
	 */
	@Length(max=256)
	private String memo;

	/**
	 * 报价机构ID，若该字段为空则使用companyName字段获取报价机构名称。此时在转换时无需获取IAM对应机构。
	 */
	@Length(max=32)
	private String quoteCompanyId;

	/**
	 * 联系人ID，若该字段为空则使用contactName和contactTelephone字段获取联系人名称及联系方式。此时在转换时无需获取IAM对应用户。
	 */
	@Length(max=32)
	private String contactId;

	/**
	 * 操作人ID，非空，为IAM系统对应用户ID
	 * 
	 */
	@NotNull
	@Length(max=32)
	private String operatorId;

	/**
	 * 报价单生成时刻
	 */
	@Null(groups = UpdateGroup.class)
	@NotNull(groups = InsertGroup.class)
	private Date createDate;

	/**
	 * 报价单最后修改时刻
	 */
    @Null(groups = UpdateGroup.class)
    @NotNull(groups = InsertGroup.class)
	private Date lastUpdateDate;

	/**
	 * 报价单生效时刻
	 */
    @Null(groups = UpdateGroup.class)
    @NotNull(groups = InsertGroup.class)
	private Date effectiveDate;

	/**
	 * 报价单过期时刻
	 */
    @Null(groups = UpdateGroup.class)
    @NotNull(groups = InsertGroup.class)
	private Date expiredDate;

	/**
	 * 报价状态
	 */
	@NotNull
	private BABQuoteStatus quoteStatus;

	/**
	 * 是否使用附加信息。既报价机构，联系人等由字符串信息提供而不用ID形式使用已经存在的机构、用户信息。本字段为true时，quoteCompanyName,contactName,contactTelphone字段才生效
	 */
	private QuoteAdditionalInfoModel additionalInfo;

    private boolean isReadOnly = false;

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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getQuoteCompanyId() {
		return quoteCompanyId;
	}

	public void setQuoteCompanyId(String quoteCompanyId) {
		this.quoteCompanyId = quoteCompanyId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
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

	public QuoteAdditionalInfoModel getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(QuoteAdditionalInfoModel additionalInfo) {
		this.additionalInfo = additionalInfo;
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

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }
}
