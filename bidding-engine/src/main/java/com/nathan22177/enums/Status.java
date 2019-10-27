package com.nathan22177.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public final boolean active;
    Status() {
        this(true);
    }
}
