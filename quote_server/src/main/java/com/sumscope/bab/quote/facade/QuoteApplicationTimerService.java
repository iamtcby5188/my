package com.sumscope.bab.quote.facade;

import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;
import com.sumscope.bab.quote.model.model.QuotePriceTrendsModel;
import com.sumscope.bab.quote.service.*;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 每日定时（由配置文件决定）服务：
 * 所有定时服务都有本类触发
 */
@Service
@Configurable
@EnableScheduling
public class QuoteApplicationTimerService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SSRQuoteQueryService ssrQuoteQueryService;

	@Autowired
	private SSRQuoteManagementTransactionalService ssrQuoteManagementService;

	@Autowired
	private SSCQuoteQueryService sscQuoteQueryService;

	@Autowired
	private SSCQuoteManagementTransactionalService sscQuoteManagementService;

	@Autowired
	private NPCQuoteQueryService npcQuoteQueryService;

	@Autowired
	private NPCQuoteManagementTransactionalService npcQuoteManagementService;

	@Autowired
	private QuotePriceTrendsCalculationResultsCacheService quotePriceTrendsCalculationResultsCacheService;

	@Autowired
	private QuotePriceTrendsSearchAndCalculationService quotePriceTrendsSearchAndCalculationService;


	/**
	 * 每日将把过期报价单归档入历史数据库，触发时间由配置文件决定
	 */
	@Scheduled(cron = "${application.schedule.quotes.achieve}")
	public void invokeQuotesAchieveService() {
		if(checkPrimaryNode()){
			LogStashFormatUtil.logInfo(logger,"报价数据归档开始！");
			QueryQuotesParameterModel queryQuotesParameterModel=new QueryQuotesParameterModel();
			queryQuotesParameterModel.setExpiredQuotesDate(new Date());
			List<BABQuoteStatus> quoteStatusList = new ArrayList<>();
			for(BABQuoteStatus babQuoteStatus:BABQuoteStatus.values()){
				quoteStatusList.add(babQuoteStatus);
			}
			queryQuotesParameterModel.setQuoteStatusList(quoteStatusList);
			queryQuotesParameterModel.setPaging(false);
			importBaseToHistory(queryQuotesParameterModel, ssrQuoteQueryService, ssrQuoteManagementService);//归档SSR数据
			importBaseToHistory(queryQuotesParameterModel, sscQuoteQueryService, sscQuoteManagementService);//归档SSC数据
			importBaseToHistory(queryQuotesParameterModel, npcQuoteQueryService, npcQuoteManagementService);//归档NPC数据

		}
	}

	private void importBaseToHistory(QueryQuotesParameterModel queryQuotesParameterModel,QuotesQueryService queryService,QuoteManagementTransactionalService quoteManagement){
		List<? extends AbstractQuoteModel> list = queryService.retrieveQuotesByCondition(queryQuotesParameterModel);
		if(list!=null && list.size()>0){
			for(AbstractQuoteModel t : list){
				try {
					quoteManagement.importQuoteToHistoryInTransaction(t);
					quoteManagement.deleteQuoteInBusinessInTransaction(t);
				} catch (Exception e) {
					LogStashFormatUtil.logError(logger,"数据归档失败！",e);
				}
			}
		}
	}

	/**
	 * 价格统计信息将在工作时间每N分钟触发一次，触发的时间条件由配置文件决定
	 */
	@Scheduled(cron = "${application.schedule.pricetrends.calculation}")
	public void invokeTimerPriceTrendsCalculation(){
		LogStashFormatUtil.logInfo(logger,"价格统计开始计算！");
		quotePriceTrendsCalculationResultsCacheService.timerCalculation();
	}


	/**
	 * 价格统计信息每日将归档一次，将本日的价格统计数据写入数据库。该服务一天触发一次，触发条件由配置文件决定
	 */
	@Scheduled(cron = "${application.schedule.pricetrends.achieve}")
	public void invokePriceTrendsAchieveService(){
		if(checkPrimaryNode()){
			LogStashFormatUtil.logInfo(logger,"价格统计信息数据归档开始！");
			Date calculationTimeOfDate = QuoteDateUtils.getCalculationTimeOfDate(Calendar.getInstance().getTime());
			List<QuotePriceTrendsModel> quotePriceTrendsModels = quotePriceTrendsSearchAndCalculationService.calculateCurrentPriceTrends(
					calculationTimeOfDate);
			if(quotePriceTrendsModels!=null && quotePriceTrendsModels.size() >0){
				quotePriceTrendsSearchAndCalculationService.persistentPriceTrends(calculationTimeOfDate,quotePriceTrendsModels);
			}
		}
	}

	/**
	 * 对于归档任务，自多实例运行时只要一个实例处理即可，因此使用一个启动命令设置为主节点承担这个任务
	 * @return primary=True设置为启动命令时返回true。
	 */
	private boolean checkPrimaryNode() {
		return ApplicationEnviromentConstant.isPrimary();
	}
}
