package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.dao.QuoteAdditionalInfoDao;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;
import com.sumscope.bab.quote.model.model.QuoteAdditionalInfoModel;
import com.sumscope.bab.quote.dao.QuoteDao;
import java.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 调用对应Dao接口读取实际数据库报价单数据。
 * 各子类则维护对应的Dao接口。本抽象类通过protected方法获取Dao接口完成业务功能。
 */
public abstract class AbstractQuoteQueryServiceImpl<T extends AbstractQuoteModel> implements QuotesQueryService<T> {
	@Autowired
	private QuoteAdditionalInfoDao additionInfoDao;

	/**
	 * 由子类完成该抽象方法。获取对应的Dao接口。
	 */
	protected abstract QuoteDao getQuoteMainDao();

	/**
	 * 调用对应方法获取相应的Dao接口先获取报价单主表数据，再根据主表ID列表获取报价单明细数据以及省份数据。最后组合所有数据获取报价单模型列表。
	 */
	public List<T> retrieveQuotesByCondition(QueryQuotesParameterModel parameter) {
		List<T> results = getQuoteMainDao().retrieveQuotesByCondition(parameter);
		//Dao层使用了缓存，又由于附加信息Addtional_Info的信息会通过本方法写入model，对于缓存操作来说既是在同一个实例上的
		//操作，在并发情况下会存在线程安全问题。因此使用以下方法将只读的缓存存储对象更新为可写的对象。
		results = copyToWritableResults(results);
        fillAdditionalInfos(results);

        for (T model :  results) {
            if (isQuoteReadOnly(model)) {
                model.setReadOnly(true);
            }
        }
		return results;
	}

    private void fillAdditionalInfos(List<T> models) {
        List<String> ids = new ArrayList<>();

        Map<String, T> map = new HashMap<>();
        for (T model : models) {
            ids.add(model.getId());
            map.put(model.getId(), model);
        }

        if(ids.size() > 0){ //没有报价情况
            List<QuoteAdditionalInfoModel> infoModels = additionInfoDao.retrieveAdditionalInfoByIds(ids);
            for (QuoteAdditionalInfoModel infoModel : infoModels) {
                T model = map.get(infoModel.getQuoteId());
                if (model != null) {
                    model.setAdditionalInfo(infoModel);
                }
            }
        }
    }

	@Override
	public List<T> retrieveHistoryQuotesByCondition(QueryQuotesParameterModel parameter) {
		List<T> results = getQuoteMainDao().retrieveHistoryQuotesByCondition(parameter);
        results = copyToWritableResults(results);
        fillAdditionalInfos(results);

        for (T model :  results) {
            model.setReadOnly(true);
        }
        return results;
	}

	private List<T> copyToWritableResults(List<T> models) {
		List<T> results = new ArrayList<>();
		if(models != null && models.size()>0){
			for(T model:models){
				T newModel = getModelInstance();
				BeanUtils.copyProperties(model,newModel);
				results.add(newModel);
			}
		}
		return results;
	}

	protected abstract T getModelInstance();

	@Override
	public List<T> retrieveQuoteByIDs(List<String> ids) {
		return getQuoteMainDao().retrieveQuoteByIDs(ids);
	}

	/**
	 * 调用相应的Dao接口获取所有报价机构ID列表
	 */
	@Override
	public List<String> retrieveCurrentQuotesCompanies() {
		return getQuoteMainDao().retrieveCurrentQuotesCompanyIds();
	}
    /**
     * 调用相应的Dao接口获取所有报价机构名称列表
     */
    @Override
    public List<String> retrieveCurrentQuotesCompaniesNames() {
        return getQuoteMainDao().retrieveCurrentQuotesCompanyNames();
    }

    private boolean isQuoteReadOnly(T model) {
        if (model.getQuoteStatus() != null) {
            switch (model.getQuoteStatus()) {
//                case DLD:
//                case CAL:
                case DEL:
                    return true;
            }
        }

        if (model.getExpiredDate() != null && model.getExpiredDate().before(Calendar.getInstance().getTime())) {
            return true;
        }

        return false;
    }

    @Override
    public void flushCache() {
        getQuoteMainDao().flushDaoCache();
        additionInfoDao.flushDaoCache();
    }
}

