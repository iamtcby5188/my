package com.sumscope.bab.quote.wsandlocalmessage;

import com.sumscope.bab.quote.commons.model.BABQuoteWrapper;
import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.MqReceiverCallback;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 报价单更新监听器。监听器完成以下几个主要功能：
 * 1. 将报价单的改变通过WebSocket发送至前端。
 * 2. 调用一个不产生实际数据的服务，以刷新QuoteDao的二级缓存。刷新缓存的原因是可能存在多个实例，报价不是由本实例产生，因此必须重置QuoteDao的二级缓存。
 */
@Component
public class QuoteChangeListener extends AbstractMQ4JReceiver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String queueName;

    @Autowired
    private WebSocketInvoker webSocketInvoke;


    /**
     * 将消息反序列化后触发业务处理
     */
    public void start(String host, int port, String queueName) {
        try {
            this.queueName = queueName;
            startListening(host, port, queueName, queueName, CreateOptions.SenderType.FANOUT, getMqReceiverCallback());
            LogStashFormatUtil.logInfo(logger, queueName + "已启动。");
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger, "启动监听报价总线失败！", e);
        }
    }

    private void processNewQuote(String content) {
        try {
            BABQuoteWrapper babQuoteWrapper = JsonUtil.readValue(content, BABQuoteWrapper.class);
            webSocketInvoke.sendQuoteInListResponseDtoFormat(babQuoteWrapper);
        } catch (Exception e) {
            LogStashFormatUtil.logWarrning(logger, "总线监听错误: ", e);
        }
    }


    private MqReceiverCallback getMqReceiverCallback() {
        return new MqReceiverCallback() {
            @Override
            public boolean processString(String message) {
                processNewQuote(message);
                LogStashFormatUtil.logInfo(logger, queueName + "总线接受消息：" + message);
                return true;
            }

            @Override
            public boolean processBytes(byte[] message) {
                if (message != null) {
                    LogStashFormatUtil.logInfo(logger, queueName + "总线接受Byte消息：" + Arrays.toString(message));
                }
                return true;
            }
        };
    }

}
