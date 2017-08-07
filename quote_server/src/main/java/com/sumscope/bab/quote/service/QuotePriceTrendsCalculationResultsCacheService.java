package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * 按固定时间间隔（间隔由配置文件决定）进行当日价格统计信息计算，并将结果缓存于内存中。每次计算都将先清空缓存再将结果写入。
 * 这样做的好处在于用户查询当前价格统计信息将足够快。
 */
@Component
public class QuotePriceTrendsCalculationResultsCacheService {
    @Autowired
	private QuotePriceTrendsSearchAndCalculationService quotePriceTrendsSearchAndCalculationService;
	/**
	 * 每次计算的结果保存在这个属性中用于实时向前端提供结果集。
	 */
	private List<QuotePriceTrendsModel> currentPriceTrends;

	/**
	 * 由配置文件确定的时间间隔定时调用，调用时进行价格统计信息计算并将结果更新缓存。
	 */
	public void timerCalculation() {
		currentPriceTrends = quotePriceTrendsSearchAndCalculationService.calculateCurrentPriceTrends(Calendar.getInstance().getTime());
	}

	/**
	 * 获取当前保留在缓存中的价格统计信息结果
	 */
	public List<QuotePriceTrendsModel> getCurrentResults() {
		return currentPriceTrends;
	}

}
