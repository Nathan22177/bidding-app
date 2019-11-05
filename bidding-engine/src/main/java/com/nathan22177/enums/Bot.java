package com.nathan22177.enums;

import java.util.function.Function;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.bidder.strategies.CopycatStrategy;
import com.nathan22177.bidder.strategies.FairStrategy;
import com.nathan22177.bidder.strategies.LehaSVV2009Strategy;
import com.nathan22177.bidder.strategies.NathanStrategy;
import com.nathan22177.bidder.strategies.PyramidPlayerStrategy;
import com.nathan22177.bidder.strategies.RisingStrategy;
import com.nathan22177.bidder.strategies.SafeStrategy;
import com.nathan22177.bidder.strategies.WinnerIncrementStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Used to quickly fetch a list of available
 * bots and to instantiate one once chosen as an opponent.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Getter
@AllArgsConstructor
public enum Bot {
    /**
     * Random strategy
     * */
    RANDOM_BOT("RANDOM", null),

    /**
     * Bids it's opponent's last bid plus one if has advantage, else skips round.
     * */
    COPYCAT_BOT("Copycaster", CopycatStrategy::getBiddingAmount),

    /**
     * Always bids mean price of 2 QU's.
     * */
    FAIR_BOT("Lawful_Goodman", FairStrategy::getBiddingAmount),

    /**
     * lehaSVV2009s AwesomeBidder strategy refactored and appropriated.
     * Comments remain true to the source.
     * */
    LEHASVV2009_BOT("LehaSVV2009", LehaSVV2009Strategy::getBiddingAmount),

    /**
     * My own strategy.
     * */
    NATHAN22177_BOT("Nathan22177", NathanStrategy::getBiddingAmount),

    /**
     * PyramidPlayers AdvancedBidder strategy refactored and appropriated.
     * Comments remain true to the source.
     * */
    PYRAMID_PLAYER_BOT("Pyramid_Player", PyramidPlayerStrategy::getBiddingAmount),

    /**
     * Gradually raises bid so that would go with empty balance at the end.
     * */
    RISING_BOT("Riser", RisingStrategy::getBiddingAmount),

    /**
     * Waits for an advantage then bids median plus 2.
     * */
    SAFE_BOT("The Calm One", SafeStrategy::getBiddingAmount),

    /**
     * Waits for an advantage then bids last winning bid plus one;
     * */
    WIN_INC_BOT("Chad", WinnerIncrementStrategy::getBiddingAmount);

    /**
     * Humanised name to show to the player
     * in order to not telegraph their strategy.
     */
    private final String name;

    /**
     * Defines how bot evaluates how much to bid.
     */
    private final Function<BidderBot, Integer> strategy;
}
