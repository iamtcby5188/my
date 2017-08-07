package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.InsertGroup;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SSRQuoteModel extends AbstractQuoteModel implements Serializable {

	/**
	 * 区域code，可空
	 */
	private String provinceCode;

	/**
	 * 银票报价时不可为空
	 */
	private BABQuotePriceType quotePriceType;

	/**
	 * 商票报价时承兑方不可空，既该字段与附加信息表对应的承兑方名称字段不可同时为空
	 */
	@Length(max=32)
	private String acceptingHouseId;

	/**
	 * 票据到期日期，可空
	 */
	private Date dueDate;

	/**
	 * 报价价格，可空
	 */
    @DecimalMax(value = Constant.PRICE_MAX_VALUE)
    @DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal price;

	/**
	 * 报价金额，可空
	 */
	//不加groups前,不知为何原因update时，如果为空也验证了，所以现在加个了groups
	@DecimalMax(value = Constant.AMOUNT_MAX_VALUE,groups=InsertGroup.class)
    @DecimalMin(value = Constant.AMOUNT_MIN_VALUE,groups=InsertGroup.class)
	private BigDecimal amount;

	/**
	 * Base64编码的图片数据信息，可空
	 */
	@Size(max = 200 * 1024)
	private String base64Img;

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public BABQuotePriceType getQuotePriceType() {
		return quotePriceType;
	}

	public void setQuotePriceType(BABQuotePriceType quotePriceType) {
		this.quotePriceType = quotePriceType;
	}

	public String getAcceptingHouseId() {
		return acceptingHouseId;
	}

	public void setAcceptingHouseId(String acceptingHouseId) {
		this.acceptingHouseId = acceptingHouseId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBase64Img() {
		return base64Img;
	}

	public void setBase64Img(String base64Img) {
		this.base64Img = base64Img;
	}
}
