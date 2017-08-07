package com.sumscope.bab.quote.model.dto;

public class PriceMarginPriceTrendsParameterDto extends DatePeriodDto {

	private boolean ibo001Wanted;

	private boolean r001Wanted;

	public boolean isIbo001Wanted() {
		return ibo001Wanted;
	}

	public void setIbo001Wanted(boolean ibo001Wanted) {
		this.ibo001Wanted = ibo001Wanted;
	}

	public boolean isR001Wanted() {
		return r001Wanted;
	}

	public void setR001Wanted(boolean r001Wanted) {
		this.r001Wanted = r001Wanted;
	}
}
