package com.nathan22177.websocket;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class EventEncoder implements Encoder.Text<GameEndpoint.Event> {
    private static Gson gson = new Gson();

    @Override
    public String encode(GameEndpoint.Event event) {
        return gson.toJson(event);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
