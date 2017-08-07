package com.sumscope.bab.quote.externalinvoke;

import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.Sender;
import com.sumscope.cdh.sumscopemq4j.SenderFactory;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by fan.bai on 2017/3/6.
 * RabbitMQ客户端发送者初始化类
 */
public abstract class AbstraceSumsopceMQ4JProxy {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Sender sender;

    Sender initSender(String host, int port, String exchangeName, CreateOptions.SenderType senderType, int heartBeat) {
        if(this.sender == null){
            LogStashFormatUtil.logInfo(logger,"尝试连接RabbitMQ服务器...");
            //.设置rabbitmq的连接信息：ip（必填），端口（必填），心跳时间（选填，默认60s）
            CreateOptions createOptions = new CreateOptions();
            createOptions.setHost(host);
            createOptions.setPort(port);
            createOptions.setRequestedHeartbeat(heartBeat);//单位是秒，尽量设置在5~10秒之间，防止断线。
            //设置传输信息：持久化（选填，默认false），队列名，传输类型（选填，默认queue，其他还有topic，fanout）
            createOptions.setExchangeName(exchangeName);
            createOptions.setSenderType(senderType);
            //创建Sender
            try {
                this.sender =  SenderFactory.newSender(createOptions);
            } catch (Exception e) {
                this.sender = null;
                LogStashFormatUtil.logError(logger,"无法初始化RabbitMQ发送服务对象！！！ host:" + host + " port:" + port + " topic:" + exchangeName,e);
            }
        }
        return this.sender;
    }

    /**
     * 往总线上发送消息 的方法
     * @param message 消息
     * @throws IOException 异常
     */
    void topicDurable(String message) throws IOException {
        if(sender!= null){
            sender.send(message);
        }else{
            LogStashFormatUtil.logError(logger,"总线发送服务对象未初始化");
        }
    }
}
