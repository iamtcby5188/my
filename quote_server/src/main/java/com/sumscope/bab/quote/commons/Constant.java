package com.sumscope.bab.quote.commons;

/**
 * 常量
 */
public final class Constant {
    private Constant(){

    }

    //用于定义业务数据库及历史数据库相关数据服务
    public static final String BUSINESS_DATA_SOURCE = "dataSource";
    public static final String HISTORY_DATA_SOURCE = "historyDataSource";
    public static final String BUSINESS_TRANSACTION_MANAGER = "currentTransactionManager";
    public static final String HISTORY_TRANSACTION_MANAGER = "historyTransactionManager";
    public static final String BUSINESS_SQL_SESSION_TEMPLATE = "sqlSessionTemplate";
    public static final String HISTORY_SQL_SESSION_TEMPLATE = "historysqlSessionTemplate";
    // 缓存名称
    public static final String DEFAULT_CACHING_NAME = "defaultcaching";
    public static final String USER_JOINING_DAO_CACHE = "userJoiningDaoCache";
    public static final String NPC_QUOTE_DAO_CACHE = "npcQuoteDaoCache";
    public static final String SSC_QUOTE_DAO_CACHE = "sscQuoteDaoCache";
    public static final String SSR_QUOTE_DAO_CACHE = "ssrQuoteDaoCache";
    public static final String QUOTE_ADDITION_INFO_DAO_CACHE = "quoteAdditionInfoDaoCache";
    // 数据验证
    public static final String PRICE_MAX_VALUE = "99.999";
    public static final String PRICE_MIN_VALUE = "0.001";
    public static final String AMOUNT_MAX_VALUE = "9999999999999.99";
    public static final String AMOUNT_MIN_VALUE = "0.01";
    //机构查询返回数量上限
    public static final int ACCEPTING_COMPANY_NUM = 10;
    //总线发送相关信息
    public static final String PROJECT_TYPE = "BABQuote";
    public static final String SENDTYPE_FANOUT = "fanout";

    public static final String JOIN_USER = "joiningUser";
    public static final String USER_TOKEN = "Token";

    public static final String USER_TOKEN_WEBSOCKET = "UserTokenInfoModel";
    //Excel新老版本后缀
    public static final String NEW_EXCEL = "xlsx";
    public static final String OLD_EXCEL = "xls";

    //价差分析
    public static final String SSC_ANALYSIS = "quotePriceTrendsSSC";
    public static final String NPC_ANALYSIS = "quotePriceTrendsNPC";
    public static final String PRICE_MARGIN_MODEL_ANALYSIS = "priceMarginModelAnalysis";

    public static final String R001 = "R001";
    public static final String IBO001 = "IBO001";
    public static final String SHIBOR_O_N ="SHIBOR_O_N";
    public static final String CODE_SHIBOR_O_N ="SHIBOR_O/N";

    //报价token的 生效时间 和失效时间(单位为妙) 及 项目标识
    public static final int effectiveDate =1;
    public static final int expiredDate =600;
    public static final String fromProject ="bab_quote";
}