package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.FlushCacheEnum;
import com.sumscope.bab.quote.commons.enums.EditMode;
import com.sumscope.bab.quote.dao.AcceptingCompanyDao;
import com.sumscope.bab.quote.externalinvoke.FlushCacheMsgSender;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class AcceptingCompanyServiceImpl implements AcceptingCompanyService {
    @Autowired
    private AcceptingCompanyInterCacheService acceptingCompanyInterCacheService;


    @Autowired
    private FlushCacheMsgSender flushCacheMsgSender;

    @Autowired
    private AcceptingCompanyDao acceptingCompanyDao;

    @Override
    public List<AcceptingCompanyModel> searchCompaniesByNameOrPY(String name) {
        List<AcceptingCompanyModel> result = new ArrayList<>();

        List<AcceptingCompanyModel> allModels = acceptingCompanyInterCacheService.retrieveCompanies();
        for (AcceptingCompanyModel model : allModels) {
            if (model == null) {
                continue;
            }

            if (isCompanyNameMatch(name, model)
                    || isCompanyNamePinYinMatch(name, model)
                    || isCompanyNamePYMatch(name, model)
                    ) {
                result.add(model);
            }
        }

        return result;
    }

    private boolean isCompanyNamePYMatch(String name, AcceptingCompanyModel model) {
        return model.getCompanyNamePY() != null && model.getCompanyNamePY().contains(name);
    }

    private boolean isCompanyNamePinYinMatch(String name, AcceptingCompanyModel model) {
        return model.getCompanyNamePinYin() != null && model.getCompanyNamePinYin().contains(name);
    }

    private boolean isCompanyNameMatch(String name, AcceptingCompanyModel model) {
        return model.getCompanyName() != null && model.getCompanyName().contains(name);
    }

    @Override
    public void insertNewAcceptingCompany(AcceptingCompanyModel model) {
        if (setLastSynDateTime(model)) {
            return;
        }
        if (updateExpiredDate(model)) {
            updateAcceptingCompany(model);
        } else {
            Calendar cal30 = calendar30();
            model.setExpiredDatetime(cal30.getTime());
            acceptingCompanyDao.insertNewAcceptingCompany(model);
            acceptingCompanyInterCacheService.updateCache(model, EditMode.INSERT);
            flushCacheMsgSender.sendMsg(FlushCacheEnum.ACCEPTING_COMPANY_INSERT,model);

        }
    }

    @Override
    public void updateAcceptingCompany(AcceptingCompanyModel model) {
        if (setLastSynDateTime(model)) {
            return;
        }
        updateExpiredDate(model);
        if (getCompanyById(model.getId()) == null) {
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.PARAMETER_INVALID, "更新acceptingCompany失败,id不存在!");
        }
        acceptingCompanyDao.updateAcceptingCompany(model);
        acceptingCompanyInterCacheService.updateCache(model, EditMode.UPDATE);
        flushCacheMsgSender.sendMsg(FlushCacheEnum.ACCEPTING_COMPANY_UPDATE,model);


    }

    private boolean setLastSynDateTime(AcceptingCompanyModel model) {
        if (model == null) {
            return true;
        }

        model.setLastSynDateTime(Calendar.getInstance().getTime());
        return false;
    }

    private Calendar calendar30() {
        Date date = new Date();
        Calendar cal30 = Calendar.getInstance();
        cal30.setTime(date);
        cal30.add(Calendar.DATE, 30);

        return cal30;
    }

    private Boolean updateExpiredDate(AcceptingCompanyModel model) {
        if (model == null) {
            return false;
        }

        Calendar cal30 = calendar30();

        AcceptingCompanyModel oldModel = getCompanyById(model.getId());
        if (oldModel != null) {
            Calendar oldCalendar = Calendar.getInstance();
            oldCalendar.setTime(oldModel.getExpiredDatetime());
            if (cal30.compareTo(oldCalendar) > 0) {
                model.setExpiredDatetime(cal30.getTime());
            } else {
                model.setExpiredDatetime(oldModel.getExpiredDatetime());
            }

            return true;
        }

        return false;
    }

    @Override
    public void deleteAcceptingCompanies(List<String> ids) {
        List<AcceptingCompanyModel> models = acceptingCompanyDao.retrieveCompaniesByIds(ids);
        acceptingCompanyDao.deleteAcceptingCompanies(ids);
        for (AcceptingCompanyModel model : models) {
            acceptingCompanyInterCacheService.updateCache(model, EditMode.DELETE);
            flushCacheMsgSender.sendMsg(FlushCacheEnum.ACCEPTING_COMPANY_DELETE,model);
        }
    }

    @Override
    public AcceptingCompanyModel getCompanyById(String id) {
        if (id == null) {
            return null;
        }

        List<AcceptingCompanyModel> allModels = acceptingCompanyInterCacheService.retrieveCompanies();
        for (AcceptingCompanyModel model : allModels) {
            if (model == null) {
                continue;
            }

            if (id.equals(model.getId())) {
                return model;
            }
        }

        return null;
    }

    @Override
    public void updateInfos(List<AcceptingCompanyModel> models) {
        if (models == null) {
            return;
        }

        for (AcceptingCompanyModel model : models) {
            insertNewAcceptingCompany(model);
        }
    }

}
