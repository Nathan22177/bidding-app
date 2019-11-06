package com.nathan22177.websocket.decoder;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.nathan22177.websocket.messages.incoming.IncomingMessage;

/**
 *  Used to process data (bids in particular) received from a client.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
public class IncomingMessageDecoder implements Decoder.Text<IncomingMessage> {
    private static Gson gson = new Gson();

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public IncomingMessage decode(String s) {
        return gson.fromJson(s, IncomingMessage.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }
}
