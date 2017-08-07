package com.sumscope.bab.quote.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABOrderByType;
import com.sumscope.bab.quote.commons.enums.WEBQuoteAmountCondition;
import com.sumscope.bab.quote.commons.model.AmountWrapper;
import com.sumscope.bab.quote.commons.model.DueDateWrapper;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.quote.commons.util.QuoteDateUtils;
import com.sumscope.bab.quote.commons.util.Utils;
import com.sumscope.bab.quote.commons.util.ValidationUtil;
import com.sumscope.bab.quote.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.dto.QueryQuoteParameterDto;
import com.sumscope.bab.quote.model.model.QueryQuotesParameterModel;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * QueryQuotesParameterDto转换器
 */
@Component
public class QueryQuotesParameterDtoConverter {
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientHelperWithCache;
	/**
	 * 转换至 QueryQuotesParameterModel类型
	 */
	public QueryQuotesParameterModel convertToModel(QueryQuoteParameterDto dto, Date expiredQuotesDate) {
		ValidationUtil.validateModel(dto);
		QueryQuotesParameterModel parameterModel = new QueryQuotesParameterModel();
        BeanUtils.copyProperties(dto, parameterModel);
        setAmountList(dto, parameterModel);
        setDueDateWrapperList(dto.getDueDateWrapperList());
        parameterModel.setDueDateWrapperList(dto.getDueDateWrapperList());

        parameterModel.setExpiredQuotesDate(expiredQuotesDate!=null ? QuoteDateUtils.getExpiredTimeOfDate(expiredQuotesDate) : null);

		parameterModel.setProvinceCodes(dto.getProvinceCodes());
		if (dto.getOrderByPriceType() != null) {
			parameterModel.setOrderByPriceType(Utils.validateStr(dto.getOrderByPriceType()));
		}
		if (dto.getOderSeq() != null) {
			parameterModel.setOderSeq(Utils.validateStr(dto.getOderSeq().getCode()));
		}
        setPage(dto, parameterModel);

        setOrderByType(dto, parameterModel);

        setQuoteStatusList(dto, parameterModel);
        setCompanyName(dto, parameterModel);
        parameterModel.setMemo(Utils.validateStr(parameterModel.getMemo()));
		return parameterModel;
	}

    private void setCompanyName(QueryQuoteParameterDto dto, QueryQuotesParameterModel parameterModel) {
        if(dto.getCompanyId()!=null){
            IdbFinancialCompanyAndIdDTO companyInfo = edmHttpClientHelperWithCache.getIdbFinancialCompanyAndIdDTO(Utils.validateStr(dto.getCompanyId()));
            parameterModel.setCompanyName(companyInfo.getName());
        }
    }

    private void setQuoteStatusList(QueryQuoteParameterDto dto, QueryQuotesParameterModel parameterModel) {
        if (dto.getQuoteStatusList() == null) {
            List<BABQuoteStatus> list = new ArrayList<>();
            list.add(BABQuoteStatus.DSB);
            list.add(BABQuoteStatus.DLD);

            parameterModel.setQuoteStatusList(list);
        }
    }

    private void setOrderByType(QueryQuoteParameterDto dto, QueryQuotesParameterModel parameterModel) {
        if (dto.getOrderByPriceType() == null || StringUtils.isBlank(dto.getOrderByPriceType())) {
            parameterModel.setOrderByPriceType("last_update_datetime");
            parameterModel.setOderSeq("DESC");
        }else{
            checkOrderByType(dto, parameterModel);
        }
    }

    private void setPage(QueryQuoteParameterDto dto, QueryQuotesParameterModel parameterModel) {
        if(dto.getPageSize()> 0 && dto.getPageNumber()>= 0){
            parameterModel.setPaging(true);
            parameterModel.setPageSize(dto.getPageSize());
            parameterModel.setPageNumber(dto.getPageNumber());
        }
    }

    private void setAmountList(QueryQuoteParameterDto dto, QueryQuotesParameterModel parameterModel) {
        if (dto.getAmountList() != null) {
            List<AmountWrapper> amountWrappers = new ArrayList<>();
            for (WEBQuoteAmountCondition amountCondition : dto.getAmountList()) {
                AmountWrapper amountWrapper = new AmountWrapper();
                amountWrapper.setAmountLow(new BigDecimal(amountCondition.getAmountLow()));
                amountWrapper.setAmountHigh(new BigDecimal(amountCondition.getAmountHigh()));
                amountWrappers.add(amountWrapper);
            }
            parameterModel.setAmountList(amountWrappers);
        }
    }

    private void checkOrderByType(QueryQuoteParameterDto dto, QueryQuotesParameterModel parameterModel) {
        List<String> list = new ArrayList<>();
        for(BABOrderByType orderByType : BABOrderByType.values()){
           if(orderByType.getDisplayName().equals(dto.getOrderByPriceType())){
               list.add(dto.getOrderByPriceType());
               break;
           }
        }
        if(!CollectionsUtil.isEmptyOrNullCollection(list)){
            parameterModel.setOrderByPriceType(dto.getOrderByPriceType());
        }else{
            ValidationUtil.throwExceptionWithDetails(
                    new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING, "OrderByPriceType", "无此排序字段！"));
        }
    }

    /**
     * 设置查询时的开始日期从当天00:00:00 到结束日期为23:59:59
     * @param dueDateWrapperList
     */
    private void setDueDateWrapperList( List<DueDateWrapper> dueDateWrapperList) {
        if(dueDateWrapperList!=null && dueDateWrapperList.size()>0){
            for(DueDateWrapper dueDate:dueDateWrapperList){
                dueDate.setDueDateBegin(QuoteDateUtils.getBeginingTimeByDate(dueDate.getDueDateBegin()));
                dueDate.setDueDateEnd(QuoteDateUtils.getBeginingTimeByDate(dueDate.getDueDateEnd()));
            }
        }
    }

    public void getQueryQuotesParameterModel(QueryQuotesParameterModel parameterModel){
        if(parameterModel.getCompanyTypes()==null && parameterModel.getBillType()== BABBillType.CMB){
            List<BABAcceptingCompanyType> list = new ArrayList<>();
            list.add(BABAcceptingCompanyType.CET);
            list.add(BABAcceptingCompanyType.LET);
            list.add(BABAcceptingCompanyType.SOE);
            list.add(BABAcceptingCompanyType.OTH);
            parameterModel.setCompanyTypes(list);
        }
    }
}
