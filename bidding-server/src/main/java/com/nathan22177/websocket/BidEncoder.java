package com.nathan22177.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class BidEncoder implements Encoder.Text<GameEndpoint.Bid> {
    private static Gson gson = new Gson();

    @Override
    public String encode(GameEndpoint.Bid bid) {
        return gson.toJson(bid);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
