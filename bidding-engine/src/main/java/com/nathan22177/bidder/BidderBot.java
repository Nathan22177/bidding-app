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

@Entity
@Getter
@Setter
public class BidderBot extends AbstractBidder {

    /**
     * Contains information about the bot
     * and strategy that dictates how to bid.
     * */
    @Enumerated(EnumType.STRING)
    private Bot bot;
    @Embedded
    private String title;

    private final Random random = new Random();


    public BidderBot(Conditions conditions, Bot bot) {
        Assert.isTrue(conditions.getQuantity() % 2 == 0 && conditions.getQuantity() > 0, "Quantity must be a positive and even number.");
        Assert.isTrue(conditions.getCash() > 0, "Cash must be positive number.");
        setConditions(conditions);
        setBalance(conditions.getCash());
        setAcquiredAmount(0);
        this.bot = bot;
        this.title = bot.getTitle();
    }


    public int getNextBid() {
        return this.bot.getStrategy().getBiddingAmount(this);
    }

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     */
    public BidderBot(){}
}
