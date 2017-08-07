package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.FlushCacheEnum;
import com.sumscope.bab.quote.commons.enums.EditMode;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.commons.util.UUIDUtils;
import com.sumscope.bab.quote.dao.QuoteAdditionalInfoDao;
import com.sumscope.bab.quote.dao.QuoteDao;
import com.sumscope.bab.quote.externalinvoke.FlushCacheMsgSender;
import com.sumscope.bab.quote.externalinvoke.QuotePersistenceMsgSender;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.model.model.QuoteAdditionalInfoModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

//TODO:增加javadoc，同时实现接口中的方法（参照查询serviceimpl）并添加事务控制标注
abstract class AbstractQuoteManagementServiceImpl<T extends AbstractQuoteModel> implements QuoteManagementService<T>, QuoteManagementTransactionalService<T> {
    @Autowired
    private QuoteAdditionalInfoDao quoteAdditionalInfoDao;

    @Autowired
    private QuotePersistenceMsgSender quotePersistenceMsgSender;

    @Autowired
    private FlushCacheMsgSender flushCacheMsgSender;

    protected abstract QuoteDao getQuoteMainDao();

    protected abstract String concreteQuoteModelClassName();

    /**
     * 调用相关Dao接口将新增报价单实际写入数据库。返回写入后的报价单。
     */
    protected List<String> performInsertDBNewQuotes(List<T> models) {

        List<QuoteAdditionalInfoModel> additionInfos = new ArrayList<>();
        for (T model : models) {
            if (model == null) {
                continue;
            }

            formatQuoteModel(model);

            getAdditionInfos(additionInfos, model);
        }
        getQuoteMainDao().insertNewQuotes(models);

        //附加信息库
        quoteAdditionalInfoDao.insertNewInfos(additionInfos);

        List<String> ids = new ArrayList<>();
        for (T model : models) {
            ids.add(model.getId());
        }

        publishQuotesToBus(EditMode.INSERT, getQuoteModelFromDB(ids));

        return ids;
    }

    private void getAdditionInfos(List<QuoteAdditionalInfoModel> additionInfos, T model) {
        if (model.getAdditionalInfo() != null) {
            QuoteAdditionalInfoModel infoModel = model.getAdditionalInfo();
            infoModel.setQuoteId(model.getId());
            additionInfos.add(infoModel);
        }
    }

    //根据业务逻辑设置一些默认数据。
    protected void formatQuoteModel(T model) {
        if (model == null) {
            return;
        }

        model.setId(UUIDUtils.generatePrimaryKey());
        Date time = Calendar.getInstance().getTime();
        model.setCreateDate(time);
        model.setLastUpdateDate(time);

        if (model.getEffectiveDate() == null) {
            model.setEffectiveDate(time);
        }

        //设置失效日期为生效日期当日的23：00：00
        if (model.getExpiredDate() == null) {
            model.setExpiredDate(QuoteDateUtils.getExpiredTimeOfDate(time));
        }
    }

    /**
     * 调用相关Dao接口将修改的报价单实际写入数据库。更新最后更新日期。返回写入后的报价单。
     */
    protected T performUpdateOneQuote(T model) {
        model.setLastUpdateDate(Calendar.getInstance().getTime());
        getQuoteMainDao().updateOneQuote(model);

        List<QuoteAdditionalInfoModel> additionInfos = new ArrayList<>();
        getAdditionInfos(additionInfos, model);

        List<String> ids = new ArrayList<>();
        ids.add(model.getId());

        //更新附加信息库
        quoteAdditionalInfoDao.deleteAllInfoByQuoteId(ids);
        quoteAdditionalInfoDao.insertNewInfos(additionInfos);

        List<T> result = getQuoteModelFromDB(ids);
        if (!result.isEmpty()) {
            publishQuotesToBus(EditMode.UPDATE,  result);
            return result.get(0);
        } else {
            return null;
        }
    }

