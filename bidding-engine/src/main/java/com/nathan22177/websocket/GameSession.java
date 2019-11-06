package com.nathan22177.websocket;

import javax.websocket.Session;

import com.nathan22177.enums.Side;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Stored to quickly identify {@link Session}
 * that needs to be notified.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Getter
@AllArgsConstructor
public class GameSession {
    Long gameId;

    /**
     * WebSocket session.
     * */
    Session session;

    /**
     * Name of a player.
     * */
    String name;

    /**
     * Side of the player.
     * */
    Side side;
}
