package com.nathan22177.enums;

import com.nathan22177.strategies.BiddingStrategy;
import com.nathan22177.strategies.CopycatStrategy;
import com.nathan22177.strategies.FairStrategy;
import com.nathan22177.strategies.LehaSVV2009Strategy;
import com.nathan22177.strategies.NathanStrategy;
import com.nathan22177.strategies.PyramidPlayerStrategy;
import com.nathan22177.strategies.RisingStrategy;
import com.nathan22177.strategies.SafeStrategy;
import com.nathan22177.strategies.WinnerIncrementStrategy;

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
    /***
     * Random strategy
     * */
    RANDOM_BOT("RANDOM", null),

    /***
     * Bids it's opponent's last bid plus one if has advantage, else skips round.
     * */
    COPYCAT_BOT("Copycaster", new CopycatStrategy()),

    /***
     * Always bids mean price of 2 QU's.
     * */
    FAIR_BOT("Lawful_Goodman", new FairStrategy()),

    /***
     * lehaSVV2009s AwesomeBidder strategy refactored and appropriated.
     * Comments remain true to the source.
     * */
    LEHASVV2009_BOT("LehaSVV2009", new LehaSVV2009Strategy()),

    /***
     * My own strategy.
     * */
    NATHAN22177_BOT("Nathan22177", new NathanStrategy()),

    /***
     * PyramidPlayers AdvancedBidder strategy refactored and appropriated.
     * Comments remain true to the source.
     * */
    PYRAMID_PLAYER_BOT("Pyramid_Player", new PyramidPlayerStrategy()),

    /***
     * Gradually raises bid so that would go with empty balance at the end.
     * */
    RISING_BOT("Riser", new RisingStrategy()),

    /***
     * Waits for an advantage then bids median plus 2.
     * */
    SAFE_BOT("The Calm One", new SafeStrategy()),

    /***
     * Waits for an advantage then bids last winning bid plus one;
     * */
    WIN_INC_BOT("Chad", new WinnerIncrementStrategy());

    /**
     * Humanised name to show to the player
     * in order to not telegraph their strategy.
     */
    private final String name;

    /**
     * Defines how bot evaluates how much to bid.
     */
    private final BiddingStrategy strategy;
}
