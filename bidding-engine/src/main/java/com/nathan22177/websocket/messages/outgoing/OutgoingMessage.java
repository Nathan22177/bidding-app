package com.nathan22177.websocket.messages.outgoing;

import com.nathan22177.enums.MessageType;
import com.nathan22177.game.dto.StateDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Used to send data (bids in particular) to a client.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
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
