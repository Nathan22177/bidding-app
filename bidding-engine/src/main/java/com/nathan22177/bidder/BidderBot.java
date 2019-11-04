package com.nathan22177.bidder;

import java.util.Random;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.util.Assert;

import com.nathan22177.enums.Bot;
import com.nathan22177.game.Conditions;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a bot in the game.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Entity
@Getter
@Setter
public class BidderBot extends AbstractBidder {

    /**
     * Contains information about the bot
     * and strategy that dictates how to bid.
     */
    @Enumerated(EnumType.STRING)
    private Bot bot;

    /**
     * Humanised name to show to the player
     * in order to not telegraph their strategy.
     */
    @Embedded
    private String title;

    /**
     * Instance of {@link Random} used by most of the strategies.
     **/
    private final Random random = new Random();

    /**
     * Constructor that we use in order to instantiate bot at the start of a game.
     *
     * @param conditions initial {@link #balance} and amount
     *                   of winnable QUs.
     * @param bot        defines {@link com.nathan22177.strategies.BiddingStrategy}
     *                   and {@link BidderBot#title}.
     * @return new instance of {@link BidderBot}.
     */
    public BidderBot(Conditions conditions, Bot bot) {
        Assert.isTrue(conditions.getQuantity() % 2 == 0 && conditions.getQuantity() > 0, "Quantity must be a positive and even number.");
        Assert.isTrue(conditions.getMoney() > 0, "Money must be positive number.");
        setConditions(conditions);
        setBalance(conditions.getMoney());
        setAcquiredAmount(0);
        this.bot = bot;
        this.title = bot.getTitle();
    }

    /**
     * Method for getting bot's ext bid.
     *
     * @return next bif of a bot according to it's {@link com.nathan22177.strategies.BiddingStrategy}.
     */
    public int getNextBid() {
        return this.bot.getStrategy().getBiddingAmount(this);
    }

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     */
    public BidderBot() {
    }
}
