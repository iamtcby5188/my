package com.sumscope.bab.quote.model.dto;

/**
 * 全国报价单Dto
 */
public class AbstractCountryQuoteDto extends AbstractQuoteDto {

	/**
	 * 是否是小金额票据
	 */
	private boolean minor;

	/**
	 * 国股价格
	 * 
	 */
	private String ggPrice;

	/**
	 * 城商价格
	 */
	private String csPrice;

	/**
	 * 农商价格
	 */
	private String nsPrice;

	/**
	 * 农信价格
	 */
	private String nxPrice;

	/**
	 * 农合价格
	 */
	private String nhPrice;

	/**
	 * 村镇价格
	 */
	private String czPrice;

	/**
	 * 外资价格
	 */
	private String wzPrice;

	/**
	 * 财务价格
	 */
	private String cwPrice;

	public boolean isMinor() {
		return minor;
	}

	public void setMinor(boolean minor) {
		this.minor = minor;
	}

	public String getGgPrice() {
		return ggPrice;
	}

	public void setGgPrice(String ggPrice) {
		this.ggPrice = ggPrice;
	}

	public String getCsPrice() {
		return csPrice;
	}

	public void setCsPrice(String csPrice) {
		this.csPrice = csPrice;
	}

	public String getNsPrice() {
		return nsPrice;
	}

	public void setNsPrice(String nsPrice) {
		this.nsPrice = nsPrice;
	}

	public String getNxPrice() {
		return nxPrice;
	}

	public void setNxPrice(String nxPrice) {
		this.nxPrice = nxPrice;
	}

	public String getNhPrice() {
		return nhPrice;
	}

	public void setNhPrice(String nhPrice) {
		this.nhPrice = nhPrice;
	}

	public String getCzPrice() {
		return czPrice;
	}

	public void setCzPrice(String czPrice) {
		this.czPrice = czPrice;
	}

	public String getWzPrice() {
		return wzPrice;
	}

	public void setWzPrice(String wzPrice) {
		this.wzPrice = wzPrice;
	}

	public String getCwPrice() {
		return cwPrice;
	}

	public void setCwPrice(String cwPrice) {
		this.cwPrice = cwPrice;
	}
}
