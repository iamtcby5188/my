package com.sumscope.bab.quote.service;

import com.sumscope.bab.quote.commons.enums.*;
import com.sumscope.bab.quote.model.dto.BABQuoteStatus;
import com.sumscope.bab.quote.model.model.*;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.BABTradeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by fan.bai on 2016/12/16.
 * Implementation
 */
@EnableCaching
@Component
public class MockTestDataServiceImpl implements MockTestDataService {
    @Autowired
    private NPCQuoteManagementService npcQuoteManagementService;

    @Autowired
    private SSRQuoteManagementService ssrQuoteManagementService;

    @Autowired
    private SSCQuoteManagementService sscQuoteManagementService;

    @Autowired
    @Qualifier(Constant.DEFAULT_CACHING_NAME)
    private CacheManager cacheManager;


    private Random random = new Random();

    private static final Double[] PRICE_CANDIDATES = {null,2.559,null,1.25,1.11,1.254,null,1.268,4.546,null,3.123,3.258,null,3.98,4.78,null,4.66,5.54,5.98,null};

    private static final Integer[] AMOUNT_CANDIDATES = {null,null,null,null,null,2354680,3224646,2400000,3000000,null,3000001,300000,300001,500000,10000000,10000001,50000000,50000001};

    private static final Integer[] DUE_DATES_CANDIDATE_ARRAY = {null,null,null,20,14,150,180,270,330,364,190,300};

    private static final Integer[] POSSIBLE_EFFECTIVE_DATE_AFTER_TODAY_ARRAY = {0,1};

    private String[] qbUserIdArray = {"ff808181431eed020143b7cab7851b9a","ff80818143b97e220143bdbd828861ce","ff8081814411391d01441a6158ff279c","ff8081814411391d01441a75c45e2a6d","ff8081814989c8b1014bfc2cc6025bc5","ff8081814989c8b1014bfd34e0677333","ff8081814989c8b1014bfd3ee6d9745b","ff8081814a03e02f014a08eb1eaa004e","ff80818141da7bf70141fd98615e6d63","ff80818141da7bf70141ef5a7e142d63","ff80818141da7bf70141ee740ef70bdd","ff8081814d6a42ef014d8f73f90244ee","ff80818141da7bf70141fe0ef4007923","1b4d07bcfffe4d52a2637eb743a99f8a"};
    private String[] qbCompanyIdArray = {"402880f034219aed0134219e0ea30717","ff80818141120c92014159322a0858bc","ff8081814411391d014419e08d7f214b","ff80818143c497400143d1a49c3b33d2","ff8081814767d5a701476b5755293476","ff8081814545f7a40145f36eb4317c6b","2a4622f4346a647001346a7cd4ae021f","ff80818144fdf16701452b46e6627725","ff8081813796e33d0137e4c2cfff2af7","402880f034219aed0134219df38f065c","402880f034219aed013421d996481b3f","ff808181409b0a0b0140ba0ca15a66e0","2a4622f4346a647001346a7c0b29002a","402880f034219aed0134219d57bb0171"};
    private String[] acceptingHouseIdArray = {"Um4wQms3NXRPR1EwU2RlaGF5d3Evdz09","UGdpT2pIbnhZOXYzVHorRTdEVzlLQT09","UG1DZDdOQ3FRaU5BN0c5allUM3NXdz09"};
    private String[] provinceCandidateArray = {"SH01","JX01",null,"SC01",null,"HN01",null,"HB02","LN01","HN02","GD01"};

    @Override
    public long generateMockTestData(long numberOfData) {
        for(long i = 0;i<numberOfData;i++){
            generateSingleMockTestData();
        }
        return numberOfData;
    }

    @Override
    public String updateCache(String msg) {
        return msg;

    }

    @Override
    public String getCache() {
        return "FirstMessage:" + Calendar.getInstance().getTime();
    }

    @Override
    public String showCache() {
        org.springframework.cache.Cache cache = cacheManager.getCache(Constant.DEFAULT_CACHING_NAME);
        cache.get("Mock");
        return "";
    }

    private void generateSingleMockTestData() {
        BABQuoteType quoteType = getRandomValueFromList(BABQuoteType.values());
        switch (quoteType) {
            case SSR:
                SSRQuoteModel ssrModel = new SSRQuoteModel();
                initSSRQuoteModel(ssrModel);
                ssrQuoteManagementService.insertNewQuotes(Arrays.asList(ssrModel));
                break;
            case SSC:
                SSCQuoteModel sscModel = new SSCQuoteModel();
                initSSCQuoteModel(sscModel);
                sscQuoteManagementService.insertNewQuotes(Arrays.asList(sscModel));
                break;
            case NPC:
                NPCQuoteModel npcModel = new NPCQuoteModel();
                initNPCQuoteModel(npcModel);
                npcQuoteManagementService.insertNewQuotes(Arrays.asList(npcModel));
                break;
        }
    }

