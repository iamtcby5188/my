package com.sumscope.bab.quote;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.cdhplus.httpclient.CDHPlusClientConstant;
import com.sumscope.iam.edmclient.EdmClientConstant;
import com.sumscope.iam.emclient.EmClientConstant;
import com.sumscope.iam.iamclient.IamClientConstant;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.FactoryConfiguration;
import net.sf.ehcache.distribution.RMICacheManagerPeerListenerFactory;
import net.sf.ehcache.distribution.RMICacheManagerPeerProviderFactory;
import net.sf.ehcache.distribution.RMICacheReplicatorFactory;
import net.sf.ehcache.management.ManagementService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;


/**
 * 应用程序配置-EhCache缓存配置
 */
@Configuration
@EnableCaching
public class CachingConfigration extends CachingConfigurerSupport {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 启动参数
     */
    private static final String HOSTNAME = "hostname";
    private static final String PEER_PORT = "peerPort";
    private static final String NEIGHBOR_URLS = "neighborUrls";

    private CacheConfiguration getDefaultSingleCacheConfiguration(String cachingName, int maxLocalHeap) {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(cachingName);
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setMaxEntriesLocalHeap(maxLocalHeap);
        cacheConfiguration.setTimeToLiveSeconds(1000);
        return cacheConfiguration;
    }


    @Bean
    public KeyGenerator keyGenerator(){
        return new CacheKeyGenerator();
    }

    /**
     * 应用程序缓存
     */
    @Override
    @Bean(name = Constant.DEFAULT_CACHING_NAME)
    public CacheManager cacheManager() {
        net.sf.ehcache.CacheManager ehCacheManager = appEhCacheManager();
        EhCacheCacheManager cacheManager =  new EhCacheCacheManager(ehCacheManager);
        //配置MBean用于JConsole监控
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ManagementService.registerMBeans(ehCacheManager, mBeanServer, true, true, true, true);

        return cacheManager;
    }

