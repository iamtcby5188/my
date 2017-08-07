package com.sumscope.bab.quote.model.model;

import com.sumscope.bab.quote.commons.Constant;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 全国直贴报价单
 */
public class SSCQuoteModel extends AbstractCountryQuoteModel implements Serializable {

	/**
	 * 区域code，可空
	 */
	private String provinceCode;

	/**
	 * 有保函价格，可空
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal ybhPrice;

	/**
	 * 无保函价格，可空
	 */
	@DecimalMax(value = Constant.PRICE_MAX_VALUE)
	@DecimalMin(value = Constant.PRICE_MIN_VALUE)
	private BigDecimal wbhPrice;

	public BigDecimal getYbhPrice() {
		return ybhPrice;
	}

	public void setYbhPrice(BigDecimal ybhPrice) {
		this.ybhPrice = ybhPrice;
	}

	public BigDecimal getWbhPrice() {
		return wbhPrice;
	}

	public void setWbhPrice(BigDecimal wbhPrice) {
		this.wbhPrice = wbhPrice;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
}
