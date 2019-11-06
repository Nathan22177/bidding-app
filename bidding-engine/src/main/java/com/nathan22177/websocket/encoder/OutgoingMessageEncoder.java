package com.nathan22177.websocket.encoder;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.nathan22177.websocket.messages.outgoing.OutgoingMessage;

/**
 *  Used to encode data that we pass to a client.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
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
