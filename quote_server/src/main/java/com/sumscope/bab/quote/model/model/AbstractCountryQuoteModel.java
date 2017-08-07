package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.Constant;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * 全国报价单抽象类，该类组织了用于全国报价特殊的字段
 */
public class AbstractCountryQuoteModel extends AbstractQuoteModel {

	/**
	 * 是否是小金额票据
	 */
	private boolean minor;

	/**
	 * 国股价格
	 * 
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal ggPrice;

	/**
	 * 城商价格
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal csPrice;

	/**
	 * 农商价格
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal nsPrice;

	/**
	 * 农信价格
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal nxPrice;

	/**
	 * 农合价格
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal nhPrice;

	/**
	 * 村镇价格
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal czPrice;

	/**
	 * 外资价格
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal wzPrice;

	/**
	 * 财务价格
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal cwPrice;

	public boolean isMinor() {
		return minor;
	}

	public void setMinor(boolean minor) {
		this.minor = minor;
	}

	public BigDecimal getGgPrice() {
		return ggPrice;
	}

	public void setGgPrice(BigDecimal ggPrice) {
		this.ggPrice = ggPrice;
	}

	public BigDecimal getCsPrice() {
		return csPrice;
	}

	public void setCsPrice(BigDecimal csPrice) {
		this.csPrice = csPrice;
	}

	public BigDecimal getNsPrice() {
		return nsPrice;
	}

	public void setNsPrice(BigDecimal nsPrice) {
		this.nsPrice = nsPrice;
	}

	public BigDecimal getNxPrice() {
		return nxPrice;
	}

	public void setNxPrice(BigDecimal nxPrice) {
		this.nxPrice = nxPrice;
	}

	public BigDecimal getNhPrice() {
		return nhPrice;
	}

	public void setNhPrice(BigDecimal nhPrice) {
		this.nhPrice = nhPrice;
	}

	public BigDecimal getCzPrice() {
		return czPrice;
	}

	public void setCzPrice(BigDecimal czPrice) {
		this.czPrice = czPrice;
	}

	public BigDecimal getWzPrice() {
		return wzPrice;
	}

	public void setWzPrice(BigDecimal wzPrice) {
		this.wzPrice = wzPrice;
	}

	public BigDecimal getCwPrice() {
		return cwPrice;
	}

	public void setCwPrice(BigDecimal cwPrice) {
		this.cwPrice = cwPrice;
	}
}
