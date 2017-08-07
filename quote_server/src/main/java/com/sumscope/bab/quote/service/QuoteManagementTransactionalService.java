package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fan.bai on 2017/1/4.
 * 使用事务控制的报价单更新接口，由Facade层调用
 */
public interface QuoteManagementTransactionalService<T extends AbstractQuoteModel> {
    /**
     * 使用事务控制的批量新增新报价。
     */
    @Transactional(value = Constant.BUSINESS_TRANSACTION_MANAGER)
    void insertNewQuotesInTransaction(List<T> models);

    /**
     * 使用事务控制的更新某一张报价单
     */
    @Transactional(value = Constant.BUSINESS_TRANSACTION_MANAGER)
    T updateQuoteInTransaction(T model);

    /**
     * 使用事务控制的对某一组报价单设置状态。
     */
    @Transactional(value = Constant.BUSINESS_TRANSACTION_MANAGER)
    void setQuoteStatusInTransaction(List<String> idList, BABQuoteStatus quoteStatus);

    /**
     * 导入报价单至历史数据库，使用历史库事务控制
     * @param model 报价单数据模型
     */
    @Transactional(value = Constant.HISTORY_TRANSACTION_MANAGER)
    void importQuoteToHistoryInTransaction(T model);

    /**
     * 当历史库导入成功后，删除业务库的报价单
     * @param model 报价单数据模型
     */
    @Transactional(value = Constant.BUSINESS_TRANSACTION_MANAGER)
    void deleteQuoteInBusinessInTransaction(T model);


}
