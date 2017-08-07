package com.sumscope.bab.quote.externalinvoke;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.commons.enums.EditMode;
import com.sumscope.bab.quote.commons.model.BABQuoteWrapper;
import com.sumscope.bab.quote.model.model.AbstractQuoteModel;
import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 * 报价信息总线消息发送者
 */
public class QuotePersistenceMsgSender extends AbstraceSumsopceMQ4JProxy {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int HEART_BEAT = 5;

    public QuotePersistenceMsgSender(String host, int port, String exchangeName){
        initSender(host,port,exchangeName, CreateOptions.SenderType.FANOUT,HEART_BEAT);
    }

    public <T extends AbstractQuoteModel> void sendQuoteChangeMsg(EditMode editMode, String quoteType, List<T> models) {
        for (T model : models) {
            BABQuoteWrapper babQuoteWrapper = new BABQuoteWrapper();
            babQuoteWrapper.setSource(Constant.PROJECT_TYPE);
            babQuoteWrapper.setEditMode(editMode);
            babQuoteWrapper.setQuoteType(quoteType);
            babQuoteWrapper.setObject(model);
            try {
                topicDurable(babQuoteWrapper.formatJsonToString());
            }
            catch (Exception e) {
                LogStashFormatUtil.logError(logger,"报价数据发送总线失败！",e);
            }
        }
    }
}