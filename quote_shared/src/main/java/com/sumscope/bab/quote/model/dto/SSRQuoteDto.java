package com.sumscope.bab.quote.model.dto;

import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

public class SSRQuoteDto extends AbstractQuoteDto {

	/**
	 * 报价区域，新增时如果该类不为null，则code必须存在并检查合法性
	 */
	@Valid
	private QuoteProvinceDto quoteProvinces;

	/**
	 * 报价价格类型，银票是不可空
	 */
	private BABQuotePriceType quotePriceType;

	/**
	 * 报价机构，商票时不可空
	 */
	private AcceptingCompanyDto acceptingHouse;

	/**
	 * 票据到期日期
	 */
	private Date dueDate;

	/**
	 * 报价价格，可空
	 */
	private String price;

	/**
	 * 报价金额
	 */
	private BigDecimal amount;

	/**
	 * Base64编码的票面图片数据
	 * 本字段仅用于前端向服务端提交修改时设置，并更新对应数据库字段。
	 * 当Dto用于返回报价单记录时，为节省网络带宽，不设置该字段，仅设置containsImg字段用于前端显示相应图标
	 */
	@Size(max = 200 * 1024)
	private String base64Img;

	/**
	 * 表明是否存在对应票面图片信息，该字段用于前端在页面上展示报价记录是否有图片的图标信息
	 * true： 页面上展示图标，表明存在图片，可点击观看
	 * false：不展示图标，没有图片
	 * 本字段仅在服务端返回前端时生效，前端提交修改时无需对本字段设值
	 */
	private boolean containsImg;


	public QuoteProvinceDto getQuoteProvinces() {
		return quoteProvinces;
	}

	public void setQuoteProvinces(QuoteProvinceDto quoteProvinces) {
		this.quoteProvinces = quoteProvinces;
	}

	public BABQuotePriceType getQuotePriceType() {
		return quotePriceType;
	}

	public void setQuotePriceType(BABQuotePriceType quotePriceType) {
		this.quotePriceType = quotePriceType;
	}

	public AcceptingCompanyDto getAcceptingHouse() {
		return acceptingHouse;
	}

	public void setAcceptingHouse(AcceptingCompanyDto acceptingHouse) {
		this.acceptingHouse = acceptingHouse;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
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

	public boolean isContainsImg() {
		return containsImg;
	}

	public void setContainsImg(boolean containsImg) {
		this.containsImg = containsImg;
	}

}
