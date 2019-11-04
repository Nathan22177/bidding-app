package com.nathan22177.bidder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.springframework.util.Assert;

import com.nathan22177.collection.BiddingRound;
import com.nathan22177.game.Conditions;

import lombok.Getter;
import lombok.Setter;


/**
 * Used as a guideline to define participant of an auction.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Getter
@Setter
public abstract class AbstractBidder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /***
     * Amount of money left.
     * */
    private int balance;

    /***
     * Amount of won QUs.
     * */
    private int acquiredAmount;

    /***
     * Initial amount money and QU.
     * */
    @Embedded
    private Conditions conditions;

    /**
     * Name chosen by user to show to other players if it's a player,
     * and a humanised name to show to the player in order
     * to not telegraph their strategy if its a bot.
     */
    @Embedded
    private String name;


    /***
     * Track of {@link BiddingRound}s.
     * */
    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<BiddingRound> biddingHistory;

    /**
     * Used to withdraw money from {@link AbstractBidder#balance}.
     *
     * @param money amount of money to be withdrawn.
     */
    private void withdraw(int money) {
        this.balance -= money;
    }

    /**
     * Used to add quantity units to {@link AbstractBidder#acquiredAmount}.
     *
     * @param quantity amount of quantity won
     */
    private void acquire(int quantity) {
        Assert.isTrue(quantity <= 2, "Shouldn't be able to won more than 2 QU per round.");
        Assert.isTrue(quantity >= 0, "Shouldn't be able to subtract QUs.");
        this.acquiredAmount += quantity;
    }

    /**
     * Initiates money withdrawal from {@link AbstractBidder#balance}
     * and logging into {@link AbstractBidder#biddingHistory}.
     *
     * @param own   amount of money bid by the player.
     * @param other amount of money bid by the player's opponent.
     */
    public void resolveBidsAndAppendHistory(int own, int other) {
        appendBiddingHistory(own, other);
        resolveBids(own, other);
    }

    /**
     * Adds entry into the {@link AbstractBidder#biddingHistory}.
     *
     * @param own   amount of money bid by the player.
     * @param other amount of money bid by the player's opponent.
     */
    private void appendBiddingHistory(int own, int other) {
        if (getBiddingHistory() == null) {
            setBiddingHistory(new LinkedList<>(Collections.singletonList(new BiddingRound(own, other))));
        } else {
            getBiddingHistory().add(new BiddingRound(own, other));
        }
    }

    /**
     * Resolves amount of QU won by the player
     * and adds it to the {@link AbstractBidder#acquiredAmount}.
     *
     * @param own   amount of money bid by the player.
     * @param other amount of money bid by the player's opponent.
     */
    private void resolveBids(int own, int other) {
        if (other < own) {
            acquire(2);
        } else if (own == other) {
            acquire(1);
        } else {
            // players has not won anything
        }
    }

    /**
     * Public method that allows withdrawal of the money upon bidding
     *
     * @param bid amount of money bid by the player.
     */
    public void withdrawBiddingAmount(int bid) {
        Assert.isTrue(bid >= 0, "Bid should be positive number");
        Assert.isTrue(bid <= getBalance(), "Bid should not be larger than amount of money on the balance");
        withdraw(bid);
    }
}