    private net.sf.ehcache.CacheManager appEhCacheManager() {
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        CacheConfiguration edmCacheConfig = getDefaultSingleCacheConfiguration(EdmClientConstant.EDM_CACHING_NAME, 5000);
        CacheConfiguration emCacheConfig = getDefaultSingleCacheConfiguration(EmClientConstant.EM_CACHING_NAME, 5000);
        CacheConfiguration icmCacheConfig = getDefaultSingleCacheConfiguration(IamClientConstant.IAM_CACHING_NAME, 5000);
        CacheConfiguration cdhPlusCacheConfig = getDefaultSingleCacheConfiguration(CDHPlusClientConstant.CDHPLUS_CACHING_NAME, 5000);

        config.addCache(edmCacheConfig);
        config.addCache(emCacheConfig);
        config.addCache(icmCacheConfig);
        config.addCache(cdhPlusCacheConfig);

        CacheConfiguration appDefaultCache = getDefaultSingleCacheConfiguration(Constant.DEFAULT_CACHING_NAME, 5000);
        config.addCache(appDefaultCache);
        CacheConfiguration userJoiningDaoCache = getDefaultSingleCacheConfiguration(Constant.USER_JOINING_DAO_CACHE, 3000);
        config.addCache(userJoiningDaoCache);
        CacheConfiguration npcQuoteDaoCache = getDefaultSingleCacheConfiguration(Constant.NPC_QUOTE_DAO_CACHE, 1000);
        config.addCache(npcQuoteDaoCache);
        CacheConfiguration sscQuoteDaoCache = getDefaultSingleCacheConfiguration(Constant.SSC_QUOTE_DAO_CACHE, 1000);
        config.addCache(sscQuoteDaoCache);
        CacheConfiguration ssrQuoteDaoCache = getDefaultSingleCacheConfiguration(Constant.SSR_QUOTE_DAO_CACHE, 1000);
        config.addCache(ssrQuoteDaoCache);
        CacheConfiguration additionInfoDaoCache = getDefaultSingleCacheConfiguration(Constant.QUOTE_ADDITION_INFO_DAO_CACHE, 1000);
        config.addCache(additionInfoDaoCache);

        //根据是否添加了-DrmiUrls 参数决定是否设置为集群模式
        String neighborUrls = System.getProperty(NEIGHBOR_URLS);
        StringBuilder rmiUrls = new StringBuilder();
        if(StringUtils.isNotBlank(neighborUrls)){
            appDefaultCache.addCacheEventListenerFactory(getRIMCacheReplicatorFactoryProperties());
            userJoiningDaoCache.addCacheEventListenerFactory(getRIMCacheReplicatorFactoryProperties());
            npcQuoteDaoCache.addCacheEventListenerFactory(getRIMCacheReplicatorFactoryProperties());
            sscQuoteDaoCache.addCacheEventListenerFactory(getRIMCacheReplicatorFactoryProperties());
            ssrQuoteDaoCache.addCacheEventListenerFactory(getRIMCacheReplicatorFactoryProperties());
            additionInfoDaoCache.addCacheEventListenerFactory(getRIMCacheReplicatorFactoryProperties());
//            根据neighborUrls启动参数生成rmiUrls字符串
            String[] neighborUrlArray = neighborUrls.split(",");
            for(String neighborUrl:neighborUrlArray){
                String hostWithPort = "//" + neighborUrl;
                rmiUrls.append(hostWithPort).append("/").append(Constant.DEFAULT_CACHING_NAME).append("|");
                rmiUrls.append(hostWithPort).append("/").append(Constant.USER_JOINING_DAO_CACHE).append("|");
                rmiUrls.append(hostWithPort).append("/").append(Constant.NPC_QUOTE_DAO_CACHE).append("|");
                rmiUrls.append(hostWithPort).append("/").append(Constant.SSC_QUOTE_DAO_CACHE).append("|");
                rmiUrls.append(hostWithPort).append("/").append(Constant.SSR_QUOTE_DAO_CACHE).append("|");
                rmiUrls.append(hostWithPort).append("/").append(Constant.QUOTE_ADDITION_INFO_DAO_CACHE).append("|");
            }
            rmiUrls.delete(rmiUrls.length()-1,rmiUrls.length());//删除最后一个"|"字符。
            LogStashFormatUtil.logInfo(logger,"配置缓存集群。rmiUrls为："+ rmiUrls.toString());
            config.addCacheManagerPeerProviderFactory(getRMIPeerProviderFactory(rmiUrls.toString()));
            config.addCacheManagerPeerListenerFactory(getRMIPeerListenerFactory());
        }

        return net.sf.ehcache.CacheManager.newInstance(config);

    }

    private FactoryConfiguration getRMIPeerListenerFactory() {
        FactoryConfiguration config = new FactoryConfiguration();
        config.setClass(RMICacheManagerPeerListenerFactory.class.getName());

        String properties = "hostName=%s, port=%s, socketTimeoutMillis=2000";
        String hostname = System.getProperty(HOSTNAME);
        if(StringUtils.isBlank(hostname)){
            hostname = "localhost";
        }
        String peerPort = System.getProperty(PEER_PORT);
        if(StringUtils.isBlank(peerPort) || !StringUtils.isNumeric(peerPort)){
            peerPort = "40001";
        }

        String formatS = String.format(properties, hostname, peerPort);

        config.setProperties(formatS);
        return config;
    }

    private FactoryConfiguration getRMIPeerProviderFactory(String rmiUrls) {
        FactoryConfiguration config = new FactoryConfiguration();
        config.setClass(RMICacheManagerPeerProviderFactory.class.getName());
        String properties = "peerDiscovery=manual, rmiUrls = %s";
        String format = String.format(properties, rmiUrls);
        config.setProperties(format);
        return config;
    }

    private CacheConfiguration.CacheEventListenerFactoryConfiguration getRIMCacheReplicatorFactoryProperties() {
        CacheConfiguration.CacheEventListenerFactoryConfiguration config = new CacheConfiguration.CacheEventListenerFactoryConfiguration();
        config.setClass(RMICacheReplicatorFactory.class.getName());
        config.setProperties("replicateAsynchronously=true,replicatePuts=true,replicateUpdates=true,replicateUpdatesViaCopy=true,replicateRemovals=true ");
        return config;
    }


}