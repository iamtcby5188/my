package com.sumscope.bab.quote.wsandlocalmessage;

import com.sumscope.bab.quote.commons.enums.EditMode;
import com.sumscope.bab.quote.commons.util.FlushCacheSourceUtil;
import com.sumscope.bab.quote.model.model.AcceptingCompanyModel;
import com.sumscope.bab.quote.model.model.FlushCacheWrapper;
import com.sumscope.bab.quote.service.*;
import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.MqReceiverCallback;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by fan.bai on 2017/3/6.
 * 缓存更新消息监听器。当获得缓存更新消息后调用对应的服务刷新缓存。
 */
@Component
public class FlushCacheListener extends AbstractMQ4JReceiver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SSRQuoteQueryService ssrQuoteQueryService;

    @Autowired
    private SSCQuoteQueryService sscQuoteQueryService;

    @Autowired
    private NPCQuoteQueryService npcQuoteQueryService;

    @Autowired
    private UserJoiningService userJoiningService;

    @Autowired
    private AcceptingCompanyInterCacheService acceptingCompanyInterCacheService;

    public void start(String host, int port, String queueName) {
        try {
            startListening(host, port, queueName, queueName, CreateOptions.SenderType.FANOUT, getMqReceiverCallback());
            LogStashFormatUtil.logInfo(logger, "成功启动缓存更新消息队列监听器.");
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger, "启动缓存更新消息队列监听器失败！！！", e);
        }
    }


    private MqReceiverCallback getMqReceiverCallback() {
        return new MqReceiverCallback() {
            @Override
            public boolean processString(String s) {
                FlushCacheWrapper flushCacheWrapper = JsonUtil.readValue(s, FlushCacheWrapper.class);
                LogStashFormatUtil.logDebug(logger,"接收到缓存更新消息：" + flushCacheWrapper.getSource() + ":" + flushCacheWrapper.getTarget());
                String thisSourceName = FlushCacheSourceUtil.getSourceName();
                if (thisSourceName == null) {
                    //如果无法成功获取当前hostname，则都执行缓存刷新。
                    processFlushCacheWrapper(flushCacheWrapper);
                } else if (!thisSourceName.equals(flushCacheWrapper.getSource())) {
                    //仅对不是自己发送的更新信息进行处理。
                    processFlushCacheWrapper(flushCacheWrapper);
                }
                return false;
            }
            @Override
            public boolean processBytes(byte[] bytes) {
                return true;
            }
        };
    }

    private void processFlushCacheWrapper(FlushCacheWrapper flushCacheWrapper) {
        LogStashFormatUtil.logDebug(logger,"根据缓存更新消息进行缓存处理：" + flushCacheWrapper.getTarget());
        switch (flushCacheWrapper.getTarget()) {
            case SSR_QUOTE_FLUSH:
                ssrQuoteQueryService.flushCache();
                break;
            case SSC_QUOTE_FLUSH:
                sscQuoteQueryService.flushCache();
                break;
            case NPC_QUOTE_FLUSH:
                npcQuoteQueryService.flushCache();
                break;
            case USER_JOINING_FLUSH:
                userJoiningService.flushCache();
                break;
            case ACCEPTING_COMPANY_INSERT: {
                acceptingCompanyInterCacheService.updateCache(getAcceptingCompanyModel(flushCacheWrapper), EditMode.INSERT);
                break;
            }
            case ACCEPTING_COMPANY_UPDATE: {
                acceptingCompanyInterCacheService.updateCache(getAcceptingCompanyModel(flushCacheWrapper), EditMode.UPDATE);
                break;
            }
            case ACCEPTING_COMPANY_DELETE: {
                acceptingCompanyInterCacheService.updateCache(getAcceptingCompanyModel(flushCacheWrapper), EditMode.DELETE);
                break;
            }
        }
    }

    private AcceptingCompanyModel getAcceptingCompanyModel(FlushCacheWrapper flushCacheWrapper) {
        return JsonUtil.readValue(flushCacheWrapper.getDataJsonString(), AcceptingCompanyModel.class);
    }
}
