package com.sumscope.bab.quote.wsandlocalmessage;


import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.MqReceiverCallback;
import com.sumscope.cdh.sumscopemq4j.Receiver;
import com.sumscope.cdh.sumscopemq4j.ReceiverFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2016/12/27.
 * 总线
 */
abstract class AbstractMQ4JReceiver {


    private static final int HEART_BEAT = 5; //心跳尽量设置在5~10秒之间，防止断线。

    private Receiver receiver;

    public void stop(){
        receiver.stop();

    }

    /**
     *  从总线上接收信息
     */
    void startListening(String host, int port, String queueName, String exchangeName,
                        CreateOptions.SenderType senderType, MqReceiverCallback mqReceiverCallback) throws IOException, TimeoutException {
        if(receiver == null){
            //创建CreateOptions
            CreateOptions createOptions= new CreateOptions();
            //设置rabbitmq的连接信息：ip（必填），端口（必填），心跳时间（选填，默认60s）
            createOptions.setHost(host);
            createOptions.setPort(port);
            createOptions.setRequestedHeartbeat(HEART_BEAT);//单位是秒，尽量设置在5~10秒之间，防止断线。
            //设置传输信息：持久化（选填，默认false），队列名，传输类型（选填，默认queue，其他还有topic
            createOptions.setQueueName(queueName);
            createOptions.setSenderType(senderType);
            createOptions.setExchangeName(exchangeName);
            receiver = ReceiverFactory.newReceiver(createOptions,mqReceiverCallback);
            receiver.receive();
        }
    }
}
