package com.nathan22177.bidder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.util.Assert;

import com.nathan22177.enums.Side;
import com.nathan22177.game.Conditions;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a player in the game.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Entity
@Getter
public class BidderPlayer extends AbstractBidder {

    /**
     * Represents side in pvp.
     */
    @Setter
    @Enumerated(EnumType.STRING)
    Side side;

    /**
     * Constructor that we use in order to instantiate bot at the start of a game.
     *
     * @param conditions initial {@link #balance} and amount
     *                   of winnable QUs.
     * @param name   {@link BidderPlayer#name} got from user input.
     * @param side       if player is blue or red.
     * @return new instance of {@link BidderBot}.
     */
    public BidderPlayer(Conditions conditions, String name, Side side) {
        Assert.isTrue(conditions.getQuantity() % 2 == 0 && conditions.getQuantity() > 0, "Quantity must be a positive and even number.");
        Assert.isTrue(conditions.getMoney() > 0, "Money must be positive number.");
        setConditions(conditions);
        setBalance(conditions.getMoney());
        setAcquiredAmount(0);
        setName(name);
        this.side = side;
    }

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     */
    public BidderPlayer() {
    }
}