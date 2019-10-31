package com.nathan22177.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//@Configuration
//
//@Controller
//public class WebSocketConfig implements WebSocketConfigurer {
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        try {
//            registry.addHandler(socketHandler(),"/socket");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Bean
//    public WebSocketHandler socketHandler() throws IOException {
//        return new TextWebSocketHandler();
//    }
//}
