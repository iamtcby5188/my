package com.sumscope.bab.quote.facade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sumscope.bab.quote.model.dto.CalculatorRequestDto;

/**
 * 票据计算器Facade接口
 */
public interface CalculatorFacade {

	/**
	 * 根据前端用户设置进行计算，先验证传入的参数是否合法，再调用对应服务进行计算。
	 */
	void calculateBills(HttpServletRequest request, HttpServletResponse response, CalculatorRequestDto parameterDto) ;

}
