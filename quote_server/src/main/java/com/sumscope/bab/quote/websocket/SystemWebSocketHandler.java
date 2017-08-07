package com.sumscope.bab.quote.websocket;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.websocket.model.UserTokenInfoModel;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * WebSocket 处理类
 */
@Component
public class SystemWebSocketHandler implements WebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WebSocketSessionManager manager;

    public SystemWebSocketHandler() {
    }

    public WebSocketSessionManager setSystemWebSocketHandler(WebSocketManager wssManager) {
        this.manager = wssManager;
        return wssManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            manager.addSession(session);
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger,"无法添加WebSocketSession！", e);
        }
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
//        TextMessage returnMessage = new TextMessage(wsm.getPayload()+ " received at server");
        String payload = wsm.getPayload().toString();
        UserTokenInfoModel infoModel = JsonUtil.readValue(payload, UserTokenInfoModel.class);
        wss.getAttributes().put(Constant.USER_TOKEN_WEBSOCKET, infoModel);
        manager.addSession(wss);
//        wss.sendMessage(returnMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        if (wss.isOpen()) {
            try {
                wss.close();
            } catch (IOException e) {
                LogStashFormatUtil.logError(logger,"关闭WebSocket失败！", e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        LogStashFormatUtil.logInfo(logger,wss + "is closed , due to " + cs.getReason());
        try {
            wss.close(CloseStatus.NORMAL);
            manager.removeSession(wss);
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger,"关闭WebSocket失败！", e);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }

}
