package com.nathan22177.websocket.messages.outgoing;

import com.nathan22177.collection.BiddingRound;
import com.nathan22177.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OutgoingMessage {

    Long gameId;
    MessageType type;
    BiddingRound round;

    public OutgoingMessage(Long gameId, MessageType type) {
        this.gameId = gameId;
        this.type = type;
    }

}