    private void initSSCQuoteModel(SSCQuoteModel model) {
        initAbstractQuoteModel(model);
        initAbstractCountryQuoteModel(model);
        if(model.getBillType() == BABBillType.CMB){
            model.setWbhPrice(getRandomPrice());
            model.setYbhPrice(getRandomPrice());
        }
    }

    private void initNPCQuoteModel(NPCQuoteModel model){
        initAbstractQuoteModel(model);
        model.setBillType(BABBillType.BKB);
        initAbstractCountryQuoteModel(model);
        model.setTradeType(getRandomValueFromList(BABTradeType.values()));
    }

    private void initAbstractCountryQuoteModel(AbstractCountryQuoteModel model) {
        model.setMinor(random.nextBoolean());
        model.setDirection(Direction.UDF);
        if(model.getBillType() == BABBillType.BKB){
            model.setGgPrice(getRandomPrice());
            model.setCsPrice(getRandomPrice());
            model.setNsPrice(getRandomPrice());
            model.setNxPrice(getRandomPrice());
            model.setNhPrice(getRandomPrice());
            model.setCzPrice(getRandomPrice());
            model.setWzPrice(getRandomPrice());
            model.setCwPrice(getRandomPrice());
        }
    }

    private void initSSRQuoteModel(SSRQuoteModel model) {
        initAbstractQuoteModel(model);
        if(model.getBillType() == BABBillType.CMB && model.getAdditionalInfo() == null){
            model.setAcceptingHouseId(getRandomValueFromList(acceptingHouseIdArray));
        }
        if(model.getContactId()==null || model.getQuoteCompanyId()==null){
            model.setAcceptingHouseId(getRandomValueFromList(acceptingHouseIdArray));
        }
        Integer amountI = getRandomValueFromList(AMOUNT_CANDIDATES);
        if(amountI != null){
            model.setAmount(new BigDecimal(amountI));
        }
        model.setPrice(getRandomPrice());
        if(model.getBillType() == BABBillType.BKB){
            model.setQuotePriceType(getRandomValueFromList(BABQuotePriceType.bkbTypes()));
        }else{
            model.setQuotePriceType(getRandomValueFromList(BABQuotePriceType.cmbTypes()));
        }

        if(random.nextBoolean()){
            model.setDirection(Direction.IN);
        }else{
            model.setDirection(Direction.OUT);
        }

        if(random.nextBoolean()){
            Calendar instance = Calendar.getInstance();
            Integer number = getRandomValueFromList(DUE_DATES_CANDIDATE_ARRAY);
            if(number != null){
                instance.add(Calendar.DAY_OF_YEAR,number);
            }
            model.setDueDate(instance.getTime());
        }

        model.setProvinceCode(getRandomValueFromList(provinceCandidateArray));

    }

    private BigDecimal getRandomPrice() {
        BigDecimal price = null;
        Double priceD = getRandomValueFromList(PRICE_CANDIDATES);
        if(priceD != null){
            price = new BigDecimal(priceD);
        }
        return price;
    }

    private void initAbstractQuoteModel(AbstractQuoteModel model) {
        if(random.nextBoolean()){
            model.setAdditionalInfo(generateAdditionInfo());
        }else{
            model.setContactId(getRandomValueFromList(qbUserIdArray));
            model.setQuoteCompanyId(getRandomValueFromList(qbCompanyIdArray));
        }
        model.setOperatorId(getRandomValueFromList(qbUserIdArray));

        model.setBillMedium(getRandomValueFromList(BABBillMedium.values()));
        model.setBillType(getRandomValueFromList(BABBillType.values()));
        Calendar instance = Calendar.getInstance();
        model.setCreateDate(instance.getTime());
        instance.add(Calendar.DAY_OF_YEAR,getRandomValueFromList(POSSIBLE_EFFECTIVE_DATE_AFTER_TODAY_ARRAY));
        model.setEffectiveDate(instance.getTime());
        if(random.nextBoolean()){
            //延期一天过期
            instance.add(Calendar.DAY_OF_YEAR,1);
        }
        instance.set(Calendar.HOUR,23);
        instance.set(Calendar.MINUTE,0);
        instance.set(Calendar.SECOND,0);
        model.setExpiredDate(instance.getTime());
        model.setQuoteStatus(getRandomValueFromList(BABQuoteStatus.values()));
        model.setMemo("test data,票据备注测试mock数据");
    }

    private QuoteAdditionalInfoModel generateAdditionInfo() {
        QuoteAdditionalInfoModel model = new QuoteAdditionalInfoModel();
        model.setContactName("测试用联系人");
        model.setContactTelephone("123-456789");
        model.setQuoteCompanyName("测试用机构名");
        model.setAcceptingHouseName("测试承兑行名");
        return model;
    }


    private <T> T getRandomValueFromList(T[] values){
        int size = values.length;
        int randomIndex = random.nextInt(size);
        return values[randomIndex];
    }
}
