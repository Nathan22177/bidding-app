package com.nathan22177.bidder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.util.Assert;

import com.nathan22177.enums.Side;
import com.nathan22177.game.Conditions;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class BidderPlayer extends AbstractBidder {

    @Embedded
    String username;

    @Setter
    @Enumerated(EnumType.STRING)
    Side side;

    public BidderPlayer(Conditions conditions, String username, Side side) {
        Assert.isTrue(conditions.getQuantity() % 2 == 0 && conditions.getQuantity() > 0, "Quantity must be a positive and even number.");
        Assert.isTrue(conditions.getCash() > 0, "Cash must be positive number.");
        setConditions(conditions);
        setBalance(conditions.getCash());
        setAcquiredAmount(0);
        this.username = username;
        this.side = side;
    }

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     */
    public BidderPlayer() {}
}