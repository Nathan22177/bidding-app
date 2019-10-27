package com.nathan22177.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.nathan22177.strategies.BiddingStrategy;
import com.nathan22177.strategies.CopycatStrategy;
import com.nathan22177.strategies.FairStrategy;
import com.nathan22177.strategies.LehaSVV2009Strategy;
import com.nathan22177.strategies.NathanStrategy;
import com.nathan22177.strategies.PyramidPlayerStrategy;
import com.nathan22177.strategies.RisingStrategy;
import com.nathan22177.strategies.SafeStrategy;
import com.nathan22177.strategies.WinnerIncrementStrategy;

import lombok.Getter;

@Getter
public enum Opponent {
    /***
     * Random strategy
     * */
    RANDOM_BOT("RANDOM", null),

    /***
     * Another user playing trough web interface
     * */
    ANOTHER_PLAYER("Real human", null),

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

    private final String title;
    private final String name;
    private final BiddingStrategy strategy;


    public static final List<Opponent> botOptions = Arrays.stream(Opponent.values())
            .filter(opponent -> opponent != ANOTHER_PLAYER)
            .collect(Collectors.toList());

    Opponent(String title, BiddingStrategy strategy) {
        this.title = title;
        this.strategy = strategy;
        this.name = this.toString();
    }
}
