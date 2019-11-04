package com.nathan22177.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Describes condition of the game.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@AllArgsConstructor
@Getter
public enum Status {
    MATCHMAKING(false),
    WAITING_FOR_BIDS,
    WAITING_FOR_BLUE,
    WAITING_FOR_RED,
    ENDED_PREMATURELY(false),
    BLUE_WON(false),
    RED_WON(false),
    DRAW(false);

    /**
     * Describes if game is still going.
     */
    public final boolean active;

    Status() {
        this(true);
    }
}
