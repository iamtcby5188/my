package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.model.dto.BABInitDto;
import com.sumscope.bab.quote.model.dto.LoginUserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 前端应用相关功能facade。例如前端应用第一次打开时通过该接口获取必要的数据并保留在前端，或者定时获取新的token。
 */
public interface ApplicationFacade {

	/**
	 * 获取报价数据浏览页面初始化数据
	 */
	void getQuoteViewInitData(HttpServletRequest request, HttpServletResponse response,BABInitDto sign) ;

	/**
	 *用户登录
     */
	void loginUser(HttpServletRequest request, HttpServletResponse response,LoginUserDto user) ;

	/**
	 * SSR 报价管理界面初始化方法
	 */
	void getSSRQuoteManagementInitData(HttpServletRequest request, HttpServletResponse response,BABInitDto sign);

    /**
     * SSC 报价管理界面初始化方法
     */
    void getSSCQuoteManagementInitData(HttpServletRequest request, HttpServletResponse response,BABInitDto sign);

    /**
     * NPC 报价管理界面初始化方法
     */
    void getNPCQuoteManagementInitData(HttpServletRequest request, HttpServletResponse response,BABInitDto sign);

	/**
	 * 生成虚拟开发测试报价单数据
	 */
	void generateMockTestData(HttpServletRequest request, HttpServletResponse response, Map number);

}
