package com.sumscope.bab.quote.wsandlocalmessage;

import com.sumscope.bab.quote.AbstractBabQuoteIntegrationTest;
import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.Sender;
import com.sumscope.cdh.sumscopemq4j.SenderFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * Created by Administrator on 2016/12/28.
 */
//@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
//@Sql(scripts = {"/com/sumscope/bab/quote/schema.sql"},config = @SqlConfig(dataSource = Constant.HISTORY_DATA_SOURCE))
//@Sql(scripts = {"/com/sumscope/bab/quote/ssr_init_data.sql"},config = @SqlConfig(dataSource = Constant.BUSINESS_DATA_SOURCE))
public class QuotePersistenceMsgSenderTest extends AbstractBabQuoteIntegrationTest {

    @Value("${application.messagebus.url}")
    private String host;
    @Value("${application.messagebus.port}")
    private int port;
    private String exchangeName="test-exchange-fanout";
    private CreateOptions.SenderType senderType=CreateOptions.SenderType.FANOUT;
    private String message="testsetestes";

    @Test
    public void topicDurable() throws Exception{
        //.设置rabbitmq的连接信息：ip（必填），端口（必填），心跳时间（选填，默认60s）
        CreateOptions  createOptions = new CreateOptions();
        createOptions.setHost(host);
        createOptions.setPort(port);
        createOptions.setRequestedHeartbeat(5);//单位是秒，尽量设置在5~10秒之间，防止断线。
        createOptions.setDurable(false);
        createOptions.setExchangeName(exchangeName);
        createOptions.setSenderType(senderType);
        Sender sender = SenderFactory.newSender(createOptions);
        sender.send(message);
        //当前sumscopeMQ4J库有一个bug，关闭多个sender中的一个会引起其他sender发送失败，造成后续集成测试失败，暂时禁止sender的关闭功能。
        //TODO(第三方库bug修正后应允许关闭):
//        sender.close();

    }
}
