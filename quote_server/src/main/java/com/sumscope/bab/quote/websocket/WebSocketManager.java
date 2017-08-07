package com.sumscope.bab.quote.websocket;

import com.sumscope.bab.quote.commons.Constant;
import com.sumscope.bab.quote.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.quote.websocket.model.UserTokenInfoModel;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * WebSocket 管理类, 实现了发送接口和 Session管理接口
 *
 */
@Component
public class WebSocketManager implements WebSocketSender, WebSocketSessionManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    private ConcurrentHashMap<WebSocketSession,String> webSocketClients = new ConcurrentHashMap();
//    private ConcurrentHashMap<String,WebSocketSession> webSocketClient = new ConcurrentHashMap();
    public WebSocketManager() {
        //默认状态是隔50秒发送一次，应为一般timeout时间是60s
        websocketInit(50000);
    }

    public WebSocketManager(long pingPongInterval) {
        websocketInit(pingPongInterval);

    }

    private void websocketInit(long pingPongInterval){
        Timer timer = new Timer();
//        10秒后开始发送ping pong数据，保持WebSocket链接
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogStashFormatUtil.logDebug(logger,"send Ping message to clients");
                for(WebSocketSession session : webSocketClients.keySet()){
                    try {
                        session.sendMessage(formatContent("ping"));
                    } catch (IOException e) {
                        LogStashFormatUtil.logError(logger,"发送Ping命令失败", e);
                    }
                }

            }
        },10000,pingPongInterval);
    }

    /**
     * 获取所有 websocketInit session, 直接发送
     * @param message
     */
    public void sendToAllClient(String message){
        LogStashFormatUtil.logDebug(logger,"Will send message to client , clients:" + webSocketClients.size() + ", content:" + message);
        for(WebSocketSession session : webSocketClients.keySet()){
            try {
                Map<String, Object> attributes = session.getAttributes();
                UserTokenInfoModel userTokenModel = (UserTokenInfoModel)attributes.get(Constant.USER_TOKEN_WEBSOCKET);
                boolean valid = iamEntitlementCheck.checkTokenByUsernameAndToken(userTokenModel.getUsername(), userTokenModel.getToken());
                if(valid){
                    session.sendMessage(formatContent(message));
                }
            } catch (Exception e) {
                LogStashFormatUtil.logError(logger,"验证失败！", e);
            }
        }
    }

    private WebSocketMessage formatContent(String message){
        return new TextMessage(message);
    }

    /**
     * 断开连接将 session 从系统中清除
     * @param session
     */
    @Override
    public void removeSession(WebSocketSession session) {
        webSocketClients.remove(session);
    }
    /**
     * 新连接到达时, 添加 session
     * @param session
     */
    @Override
    public void addSession(WebSocketSession session) {
        webSocketClients.put(session,"");
    }
    @Override
    public int webSocketCount() {
        return webSocketClients.size();
    }
}
