package com.sumscope.bab.quote.dao;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;
import com.sumscope.bab.quote.model.model.QuoteAdditionalInfoModel;
import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

/**
 * Created by shaoxu.wang on 2016/12/13.
 * 抽象实现类，本类负责进行实际的Dao层操作
 */
public abstract class AbstractQuoteDaoImpl<T extends AbstractQuoteModel> implements QuoteDao<T> {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier(value = Constant.BUSINESS_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    @Qualifier(value = Constant.HISTORY_SQL_SESSION_TEMPLATE)
    private SqlSessionTemplate hisSqlSessionTemplate;

    private SqlSessionTemplate getBusinessTemplate() {
        return sqlSessionTemplate;
    }

    private SqlSessionTemplate getHisSqlSessionTemplate() {
        return hisSqlSessionTemplate;
    }


    @Override
    public List<String> retrieveCurrentQuotesCompanyIds() {
        return getBusinessTemplate().selectList(getRetrieveCurrentQuotesCompanyIdsString());
    }

    @Override
    public List<String> retrieveCurrentQuotesCompanyNames() {
        return getBusinessTemplate().selectList(getRetrieveCurrentQuotesCompanyNamesString());
    }

    protected abstract String getRetrieveCurrentQuotesCompanyNamesString();

    protected abstract String getRetrieveCurrentQuotesCompanyIdsString();

    @Override
    public List<T> retrieveQuotesByCondition(QueryQuotesParameterModel parameter) {
        LogStashFormatUtil.logDebug(logger,"retrieveQuotesByCondition被调用，进行数据库查询。");
        normalizeParameter(parameter);

        return getBusinessTemplate().selectList(getRetrieveQuotesByConditionsString(), parameter);

    }

    @Override
    public List<T> retrieveHistoryQuotesByCondition(QueryQuotesParameterModel parameter) {
        LogStashFormatUtil.logDebug(logger,"retrieveHistoryQuotesByCondition，进行数据库查询。");
        normalizeParameter(parameter);

        return getHisSqlSessionTemplate().selectList(getRetrieveQuotesByConditionsString(), parameter);
    }

    protected abstract String getRetrieveQuotesByConditionsString();

    @Override
    public List<T> retrieveQuoteByIDs(List<String> ids) {
        return getBusinessTemplate().selectList(getRetrieveQuoteByIDsString(), ids);

    }

    protected abstract String getRetrieveQuoteByIDsString();

    @Override
    public void insertNewQuotes(List<T> models) {
        List<ValidationExceptionDetails> exceptionDetails = checkValidateModels(models);
        ValidationUtil.setValidationException(exceptionDetails);

        getBusinessTemplate().insert(getInsertNewQuotesString(), models);
    }

    protected abstract String getInsertNewQuotesString();

    private List<ValidationExceptionDetails> checkValidateModels(List<T> models) {
        List<ValidationExceptionDetails> exceptionDetails = new ArrayList<>();
        for (T model : models) {
            exceptionDetails.addAll(checkValidateModel(model));
        }

        return exceptionDetails;
    }

    private List<ValidationExceptionDetails> checkValidateModel(T model) {
        List<ValidationExceptionDetails> details = ValidationUtil.validateModelAndCollectConstraintsViolations(model);

        QuoteAdditionalInfoModel infoModel = model.getAdditionalInfo();

        validateCompany(model, details, infoModel);

        validateContact(model, details, infoModel);

        return details;
    }

    private void validateContact(T model, List<ValidationExceptionDetails> details, QuoteAdditionalInfoModel infoModel) {
        String contactId = model.getContactId();
        //若contactId和additionalInfo的contactName同时为空, 则报错
        if (StringUtils.isBlank(contactId) && (infoModel == null || StringUtils.isBlank(infoModel.getContactName()))) {
            details.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "contactId", "联系人ID和联系人名称同时为空!"));
        }

