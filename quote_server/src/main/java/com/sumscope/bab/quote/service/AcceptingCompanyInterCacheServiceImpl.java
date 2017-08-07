package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.EditMode;
import com.sumscope.bab.quote.dao.AcceptingCompanyDao;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by shaoxu.wang on 2016/12/22.
 * 为了支持AcceptingCompanyService中对于名称和拼音方式查询的快速响应，我们使用一次性从数据库中读取所有机构并缓存，
 * 再根据名称或名称拼音依次搜索的方式查询数据。这种方式比从数据库中按名称或拼音进行模糊查询的方式要快的多。代价是占用一定内存。
 * 如果本地缓存的机构在3万以下，这种方式的查询可以提供低于10毫秒的响应时间。
 * 本类既缓存类，缓存在一定时间，比如2小时内，有效。过期通过Dao读取全部数据。在承兑行机构新增或修改删除时会向本地总线发送消息，
 * 消息监听器将调用更新缓存命令，这样在2小时内即时发生了本地数据库修改也无需重新读取数据。
 *
 */
@Component
public class AcceptingCompanyInterCacheServiceImpl implements AcceptingCompanyInterCacheService {
    @Autowired
    private AcceptingCompanyDao acceptingCompanyDao;

    @Autowired
    @Qualifier(Constant.DEFAULT_CACHING_NAME)
    private CacheManager appCacheManager;

    @Override
    public List<AcceptingCompanyModel> retrieveCompanies() {
        //注意：我们使用Map作为缓存容器，因此返回的列表不做，也无法做任何排序操作。两次读取的列表值可能顺序不同。
        Cache cache = appCacheManager.getCache(Constant.DEFAULT_CACHING_NAME);
        Cache.ValueWrapper element = cache.get("AcceptingCompanyInterCacheServiceImpl.retrieveCompanies");
        if(element!=null){
            Map<String,AcceptingCompanyModel> map = (Map<String,AcceptingCompanyModel>)element.get();
            Collection<AcceptingCompanyModel> values = map.values();
            return new ArrayList<>(values);
        }else{
            synchronized (this){
                List<AcceptingCompanyModel> companyList = acceptingCompanyDao.retrieveCompanies();
                cache.put("AcceptingCompanyInterCacheServiceImpl.retrieveCompanies",buildMapByID(companyList));
                return companyList;
            }
        }
    }

    private Map<String, AcceptingCompanyModel> buildMapByID(List<AcceptingCompanyModel> companyList) {
        Map<String,AcceptingCompanyModel> map = new HashMap<>();
        for(AcceptingCompanyModel model: companyList){
            map.put(model.getId(),model);
        }
        return map;
    }

    @Override
    public void updateCache(AcceptingCompanyModel model, EditMode editMode) {
        Cache cache = appCacheManager.getCache(Constant.DEFAULT_CACHING_NAME);
        Cache.ValueWrapper element  = cache.get("AcceptingCompanyInterCacheServiceImpl.retrieveCompanies");
        if (element == null || model == null) {
            return;
        }

        Map<String,AcceptingCompanyModel> companyModelMap = (Map<String,AcceptingCompanyModel>) element.get();
        AcceptingCompanyModel currentCompanyInCache = companyModelMap.get(model.getId());
        switch (editMode){
            case INSERT:
                if(currentCompanyInCache == null){
                    companyModelMap.put(model.getId(),model);
                }
                break;
            case UPDATE:
                if(currentCompanyInCache != null){
                    BeanUtils.copyProperties(model, currentCompanyInCache);
                }
                break;
            case DELETE:
                companyModelMap.remove(model.getId());
                break;
            default:
                break;
        }
    }


}
