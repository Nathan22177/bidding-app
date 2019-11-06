package com.nathan22177.websocket.messages.incoming;

import com.nathan22177.enums.Side;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Used to receive data (bids in particular) from a client.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Getter
@AllArgsConstructor
public class IncomingMessage {
    /**
     * Used nto identify other clients that
     * need to be notified about that message.
     */
    Long gameId;

    /**
     * User input.
     */
    int bid;

    /**
     * Used to identify which of the
     * two players has placed the bid.
     */
    Side side;
}
