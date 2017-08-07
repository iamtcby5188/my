package com.sumscope.bab.quote.wsandlocalmessage;

import com.sumscope.bab.quote.commons.model.BABQuoteWrapper;
import com.sumscope.bab.quote.facade.converter.NPCQuoteDtoConverter;
import com.sumscope.bab.quote.facade.converter.SSCQuoteDtoConverter;
import com.sumscope.bab.quote.facade.converter.SSRQuoteDtoConverter;
import com.sumscope.bab.quote.model.dto.NPCQuoteDto;
import com.sumscope.bab.quote.model.dto.SSCQuoteDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.quote.model.model.NPCQuoteModel;
import com.sumscope.bab.quote.model.model.SSCQuoteModel;
import com.sumscope.bab.quote.model.model.SSRQuoteModel;
import com.sumscope.bab.quote.websocket.WebSocketSender;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shaoxu.wang on 2016/12/27.
 * webSocket推送信息
 */
@Component
class WebSocketInvoker {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WebSocketSender webSocketSender;

    @Autowired
    private SSRQuoteDtoConverter ssrQuoteDtoConverter;

    @Autowired
    private SSCQuoteDtoConverter sscQuoteDtoConverter;

    @Autowired
    private NPCQuoteDtoConverter npcQuoteDtoConverter;

    public WebSocketInvoker() {
        timerOnlineNum();
    }

    private void timerOnlineNum() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Integer integer = webSocketSender.webSocketCount();
                LogStashFormatUtil.logOnlineNumber(logger,integer);
            }
        }, 100, 5 * 60 * 1000);
    }

    void sendQuoteInListResponseDtoFormat(BABQuoteWrapper quoteWrapper) {
        if (quoteWrapper == null) {
            return;
        }
        if (SSRQuoteModel.class.getSimpleName().equals(quoteWrapper.getQuoteType())) {
            SSRQuoteModel model = JsonUtil.readValue(quoteWrapper.getObject().toString(), SSRQuoteModel.class);
            SSRQuoteDto dto = ssrQuoteDtoConverter.convertToSSRQuoteDto(model);
            quoteWrapper.setObject(dto);
        } else if (SSCQuoteModel.class.getSimpleName().equals(quoteWrapper.getQuoteType())) {
            SSCQuoteModel model = JsonUtil.readValue(quoteWrapper.getObject().toString(), SSCQuoteModel.class);
            SSCQuoteDto dto = sscQuoteDtoConverter.convertToSSCQuoteDto(model);
            quoteWrapper.setObject(dto);
        } else if (NPCQuoteModel.class.getSimpleName().equals(quoteWrapper.getQuoteType())) {
            NPCQuoteModel model = JsonUtil.readValue(quoteWrapper.getObject().toString(), NPCQuoteModel.class);
            NPCQuoteDto dto = npcQuoteDtoConverter.convertToNPCQuoteDto(model);
            quoteWrapper.setObject(dto);
        }
        String jsString = quoteWrapper.formatJsonToString();
        webSocketSender.sendToAllClient(jsString);
    }

}
