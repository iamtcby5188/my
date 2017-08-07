package com.sumscope.bab.quote.model.dto;

import java.util.List;
import com.sumscope.bab.quote.commons.enums.WEBSearchParameterMode;

/**
 * 对于前端所有的查询条件，由此dto进行提供。Web端程序不将查询条件固定写入代码。
 */
public class FilterDto {

	/**
	 * 某一查询条件的所有可选项目
	 */
	private List<WEBParameterEnumDto> conditions;

	/**
	 * 查询条件输入方式
	 */
	private WEBSearchParameterMode conditionMode;

	/**
	 * 查询条件的WEB显示名称，例如 “到期日期”
	 */
	private String conditionName;

	public List<WEBParameterEnumDto> getConditions() {
		return conditions;
	}

	public void setConditions(List<WEBParameterEnumDto> conditions) {
		this.conditions = conditions;
	}

	public WEBSearchParameterMode getConditionMode() {
		return conditionMode;
	}

	public void setConditionMode(WEBSearchParameterMode conditionMode) {
		this.conditionMode = conditionMode;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}
}
