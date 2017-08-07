package com.sumscope.bab.quote.externalinvoke;

import com.sumscope.bab.quote.commons.FlushCacheEnum;
import com.sumscope.bab.quote.commons.util.FlushCacheSourceUtil;
import com.sumscope.bab.quote.model.model.FlushCacheWrapper;
import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by fan.bai on 2017/3/6.
 * 使用总线传递缓存更新消息，本类封装了缓存消息总线消息发送者。
 */
public class FlushCacheMsgSender extends AbstraceSumsopceMQ4JProxy {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int HEART_BEAT = 5;

    public FlushCacheMsgSender(String host, int port, String exchangeName){
        initSender(host,port,exchangeName, CreateOptions.SenderType.FANOUT,HEART_BEAT);
    }

    public void sendMsgWithoutObject(FlushCacheEnum target){
        sendMsg(target,null);
    }

    public void sendMsg(FlushCacheEnum target, Object msgInfo){
        FlushCacheWrapper flushCacheWrapper = new FlushCacheWrapper();
        flushCacheWrapper.setSource(FlushCacheSourceUtil.getSourceName());
        flushCacheWrapper.setTarget(target);
        if(msgInfo != null){
            flushCacheWrapper.setClassFullName(msgInfo.getClass().getName());
            flushCacheWrapper.setDataJsonString(JsonUtil.writeValueAsString(msgInfo));
        }

        String msg = JsonUtil.writeValueAsString(flushCacheWrapper);

        try {
            topicDurable(msg);
        } catch (IOException e) {
            LogStashFormatUtil.logError(logger,"发送缓存更新消息失败...",e);
        }

    }



}
