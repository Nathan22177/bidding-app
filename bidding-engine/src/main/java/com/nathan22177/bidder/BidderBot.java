package com.nathan22177.bidder;

import java.util.Random;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import org.springframework.util.Assert;

import com.nathan22177.enums.Opponent;
import com.nathan22177.game.Conditions;
import com.nathan22177.strategies.BiddingStrategy;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BidderBot extends AbstractBidder {

    /***
     * Strategy that defines how to bid.
     * */
    @Enumerated(EnumType.STRING)
    private Opponent opponent;
    @Embedded
    private String title;

    private final Random random = new Random();


    public BidderBot(Conditions conditions, Opponent opponent) {
        Assert.isTrue(conditions.getQuantity() % 2 == 0 && conditions.getQuantity() > 0, "Quantity must be a positive and even number.");
        Assert.isTrue(conditions.getCash() > 0, "Cash must be positive number.");
        setConditions(conditions);
        setBalance(conditions.getCash());
        setAcquiredAmount(0);
        this.opponent = opponent;
        this.title = opponent.getTitle();
    }


    public int getNextBid() {
        return this.opponent.getStrategy().getBiddingAmount(this);
    }

    /**
     * Used by JPA.
     */
    public BidderBot(){}
}
