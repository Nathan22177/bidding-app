package com.nathan22177.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class EventDecoder implements Decoder.Text<GameEndpoint.Event> {

    private static Gson gson = new Gson();

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public GameEndpoint.Event decode(String s) {
        return gson.fromJson(s, GameEndpoint.Event.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }
}
