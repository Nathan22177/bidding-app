package com.nathan22177.bidder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.springframework.util.Assert;

import com.nathan22177.game.Conditions;

import lombok.Getter;

@Entity
@Getter
public class BidderPlayer extends AbstractBidder {

    @Embedded
    String username;

    @Transient
    Session session;

    public BidderPlayer(Conditions conditions, String username) {
        Assert.isTrue(conditions.getQuantity() % 2 == 0 && conditions.getQuantity() > 0, "Quantity must be a positive and even number.");
        Assert.isTrue(conditions.getCash() > 0, "Cash must be positive number.");
        setConditions(conditions);
        setBalance(conditions.getCash());
        setAcquiredAmount(0);
        this.username = username;
    }

    /**
     * Used by JPA.
     */
    public BidderPlayer() {}
}