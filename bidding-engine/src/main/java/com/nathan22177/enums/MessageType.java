package com.nathan22177.enums;

/**
 * Describes to the client what kind of message came in.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
public enum MessageType {

    /**
     * Used to notify the client that they has entered matchmaking.
     */
    WAITING_FOR_OPPONENT,

    /**
     * Used to notify the client that the opponent was found.
     */
    PLAYER_JOINED,

    /**
     * Used to notify the client that the message contains bids.
     */
    BIDS,

    /**
     * Used to notify the client that it's opponent has left the game.
     */
    PLAYER_LEFT
}
