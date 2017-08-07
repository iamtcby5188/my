package com.sumscope.bab.quote.websocket;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/1/12.
 * WebSocket
 */
public class WebSocket {

    @Autowired
    private SystemWebSocketHandler systemWebSocketHandler;

    @Autowired
    private  WebSocketManager babQuoteWebSocketManager;

    public WebSocketSessionManager init(){
       return systemWebSocketHandler.setSystemWebSocketHandler((WebSocketManager) babQuoteWebSocketSender());
    }

    public WebSocketSender babQuoteWebSocketSender() {
        if (this.babQuoteWebSocketManager == null) {
            this.babQuoteWebSocketManager = new WebSocketManager();
        }
        return babQuoteWebSocketManager;
    }
}