    //excel批量导入时，要把对应的additionalInfo字段的值一起推送给web 端
    private List<T> getQuoteModelFromDB(List<String> ids) {
        List<T> list = getQuoteMainDao().retrieveQuoteByIDs(ids);
        List<QuoteAdditionalInfoModel> quoteAdditionalInfoModels = quoteAdditionalInfoDao.retrieveAdditionalInfoByIds(ids);
        for (T model : list) {
            String id = model.getId();
            QuoteAdditionalInfoModel additionalInfoModel = getModelFromListByID(quoteAdditionalInfoModels, id);
            model.setAdditionalInfo(additionalInfoModel);
        }
        return list;
    }

    private QuoteAdditionalInfoModel getModelFromListByID(List<QuoteAdditionalInfoModel> quoteAdditionalInfoModels, String id) {
        for (QuoteAdditionalInfoModel model : quoteAdditionalInfoModels) {
            if (model.getQuoteId().equals(id)) {
                return model;
            }
        }
        return null;
    }

    /**
     * 调用相关Dao接口将报价单的状态改变实际写入数据库。返回写入后的报价单。
     */
    protected List<T> performSetQuoteStatus(List<String> ids, BABQuoteStatus status) {
        getQuoteMainDao().updateQuotesStatus(ids, status, Calendar.getInstance().getTime());

        List<T> results = getQuoteModelFromDB(ids);

        publishQuotesToBus(EditMode.UPDATE,  results);
        return results;
    }

    /**
     * 将报价单发布到系统总线上。报价单创建后或者修改后需要将报价单发布到总线上，以支持后续功能。比如发布WebSocket推送信息或者外部系统获取行情数据
     * 同时发送更新缓存消息
     */
    protected void publishQuotesToBus(EditMode editMode,  List<T> models) {
        quotePersistenceMsgSender.sendQuoteChangeMsg(editMode, concreteQuoteModelClassName(), models);
        //发布更新缓存命令
        flushCacheMsgSender.sendMsgWithoutObject(getFluchCacheTarget());

    }

    protected abstract FlushCacheEnum getFluchCacheTarget();


    @Override
    public void insertNewQuotesInTransaction(List<T> models) {
        insertNewQuotes(models);
    }

    @Override
    public T updateQuoteInTransaction(T model) {
        return updateQuote(model);
    }

    @Override
    public void setQuoteStatusInTransaction(List<String> idList, BABQuoteStatus quoteStatus) {
        setQuoteStatus(idList, quoteStatus);
    }

    @Override
    public List<String> insertNewQuotes(List<T> models) {
        // TODO: 2016/12/26 报价时多个字段需判断是否合法及时间生成
        return performInsertDBNewQuotes(models);
    }

    @Override
    public T updateQuote(T model) {
        return performUpdateOneQuote(model);
    }

    @Override
    public void setQuoteStatus(List<String> idList, BABQuoteStatus quoteStatus) {
        performSetQuoteStatus(idList, quoteStatus);
    }

    @Override
    public void importQuoteToHistoryInTransaction(T model){
        importToHistory(model);
    }

    @Override
    public void deleteQuoteInBusinessInTransaction(T model){
        deleteQuoteByIds(Collections.singletonList(model.getId()));
        publishQuotesToBus(EditMode.DELETE, Collections.singletonList(model));
    }

    @Override
    public void importToHistory(T model) {
        getQuoteMainDao().deleteQuoteFromHistoryById(model.getId());
        quoteAdditionalInfoDao.deleteInfosFromHistoryByQuoteId(model.getId());

        getQuoteMainDao().importToHistory(model);
        quoteAdditionalInfoDao.importToHistory(model.getAdditionalInfo());

    }

    @Override
    public void deleteQuoteByIds(List<String> ids) {
        getQuoteMainDao().deleteQuoteByIds(ids);
        quoteAdditionalInfoDao.deleteAllInfoByQuoteId(ids);
    }
}