package com.nathan22177.websocket;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.nathan22177.websocket.messages.outgoing.OutgoingMessage;

public class OutgoingMessageEncoder implements Encoder.Text<OutgoingMessage> {
    private static Gson gson = new Gson();

    @Override
    public String encode(OutgoingMessage message) {
        return gson.toJson(message);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
