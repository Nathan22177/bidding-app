package com.nathan22177.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class BidDecoder implements Decoder.Text<GameEndpoint.Bid> {

    private static Gson gson = new Gson();

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public GameEndpoint.Bid decode(String s) {
        return gson.fromJson(s, GameEndpoint.Bid.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }
}
