package com.nathan22177.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.nathan22177.websocket.GameEndpoint;

@Configuration
public class GameEndpointConfig {
    @Bean
    public GameEndpoint gameEndpoint() {
        return new GameEndpoint();
    }


    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }

}

