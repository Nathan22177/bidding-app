package com.nathan22177.websocket.messages.outgoing;

import com.nathan22177.collection.BiddingRound;
import com.nathan22177.enums.MessageType;
import com.nathan22177.game.dto.StateDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OutgoingMessage {

    Long gameId;
    MessageType type;
    StateDTO state;
    public OutgoingMessage(Long gameId, MessageType type) {
        this.gameId = gameId;
        this.type = type;
    }
}
