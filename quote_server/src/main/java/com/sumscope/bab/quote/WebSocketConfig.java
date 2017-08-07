package com.sumscope.bab.quote;

import com.sumscope.bab.quote.websocket.HandshakeInterceptor;
import com.sumscope.bab.quote.websocket.SystemWebSocketHandler;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements
        WebSocketConfigurer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(babQuoteWebSocketHandler(), "/websck/babquote").setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor());
        registry.addHandler(babQuoteWebSocketHandler(), "/sockjs/websck/babquote").addInterceptors(new HandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();

        LogStashFormatUtil.logInfo(logger,"启动时 WebSocket 注册,位置：/websck/babquote 和/sockjs/websck/babquote" );
    }

    @Bean
    public WebSocketHandler babQuoteWebSocketHandler(){
        return new SystemWebSocketHandler();
    }

}
