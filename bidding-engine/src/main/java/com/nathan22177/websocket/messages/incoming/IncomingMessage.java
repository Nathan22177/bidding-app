package com.nathan22177.websocket.messages.incoming;

import com.nathan22177.enums.Side;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncomingMessage {
        Long gameId;
        int bid;
        Side side;
}
