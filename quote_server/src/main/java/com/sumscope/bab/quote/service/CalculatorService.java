package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.model.CalculatorRequestModel;
import com.sumscope.bab.quote.model.model.CalculatorResponseModel;

/**
 * 票据计算器service接口
 */
public interface CalculatorService {

	/**
	 * 根据前端用户设置进行计算，计算逻辑见需求
	 */
	CalculatorResponseModel calculateBills(CalculatorRequestModel parameter);

}
