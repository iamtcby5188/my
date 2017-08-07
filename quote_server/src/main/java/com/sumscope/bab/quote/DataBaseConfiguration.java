package com.sumscope.bab.quote;

import com.alibaba.druid.pool.DruidDataSource;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@AutoConfigureAfter(PropertyConfig.class)
public class DataBaseConfiguration {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${application.datasource-primary.url}")
    private String primaryUrl;
    @Value("${application.datasource-primary.driverClassName}")
    private String primaryDriverClassName;
    @Value("${application.datasource-primary.username}")
    private String primaryUserName;
    @Value("${application.datasource-primary.password}")
    private String primaryPassword;
    @Value("${application.datasource-primary.min-idle}")
    private String primaryMinIdle;
    @Value("${application.datasource-primary.max-active}")
    private String primaryMaxActive;
    @Value("${application.datasource-primary.initial-size}")
    private String primaryInitialSize;
    @Value("${application.datasource-primary.validation-query}")
    private String primaryValidationQuery;

    @Value("${application.datasource-history.url}")
    private String historyUrl;
    @Value("${application.datasource-history.driverClassName}")
    private String historyDriverClassName;
    @Value("${application.datasource-history.username}")
    private String historyUserName;
    @Value("${application.datasource-history.password}")
    private String historyPassword;
    @Value("${application.datasource-history.min-idle}")
    private String historyMinIdle;
    @Value("${application.datasource-history.max-active}")
    private String historyMaxActive;
    @Value("${application.datasource-history.initial-size}")
    private String historyInitialSize;
    @Value("${application.datasource-history.validation-query}")
    private String historyValidationQuery;


    @Bean(name = Constant.BUSINESS_DATA_SOURCE, destroyMethod = "close", initMethod = "init")
    @Primary
    public DataSource dataSource() {
        LogStashFormatUtil.logDebug(logger,"Configruing DataSource:" + primaryUrl + ":" + primaryUserName);
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(primaryUrl);
        datasource.setDriverClassName(primaryDriverClassName);
        datasource.setMaxActive(NumberUtils.toInt(primaryMaxActive));
        datasource.setMinIdle(NumberUtils.toInt(primaryMinIdle));
        datasource.setInitialSize(NumberUtils.toInt(primaryInitialSize));
        datasource.setValidationQuery(primaryValidationQuery);
        datasource.setUsername(primaryUserName);
        datasource.setPassword(primaryPassword);
        return datasource;
    }

    @Bean(name = Constant.HISTORY_DATA_SOURCE, destroyMethod = "close", initMethod = "init")
    public DataSource historydataSource() {
        LogStashFormatUtil.logDebug(logger,"Configruing historyDataSource"+ historyUrl + ":" + historyUserName);
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(historyUrl);
        datasource.setDriverClassName(historyDriverClassName);
        datasource.setMaxActive(NumberUtils.toInt(historyMaxActive));
        datasource.setMinIdle(NumberUtils.toInt(historyMinIdle));
        datasource.setInitialSize(NumberUtils.toInt(historyInitialSize));
        datasource.setValidationQuery(historyValidationQuery);
        datasource.setUsername(historyUserName);
        datasource.setPassword(historyPassword);
        return datasource;

    }



}