        //若contactId和additionalInfo的contactName同时不为空, 则报错
        if (StringUtils.isNotBlank(contactId) && (infoModel != null && StringUtils.isNotBlank(infoModel.getContactName()))) {
            details.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "contactId", "联系人ID和联系人名称同时有值!"));
        }
    }

    private void validateCompany(T model, List<ValidationExceptionDetails> details, QuoteAdditionalInfoModel infoModel) {
        String companyId = model.getQuoteCompanyId();
        //若quoteCompanyId和additionalInfo的quoteCompanyName同时为空, 则报错
        if (StringUtils.isBlank(companyId) && (infoModel == null || StringUtils.isBlank(infoModel.getQuoteCompanyName()))) {
            details.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "quoteCompanyId", "报价机构ID和报价机构名称同时为空!"));
        }

        //若quoteCompanyId和additionalInfo的quoteCompanyName同时不为空, 则报错
        if (StringUtils.isNotBlank(companyId) && (infoModel != null && StringUtils.isNotBlank(infoModel.getQuoteCompanyName()))) {
            details.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "quoteCompanyId", "报价机构ID和报价机构名称同时有值!"));
        }
    }

    @Override
    public void updateOneQuote(T model) {
        List<ValidationExceptionDetails> exceptionDetails = checkValidateModel(model);
        ValidationUtil.setValidationException(exceptionDetails);

        getBusinessTemplate().update(getUpdateOneQuoteString(), model);
    }

    protected abstract String getUpdateOneQuoteString();

    @Override
    public void updateQuotesStatus(List<String> ids, BABQuoteStatus status, Date updateTime) {
        Map<String, Object> mp = new HashMap<>();
        mp.put("list", ids);
        mp.put("status", status);
        mp.put("updateTime", updateTime);
        getBusinessTemplate().update(getUpdateQuotesStatusString(), mp);
    }

    protected abstract String getUpdateQuotesStatusString();

    @Override
    public void importToHistory(AbstractQuoteModel model) {
        getHisSqlSessionTemplate().insert(getImportToHistoryString(), model);
    }

    protected abstract String getImportToHistoryString();

    @Override
    public void deleteQuoteByIds(List<String> ids) {
        getBusinessTemplate().delete(getDeleteQuoteByIdsString(), ids);
    }

    protected abstract String getDeleteQuoteByIdsString();

    @Override
    public void deleteQuoteFromHistoryById(String id) {
        getHisSqlSessionTemplate().delete(getDeleteQuoteFromHistoryByIdString(), id);
    }

    protected abstract String getDeleteQuoteFromHistoryByIdString();

    /**
     * 在XML中，我们使用 id == null 来判断是否设置了列表值，并进行where语句的组装。 在xml文件中不再进行
     * .size() == 0 的判断，因为在某些并发情况下，对size的判断会出现无法解释的空指针异常。
     * 因此在本方法中，对空的但非null的数值进行判断，如果是空则设置该值为null。
     */
    private void normalizeParameter(QueryQuotesParameterModel parameter) {
        if (CollectionsUtil.isEmptyOrNullCollection(parameter.getQuotePriceTypeList())) {
            parameter.setQuotePriceTypeList(null);
        }
        if (CollectionsUtil.isEmptyOrNullCollection(parameter.getCompanyTypes())) {
            parameter.setCompanyTypes(null);
        }
        if (CollectionsUtil.isEmptyOrNullCollection(parameter.getAmountList())) {
            parameter.setAmountList(null);
        }
        if (CollectionsUtil.isEmptyOrNullCollection(parameter.getDueDateWrapperList())) {
            parameter.setDueDateWrapperList(null);
        }
        if (CollectionsUtil.isEmptyOrNullCollection(parameter.getProvinceCodes())) {
            parameter.setProvinceCodes(null);
        }
        if (StringUtils.isEmpty(parameter.getOrderByPriceType())) {
            parameter.setOrderByPriceType("last_update_datetime");
        }
        if (CollectionsUtil.isEmptyOrNullCollection(parameter.getQuoteStatusList())) {
            parameter.setQuoteStatusList(null);
        }
        if (StringUtils.isEmpty(parameter.getOderSeq())) {
            parameter.setOderSeq("DESC");
        }
    }
    @Override
    public void flushDaoCache() {

    }
}
